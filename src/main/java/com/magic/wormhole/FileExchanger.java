package com.magic.wormhole;

import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileExchanger {

    public void sendFile(Path filePath, ClientAddress address) {
        try (var fromChannel = FileChannel.open(filePath, StandardOpenOption.READ);
             var toChannel = SocketChannel.open(new InetSocketAddress(address.hostname(), address.port()))) {

            //write buffer
            var headerBuffer = Header.asByteBuffer(filePath);
            while (headerBuffer.hasRemaining()) {
                toChannel.write(headerBuffer);
            }

            //write content
            ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
            int bytesRead = fromChannel.read(buffer);
            while (bytesRead != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    toChannel.write(buffer);
                }
                buffer.clear();
                bytesRead = fromChannel.read(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void receiveFile(ReadableByteChannel fromChannel, Path pathTo) {
        try (fromChannel) {
            var buffer = ByteBuffer.allocateDirect(4096);
            var header = readHeader(fromChannel, buffer);
            var newFilePath = Paths.get(pathTo.toString(), header.filename());
            readContent(buffer, fromChannel, newFilePath);
            System.out.println("File with size [" + header.fileSize() + "] saved to: " + newFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readContent(ByteBuffer buffer, ReadableByteChannel fromChannel, Path newFilePath) throws IOException {
        try (var fileChannel = FileChannel.open(newFilePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            int bytesRead = fromChannel.read(buffer);
            while (bytesRead != -1) {
                buffer.flip();  // flip buffer for reading
                while (buffer.hasRemaining()) {
                    fileChannel.write(buffer);
                }
                buffer.compact();  // prepare buffer for writing
                bytesRead = fromChannel.read(buffer);
            }

            buffer.flip();
            while (buffer.hasRemaining()) {
                fileChannel.write(buffer);
            }
        }
    }

    private Header readHeader(ReadableByteChannel fromChannel, ByteBuffer buffer) throws IOException {
        fromChannel.read(buffer);
        buffer.flip();
        byte[] headerBytes = new byte[buffer.getInt()];
        buffer.get(headerBytes);
        var header = (Header) SerializationUtils.deserialize(headerBytes);

        buffer.compact();
        return header;
    }
}

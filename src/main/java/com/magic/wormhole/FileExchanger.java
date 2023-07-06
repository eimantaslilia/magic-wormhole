package com.magic.wormhole;

import org.springframework.stereotype.Component;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FileExchanger {

    public void sendFile(Path filePath) {
        try (var fromChannel = FileChannel.open(filePath, StandardOpenOption.READ);
             var toChannel = SocketChannel.open(new InetSocketAddress("localhost", 6666))) {

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
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.hh.mm.ss");
        String formattedDate = dateTimeFormatter.format(now);
        String fileName = "incoming_" + formattedDate + ".pdf";

        try (var fileChannel = FileChannel.open(Paths.get(pathTo.toString(), fileName), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
             fromChannel) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
            //read for the first time, has a header
            int bytesRead = fromChannel.read(buffer);
            buffer.flip();

//            int headerLength = ;
            byte[] headerBytes = new byte[buffer.getInt()];
            buffer.get(headerBytes);
            var header = (Header) SerializationUtils.deserialize(headerBytes);
            System.out.println(header);


            buffer.compact();
            bytesRead = fromChannel.read(buffer);

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

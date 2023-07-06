package com.magic.wormhole;

import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;

@Component
public class FileExchanger {

    record Header(String filename, String from) implements Serializable {
        public static final int HEADER_SIZE_IN_BYTES = 4;

        public static ByteBuffer asByteBuffer(Path filePath) {
            var header = new Header(filePath.getFileName().toString(), "John");
            var headerBytes = SerializationUtils.serialize(header);

            var headerBuffer = ByteBuffer.allocate(HEADER_SIZE_IN_BYTES + headerBytes.length);
            headerBuffer.putInt(headerBytes.length);
            headerBuffer.put(headerBytes);

            headerBuffer.flip();

            return headerBuffer;
        }
    }

    public void sendFile(FileChannel fromChannel, WritableByteChannel toChannel, Path filePath) {
        try (fromChannel; toChannel) {
            var headerBuffer = Header.asByteBuffer(filePath);
            while (headerBuffer.hasRemaining()) {
                toChannel.write(headerBuffer);
            }

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

    public void receiveFile(ReadableByteChannel fromChannel, FileChannel toChannel) {
        try (fromChannel; toChannel){
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
}

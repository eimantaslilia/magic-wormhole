package com.magic.wormhole;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

@Component
public class FileExchanger {

    public void sendFile(FileChannel fromChannel, WritableByteChannel toChannel) {
        try {
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

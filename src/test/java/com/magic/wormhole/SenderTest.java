package com.magic.wormhole;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SenderTest {

    private final Sender sender = new Sender();
    @TempDir
    private Path tempDir;

    @Test
    void shouldSendFile() throws Exception {
        Path file = Files.createFile(tempDir.resolve("test.txt"));
        assertTrue(Files.exists(file));
        Files.writeString(file, "Test data");

        try (var fileChannel = FileChannel.open(file, StandardOpenOption.READ)) {
            var buffer = ByteBuffer.allocate(4096);
            var outChannel = new WritableTestByteChannel(buffer);

            sender.sendFile(fileChannel, outChannel);

            buffer.flip();
            String result = StandardCharsets.UTF_8.decode(buffer).toString();
            assertEquals("Test data", result);
        }
    }

    record WritableTestByteChannel(ByteBuffer buffer) implements WritableByteChannel {
        @Override
        public int write(ByteBuffer src) {
            buffer.put(src);
            return src.position();
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public void close() {
        }
    }
}


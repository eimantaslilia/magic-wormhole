package com.magic.wormhole;

import org.junit.jupiter.api.io.TempDir;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;

public class FileExchangerTest {

    private final FileExchanger fileExchanger = new FileExchanger();
    @TempDir
    private Path tempDir;

//    @Test
//    void shouldSendFile() throws Exception {
//        Path file = Files.createFile(tempDir.resolve("test.txt"));
//        assertTrue(Files.exists(file));
//        Files.writeString(file, "Test data");
//
//        try (var fileChannel = FileChannel.open(file, StandardOpenOption.READ)) {
//            var buffer = ByteBuffer.allocate(4096);
//            var outChannel = new WritableTestByteChannel(buffer);
//
//            fileExchanger.sendFile(fileChannel, outChannel, file);
//            buffer.flip();
//
//            int headerSize = buffer.getInt();
//            byte[] headerBytes = new byte[headerSize];
//            buffer.get(headerBytes);
//            var header = (FileExchanger.Header) SerializationUtils.deserialize(headerBytes);
//            assertNotNull(header);
//            assertEquals("test.txt", header.filename());
//            assertEquals("John", header.from());
//
//            var content = new byte[buffer.remaining()];
//            buffer.get(content);
//
//            String result = new String(content, StandardCharsets.UTF_8);
//            assertEquals("Test data", result);
//        }
//    }

//    @Test
//    void shouldReceiveFile() throws Exception {
//        Path file = Files.createFile(tempDir.resolve("test.txt"));
//        assertTrue(Files.exists(file));
//
//        try (var fileChannel = FileChannel.open(file, StandardOpenOption.WRITE, StandardOpenOption.READ)) {
//            var buffer = ByteBuffer.wrap("testing".getBytes(StandardCharsets.UTF_8));
//            var inChannel = new ReadableTestByteChannel(buffer);
//
//            fileExchanger.receiveFile(inChannel, fileChannel);
//
//            String result = Files.readString(file);
//            assertEquals("testing", result);
//        }
//    }

    record WritableTestByteChannel(ByteBuffer buffer) implements WritableByteChannel {
        @Override
        public int write(ByteBuffer src) {
            buffer.put(src);
            return src.position();
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void close() {
        }
    }

    record ReadableTestByteChannel(ByteBuffer buffer) implements ReadableByteChannel {
        @Override
        public int read(ByteBuffer dst) {
            if (buffer.hasRemaining()) {
                int oldLimit = Math.min(buffer.remaining(), dst.remaining());
                for (int i = 0; i < oldLimit; i++) {
                    dst.put(buffer.get());
                }
                return oldLimit;
            } else {
                return -1;
            }
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


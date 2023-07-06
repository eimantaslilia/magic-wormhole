package com.magic.wormhole;

import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.file.Path;

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
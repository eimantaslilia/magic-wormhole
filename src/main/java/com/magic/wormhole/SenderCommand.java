package com.magic.wormhole;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
@CommandLine.Command(name = "send")
public class SenderCommand implements Runnable {

    @CommandLine.Option(names = "-p", required = true, description = "Path of the file")
    private Path filePath;

    private final FileExchanger fileExchanger = new FileExchanger();

    @Override
    public void run() {
        send();
    }

    private void send() {
        try {
            var fromChannel = FileChannel.open(filePath, StandardOpenOption.READ);
            var socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 6666));

            if (Files.exists(filePath)) {
                System.out.println("Sending file over network, filename: " + filePath);
                fileExchanger.sendFile(fromChannel, socketChannel);
            } else {
                System.out.println("Provide a file with the -p=<path> arg");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Path getFilePath() {
        return filePath;
    }
}

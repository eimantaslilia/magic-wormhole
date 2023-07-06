package com.magic.wormhole;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DummyServer implements ApplicationRunner {

    private final FileExchanger fileExchanger = new FileExchanger();

    private final Path receivingPath = Paths.get("C:\\Users\\ITWORK\\Desktop\\out");
    private final ExecutorService executors = Executors.newCachedThreadPool();

    @Override
    public void run(ApplicationArguments args) {
        if (!Files.isDirectory(receivingPath)) {
            throw new IllegalArgumentException("Provided path is not a directory, :" + receivingPath);
        }

        try (var serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(6666));

            System.out.println("Server is listening...");
            while (true) {
                var clientChannel = serverChannel.accept();
                executors.submit(() -> handleConnection(clientChannel));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleConnection(SocketChannel socketChannel) {
        System.out.println("handling incoming request...");
        fileExchanger.receiveFile(socketChannel, receivingPath);
    }
}
package com.magic.wormhole;

import picocli.CommandLine;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CommandLine.Command(name = "recv")
public class ReceiverCommand implements Runnable {

    private final FileExchanger fileExchanger = new FileExchanger();

    @CommandLine.Option(names = "-incomingPath", required = true, description = "Path of the receiving file directory")
    private Path fileDir;

    private final ExecutorService executors = Executors.newCachedThreadPool();

    @Override
    public void run() {
        if (!Files.isDirectory(fileDir)) {
            throw new IllegalArgumentException("Provided path is not a directory, :" + fileDir);
        }

        try (var serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(6666));

            System.out.println("Ready to accept connections...");
            while (true) {
                var clientChannel = serverChannel.accept();
                executors.submit(() -> handleConnection(clientChannel));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleConnection(SocketChannel socketChannel) {
        System.out.println("handling request...");
        fileExchanger.receiveFile(socketChannel, fileDir);
    }
}
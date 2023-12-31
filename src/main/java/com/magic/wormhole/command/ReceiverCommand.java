package com.magic.wormhole.command;

import com.magic.wormhole.FileExchanger;
import com.magic.wormhole.registry.RegistrarClient;
import com.magic.wormhole.registry.RegistrationRequest;
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

    private static final int DEFAULT_PORT = 8090;
    private static final String DEFAULT_NAME = "receiver";

    @CommandLine.Option(names = "-saveTo", required = true, description = "Path of the receiving file directory")
    private Path fileDir;

    @CommandLine.Option(names = "-registryIP", description = "IP of the registry")
    private String registryIP;

    @CommandLine.Option(names = "-port", description = "Port of the receiver")
    private int port = DEFAULT_PORT;

    @CommandLine.Option(names = "-name", description = "Name of the receiver")
    private String name = DEFAULT_NAME;

    private final ExecutorService executors = Executors.newCachedThreadPool();

    @Override
    public void run() {
        if (!Files.isDirectory(fileDir)) {
            throw new IllegalArgumentException("Provided path is not a directory, :" + fileDir);
        }

        RegistrarClient.register(registryIP, new RegistrationRequest(name, port));

        try (var serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(port));

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
        FileExchanger.receiveFile(socketChannel, fileDir);
    }
}
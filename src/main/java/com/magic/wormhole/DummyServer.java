package com.magic.wormhole;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DummyServer implements ApplicationRunner {

    private final FileExchanger fileExchanger = new FileExchanger();

    private final Path receivingPath = Paths.get("C:\\Users\\ITWORK\\Desktop\\out");
    private final ExecutorService executors = Executors.newCachedThreadPool();

    @Override
    public void run(ApplicationArguments args) {

        try(var serverChannel = ServerSocketChannel.open()) {
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
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.hh.mm.ss");
        String formattedDate = dateTimeFormatter.format(now);
        String fileName = "incoming_" + formattedDate + ".pdf";
        try (var fileChannel = FileChannel.open(Paths.get(receivingPath.toString(), fileName), StandardOpenOption.CREATE, StandardOpenOption.WRITE)){
            System.out.println("handling incoming request...will attempt to save to: " + receivingPath);
            fileExchanger.receiveFile(socketChannel, fileChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
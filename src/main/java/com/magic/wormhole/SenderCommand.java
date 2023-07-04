package com.magic.wormhole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
@CommandLine.Command(name = "send")
public class SenderCommand implements Runnable {

    @CommandLine.Option(names = "-p", required = true, description = "Path of the file")
    Path filePath;

    @Autowired
    private Sender sender;
    @Override
    public void run() {
        System.out.println("sender called with path: " + filePath);
        doStuff();
    }

    public void doStuff() {
        try (var socket = new Socket("127.0.0.1", 4444);
             var out = new PrintWriter(socket.getOutputStream(), true);
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            ping(out, in);

            sender.sendFile(FileChannel.open(filePath, StandardOpenOption.READ), socket.getChannel());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void ping(PrintWriter out, BufferedReader in) throws Exception {
        for (int i = 0; i < 10; i++) {
            out.println("hello from sender: " + i);
            String resp = in.readLine();
            System.out.println(resp);
            Thread.sleep(500);
        }
    }
}

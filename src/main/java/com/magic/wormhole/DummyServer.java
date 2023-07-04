package com.magic.wormhole;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DummyServer implements ApplicationRunner {

    private final ExecutorService executors = Executors.newCachedThreadPool();

    @Override
    public void run(ApplicationArguments args) {
        try (ServerSocket serverSocket = new ServerSocket(4444)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executors.submit(() -> connect(clientSocket));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void connect(Socket clientSocket) {
        try (Socket socket = clientSocket;
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            readPings(in, out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void readPings(BufferedReader in, PrintWriter out) throws Exception {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("server received: " + inputLine);
            out.println("server acknowledges!!!");
            Thread.sleep(500);
        }
    }
}

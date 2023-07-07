package com.magic.wormhole;

import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;

@CommandLine.Command(name = "send")
public class SenderCommand implements Runnable {

    private static final String DEFAULT_RECEIVER = "receiver";
    @CommandLine.Option(names = "-p", required = true, description = "Path of the file")
    private Path filePath;

    @CommandLine.Option(names = "-dest", description = "Name of the receiver")
    private String destination = DEFAULT_RECEIVER;

    private final FileExchanger fileExchanger = new FileExchanger();
    private final RegistrarClient registrarClient = new RegistrarClient();

    @Override
    public void run() {
        send();
    }

    private void send() {


        if (Files.exists(filePath)) {
            var address = registrarClient.fetchClientAddress(destination);

            System.out.println("Sending file: " + filePath + " to: " + address);
            fileExchanger.sendFile(filePath, address);
        } else {
            System.out.println("Provide a file with the -p=<path> arg");
        }
    }

    public Path getFilePath() {
        return filePath;
    }
}

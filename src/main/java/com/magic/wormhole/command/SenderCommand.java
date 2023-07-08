package com.magic.wormhole.command;

import com.magic.wormhole.FileExchanger;
import com.magic.wormhole.registry.RegistrarClient;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;

@CommandLine.Command(name = "send")
public class SenderCommand implements Runnable {

    private static final String DEFAULT_RECEIVER = "receiver";
    @CommandLine.Option(names = "-p", required = true, description = "Path of the file")
    private Path filePath;

    @CommandLine.Option(names = "-registryHost", description = "Hostname of the registry")
    private String registryHost;

    @CommandLine.Option(names = "-receiver", description = "Name of the receiver")
    private String receiver = DEFAULT_RECEIVER;

    @Override
    public void run() {
        send();
    }

    private void send() {
        if (Files.exists(filePath)) {
            var address = RegistrarClient.fetchClientAddress(registryHost, receiver);
            System.out.println("Sending file: " + filePath + " to: " + address);
            FileExchanger.sendFile(filePath, address);
        } else {
            System.out.println("Provide a file with the -p=<path> arg");
        }
    }

    public Path getFilePath() {
        return filePath;
    }
}

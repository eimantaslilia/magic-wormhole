package com.magic.wormhole.command;

import com.magic.wormhole.FileExchanger;
import com.magic.wormhole.registry.RegistrarClient;
import org.springframework.util.StopWatch;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;

@CommandLine.Command(name = "send")
public class SenderCommand implements Runnable {

    private static final String DEFAULT_RECEIVER = "receiver";
    @CommandLine.Option(names = "-p", required = true, description = "Path of the file")
    private Path filePath;

    @CommandLine.Option(names = "-registryIP", description = "IP of the registry")
    private String registryIP;

    @CommandLine.Option(names = "-receiver", description = "Name of the receiver")
    private String receiver = DEFAULT_RECEIVER;

    @Override
    public void run() {
        send();
    }

    private void send() {
        if (Files.exists(filePath)) {
            var stopWatch = new StopWatch();

            stopWatch.start();
            var address = RegistrarClient.fetchClientAddress(registryIP, receiver);
            stopWatch.stop();
            System.out.println("Fetching from registry took: [" + stopWatch.getTotalTimeSeconds() + "] seconds");

            System.out.println("Sending file: " + filePath + " to: " + address);

            stopWatch = new StopWatch();
            stopWatch.start();
            FileExchanger.sendFile(filePath, address);
            stopWatch.stop();

            System.out.println("File transfer took: [" + stopWatch.getTotalTimeSeconds() + "] seconds");
        } else {
            System.out.println("Provide a file with the -p=<path> arg");
        }
    }

    public Path getFilePath() {
        return filePath;
    }
}

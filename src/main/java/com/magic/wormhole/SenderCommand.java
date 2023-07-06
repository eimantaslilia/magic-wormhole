package com.magic.wormhole;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;

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
        if (Files.exists(filePath)) {
            System.out.println("Sending file: " + filePath);
            fileExchanger.sendFile(filePath);
        } else {
            System.out.println("Provide a file with the -p=<path> arg");
        }
    }

    public Path getFilePath() {
        return filePath;
    }
}

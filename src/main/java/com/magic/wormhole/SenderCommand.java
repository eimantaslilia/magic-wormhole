package com.magic.wormhole;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.nio.file.Path;

@Component
@CommandLine.Command(name = "send")
public class SenderCommand implements Runnable {

    @CommandLine.Option(names = "-p", required = true, description = "Path of the file")
    Path filePath;

    @Override
    public void run() {
        System.out.println("sender called with path: " + filePath);
    }
}

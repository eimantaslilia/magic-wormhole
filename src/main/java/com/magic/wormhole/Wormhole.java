package com.magic.wormhole;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "wormhole", subcommands = {SenderCommand.class, ReceiverCommand.class})
public class Wormhole implements Runnable {

    @Override
    public void run() {
        System.out.println("running wormhole...");
    }
}

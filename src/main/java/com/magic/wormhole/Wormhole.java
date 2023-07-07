package com.magic.wormhole;

import picocli.CommandLine;

@CommandLine.Command(name = "wormhole", subcommands = {SenderCommand.class, ReceiverCommand.class})
public class Wormhole implements Runnable {

    @Override
    public void run() {
    }
}

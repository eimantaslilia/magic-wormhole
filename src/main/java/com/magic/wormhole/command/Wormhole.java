package com.magic.wormhole.command;

import picocli.CommandLine;

@CommandLine.Command(name = "wormhole", subcommands = {SenderCommand.class, ReceiverCommand.class})
public class Wormhole implements Runnable {

    @Override
    public void run() {
    }
}

package com.magic.wormhole;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
public class MyAppRunner implements CommandLineRunner {

    private final CommandLine.IFactory factory;
    private final Wormhole wormhole;


    public MyAppRunner(CommandLine.IFactory factory, Wormhole wormhole) {
        this.factory = factory;
        this.wormhole = wormhole;
    }

    @Override
    public void run(String... args) {
        System.out.println("calling wormhole...");
        new CommandLine(wormhole, factory).execute(args);
    }
}
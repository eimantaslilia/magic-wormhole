package com.magic.wormhole;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        var commands = List.of("send", "recv");

        Arrays.stream(args)
                .filter(commands::contains)
                .findAny()
                .ifPresentOrElse(
						arg -> runCommand(args),
                        () -> runApp(args));
    }

    private static void runCommand(String[] args) {
		System.out.println("Executing commands only...");
		new CommandLine(new Wormhole()).execute(args);
    }

    private static void runApp(String[] args) {
        SpringApplication.exit(SpringApplication.run(Application.class, args));
    }
}

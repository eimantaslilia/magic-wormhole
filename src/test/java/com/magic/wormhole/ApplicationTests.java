package com.magic.wormhole;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = NONE, classes = Application.class)
class ApplicationTests {

    @Autowired
    private CommandLine.IFactory factory;

    @Autowired
    private Wormhole wormhole;

    @Test
    void shouldParseCLI() {
        var parseResult = new CommandLine(wormhole, factory)
                .parseArgs("send", "-p", "C:\\Projects");

        assertTrue(parseResult.hasSubcommand());

        var subcommand = parseResult.subcommand();

        var sender = (SenderCommand) subcommand.commandSpec().userObject();

        assertEquals("C:\\Projects", sender.getFilePath().toString());
    }
}

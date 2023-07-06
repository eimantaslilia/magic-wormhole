package com.magic.wormhole;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WormholeTest {

    @Test
    void shouldParseCLI() {
        var parseResult = new CommandLine(new Wormhole())
                .parseArgs("send", "-p", "C:\\Projects");

        assertTrue(parseResult.hasSubcommand());

        var subcommand = parseResult.subcommand();

        var sender = (SenderCommand) subcommand.commandSpec().userObject();

        assertEquals("C:\\Projects", sender.getFilePath().toString());
    }
}

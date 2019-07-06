package org.ships.implementation.sponge.platform;

import org.core.source.command.ConsoleSource;
import org.core.source.viewer.CommandViewer;
import org.ships.implementation.sponge.text.SText;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

public class PlatformConsole implements ConsoleSource {
    @Override
    public CommandViewer sendMessage(org.core.text.Text message) {
        org.spongepowered.api.Sponge.getServer().getConsole().sendMessage(((SText)message).toSponge());
        return this;
    }

    @Override
    public CommandViewer sendMessagePlain(String message) {
        org.spongepowered.api.Sponge.getServer().getConsole().sendMessage(Text.builder(message).build());
        return this;
    }

    @Override
    public boolean sudo(String wholeCommand) {
        return Sponge.getCommandManager().process(Sponge.getServer().getConsole(), wholeCommand).getSuccessCount().isPresent();
    }
}

package org.ships.implementation.sponge.platform;

import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.core.source.command.ConsoleSource;
import org.core.source.viewer.CommandViewer;
import org.core.text.Text;
import org.ships.implementation.sponge.text.SText;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;

import java.util.UUID;

public class PlatformConsole implements ConsoleSource {
    @Override
    public CommandViewer sendMessage(Text message, UUID uuid) {
        if(uuid == null){
            return sendMessage(message);
        }
        org.spongepowered.api.Sponge.getSystemSubject().sendMessage(Identity.identity(uuid), ((SText<?>)message).toSponge());
        return this;
    }

    @Override
    public CommandViewer sendMessage(org.core.text.Text message) {
        org.spongepowered.api.Sponge.getSystemSubject().sendMessage(((SText<?>)message).toSponge());
        return this;
    }

    @Override
    public CommandViewer sendMessagePlain(String message) {
        org.spongepowered.api.Sponge.getSystemSubject().sendMessage(Component.text(message));
        return this;
    }

    @Override
    public boolean sudo(String wholeCommand) {
        try {
            return Sponge.getCommandManager().process(Sponge.getSystemSubject(), Sponge.getServer().getBroadcastAudience(), wholeCommand).isSuccess();
        } catch (CommandException e) {
            e.printStackTrace();
        }
        return false;
    }
}

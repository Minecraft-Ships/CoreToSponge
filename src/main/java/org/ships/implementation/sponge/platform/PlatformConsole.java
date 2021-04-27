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
        if (uuid == null) {
            return sendMessage(message);
        }
        org.spongepowered.api.Sponge.systemSubject().sendMessage(Identity.identity(uuid), ((SText<?>) message).toSponge());
        return this;
    }

    @Override
    public CommandViewer sendMessage(org.core.text.Text message) {
        org.spongepowered.api.Sponge.systemSubject().sendMessage(((SText<?>) message).toSponge());
        return this;
    }

    @Override
    public CommandViewer sendMessagePlain(String message) {
        org.spongepowered.api.Sponge.systemSubject().sendMessage(Component.text(message));
        return this;
    }

    @Override
    public boolean sudo(String wholeCommand) {
        if (Sponge.isServerAvailable()) {
            try {
                return Sponge.server().commandManager().process(Sponge.systemSubject(), Sponge.server().broadcastAudience(), wholeCommand).isSuccess();
            } catch (CommandException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (Sponge.isClientAvailable()) {
            try {
                return Sponge.client().server().orElseThrow(() -> new IllegalStateException("Command manager on the client is not accessible")).commandManager().process(Sponge.systemSubject(), Sponge.server().broadcastAudience(), wholeCommand).isSuccess();

            } catch (CommandException e) {
                e.printStackTrace();
                return false;
            }
        }
        throw new IllegalStateException("Could not find client nor server state");
    }
}

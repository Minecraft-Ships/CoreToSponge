package org.core.implementation.sponge.platform;

import net.kyori.adventure.identity.Identity;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.source.command.ConsoleSource;
import org.core.source.viewer.CommandViewer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;

import java.util.UUID;

public class PlatformConsole implements ConsoleSource {

    @Override
    public CommandViewer sendMessage(AText message, UUID uuid) {
        org.spongepowered.api.Sponge.systemSubject().sendMessage(Identity.identity(uuid), ((AdventureText) message).getComponent());
        return this;
    }

    @Override
    public CommandViewer sendMessage(AText message) {
        org.spongepowered.api.Sponge.systemSubject().sendMessage(((AdventureText) message).getComponent());
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

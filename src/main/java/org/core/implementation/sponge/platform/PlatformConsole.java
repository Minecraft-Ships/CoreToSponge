package org.core.implementation.sponge.platform;

import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.core.source.command.ConsoleSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;

import java.util.UUID;

public class PlatformConsole implements ConsoleSource {

    @Override
    public void sendMessage(@NotNull Component message, @Nullable UUID uuid) {
        Sponge.systemSubject().sendMessage(uuid == null ? Identity.nil() : Identity.identity(uuid), message);
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        Sponge.systemSubject().sendMessage(message);
    }

    @Override
    public boolean sudo(String wholeCommand) {
        if (Sponge.isServerAvailable()) {
            try {
                return Sponge
                        .server()
                        .commandManager()
                        .process(Sponge.systemSubject(), Sponge.server().broadcastAudience(), wholeCommand)
                        .isSuccess();
            } catch (CommandException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (Sponge.isClientAvailable()) {
            try {
                return Sponge
                        .client()
                        .server()
                        .orElseThrow(() -> new IllegalStateException("Command manager on the client is not accessible"))
                        .commandManager()
                        .process(Sponge.systemSubject(), Sponge.server().broadcastAudience(), wholeCommand)
                        .isSuccess();

            } catch (CommandException e) {
                e.printStackTrace();
                return false;
            }
        }
        throw new IllegalStateException("Could not find client nor server state");
    }
}

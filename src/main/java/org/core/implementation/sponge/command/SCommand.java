package org.core.implementation.sponge.command;

import net.kyori.adventure.text.Component;
import org.core.TranslateCore;
import org.core.command.BaseCommandLauncher;
import org.core.exceptions.NotEnoughArguments;
import org.core.implementation.sponge.platform.PlatformConsole;
import org.core.implementation.sponge.platform.SpongePlatform;
import org.core.source.command.CommandSource;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SCommand implements Command.Raw {

    private final BaseCommandLauncher launcher;

    public SCommand(BaseCommandLauncher launcher) {
        this.launcher = launcher;
    }

    private CommandSource toCommandSource(Object sender) {
        SpongePlatform platform = (SpongePlatform) (TranslateCore.getPlatform());
        if (sender instanceof ServerPlayer player) {
            return (CommandSource) platform.createEntityInstance(player);
        }
        if (sender.equals(Sponge.systemSubject())) {
            return new PlatformConsole();
        }
        throw new RuntimeException("Unknown source of '" + sender.getClass().getName() + "'");
    }

    @Override
    public CommandResult process(CommandCause cause, ArgumentReader.Mutable arguments) throws CommandException {
        CommandSource sender = toCommandSource(cause.root());
        try {
            boolean result = this.launcher.run(sender, arguments.input().split(" "));
            return result ? CommandResult.success() : CommandResult.error(
                    Component.text(this.launcher.getUsage(sender)));
        } catch (NotEnoughArguments e) {
            return CommandResult.error(Component.text(e.getMessage()));
        }
    }

    @Override
    public List<CommandCompletion> complete(CommandCause cause, ArgumentReader.Mutable arguments)
            throws CommandException {
        CommandSource sender = toCommandSource(cause.root());
        List<String> result = this.launcher.tab(sender, arguments.input().split(" "));
        return result.stream().map(CommandCompletion::of).collect(Collectors.toList());
    }

    @Override
    public boolean canExecute(CommandCause cause) {
        CommandSource sender = toCommandSource(cause.root());
        return this.launcher.hasPermission(sender);
    }

    @Override
    public Optional<Component> shortDescription(CommandCause cause) {
        return Optional.of(this.launcher.getDescription()).map(Component::text);
    }

    @Override
    public Optional<Component> extendedDescription(CommandCause cause) {
        return Optional.empty();
    }

    @Override
    public Component usage(CommandCause cause) {
        CommandSource sender = toCommandSource(cause.root());
        return Component.text(this.launcher.getUsage(sender));
    }
}

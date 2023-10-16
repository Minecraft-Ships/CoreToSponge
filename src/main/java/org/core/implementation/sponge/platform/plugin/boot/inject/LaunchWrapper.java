package org.core.implementation.sponge.platform.plugin.boot.inject;

import org.core.command.CommandRegister;
import org.core.implementation.sponge.command.SCommand;
import org.core.logger.Logger;
import org.core.platform.plugin.CorePlugin;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.plugin.PluginContainer;

public class LaunchWrapper {

    private final CorePlugin plugin;
    private final Logger logger;
    private final PluginContainer container;

    public LaunchWrapper(CorePlugin plugin, Logger logger, PluginContainer container) {
        this.plugin = plugin;
        this.logger = logger;
        this.container = container;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public CorePlugin getPlugin() {
        return this.plugin;
    }

    @Listener
    public void onServerStarting(StartingEngineEvent<Server> event) {
        this.plugin.onCoreReady();
    }

    @Listener
    public void onCommandRegister(RegisterCommandEvent<Command.Raw> event) {
        CommandRegister cmdReg = new CommandRegister();
        this.plugin.onRegisterCommands(cmdReg);
        cmdReg.getCommands().forEach(launcher -> {
            Command.Raw command = new SCommand(launcher);
            event.register(this.container, command, launcher.getName(), launcher.getAliases());
        });
    }

}

package org.core.implementation.sponge.platform.plugin.boot.inject;

import org.core.logger.Logger;
import org.core.platform.plugin.CorePlugin;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;

public class LaunchWrapper {

    private final CorePlugin plugin;
    private final Logger logger;

    public LaunchWrapper(CorePlugin plugin, Logger logger) {
        this.plugin = plugin;
        this.logger = logger;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public CorePlugin getPlugin() {
        return this.plugin;
    }

    @Listener
    public void onServerStarting(StartingEngineEvent<Server> event){
        this.plugin.onCoreReady();
    }

}

package org.ships.implementation.sponge;

import org.core.CorePlugin;
import org.core.configuration.ConfigurationFile;
import org.core.configuration.type.ConfigurationLoaderType;
import org.core.event.EventManager;
import org.core.platform.Platform;
import org.core.platform.PlatformServer;
import org.core.schedule.SchedulerBuilder;

import java.io.File;

public class CoreToSponge extends CorePlugin.CoreImplementation {

    public CoreToSponge(){
        CoreImplementation.IMPLEMENTATION = this;
    }

    @Override
    public Platform getRawPlatform() {
        return null;
    }

    @Override
    public EventManager getRawEventManager() {
        return null;
    }

    @Override
    public SchedulerBuilder createRawSchedulerBuilder() {
        return null;
    }

    @Override
    public ConfigurationFile createRawConfigurationFile(File file, ConfigurationLoaderType type) {
        return null;
    }

    @Override
    public PlatformServer getRawServer() {
        return null;
    }
}

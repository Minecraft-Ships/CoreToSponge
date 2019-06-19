package org.ships.implementation.sponge;

import org.core.CorePlugin;
import org.core.configuration.ConfigurationFile;
import org.core.configuration.type.ConfigurationLoaderType;
import org.core.configuration.type.ConfigurationLoaderTypes;
import org.core.event.EventManager;
import org.core.platform.Platform;
import org.core.platform.PlatformServer;
import org.core.schedule.SchedulerBuilder;
import org.core.source.command.ConsoleSource;
import org.core.text.Text;
import org.ships.implementation.sponge.configuration.AbstractConfigurationFile;
import org.ships.implementation.sponge.configuration.JsonConfigurationLoaderType;
import org.ships.implementation.sponge.events.SEventManager;
import org.ships.implementation.sponge.platform.PlatformConsole;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.platform.SpongePlatformServer;
import org.ships.implementation.sponge.text.SText;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.File;
import java.io.IOException;

public class CoreToSponge extends CorePlugin.CoreImplementation {

    protected SpongePlatform platform = new SpongePlatform();
    protected SEventManager eventManager = new SEventManager();
    protected PlatformConsole console = new PlatformConsole();
    protected SpongePlatformServer server = new SpongePlatformServer(org.spongepowered.api.Sponge.getServer());

    public CoreToSponge(Object plugin){
        CoreImplementation.IMPLEMENTATION = this;
        org.spongepowered.api.Sponge.getEventManager().registerListeners(plugin, eventManager.getRawListener());
    }

    @Override
    public Platform getRawPlatform() {
        return this.platform;
    }

    @Override
    public EventManager getRawEventManager() {
        return this.eventManager;
    }

    @Override
    public ConsoleSource getRawConsole() {
        return this.console;
    }

    @Override
    public SchedulerBuilder createRawSchedulerBuilder() {
        return null;
    }

    @Override
    public ConfigurationFile createRawConfigurationFile(File file, ConfigurationLoaderType type) {
        if(file == null){
            new IOException("Unknown file").printStackTrace();
            return null;
        }
        if(type == null){
            new IOException("Unknown Configuration Loader Type").printStackTrace();
            return null;
        }
        File file2 = file;
        if(file.getName().endsWith(".temp")) {
            String name = file.getName().substring(0, file.getName().length() - 4);
            if (type.equals(ConfigurationLoaderTypes.YAML)) {
                file2 = new File(file.getParentFile(), name + "yaml");
            }else if(type.equals(ConfigurationLoaderTypes.DEFAULT) || type instanceof JsonConfigurationLoaderType){
                file2 = new File(file.getParentFile(), name + "conf");
            }else{
                System.err.println("Failed to read file extension. " + file.getName());
            }
        }else{
            System.err.println("File does not end with temp.");
        }
        return new AbstractConfigurationFile(file2, type);
    }

    @Override
    public PlatformServer getRawServer() {
        return this.server;
    }

    @Override
    public Text textBuilder(String chars) {
        return new SText(TextSerializers.FORMATTING_CODE.deserialize(chars));
    }
}

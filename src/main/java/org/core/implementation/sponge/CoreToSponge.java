package org.core.implementation.sponge;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.core.TranslateCore;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationStream;
import org.core.event.EventManager;
import org.core.implementation.sponge.boss.SServerBossBar;
import org.core.implementation.sponge.configuration.YAMLConfigurationFile;
import org.core.implementation.sponge.events.SEventManager;
import org.core.implementation.sponge.platform.PlatformConsole;
import org.core.implementation.sponge.platform.SpongePlatform;
import org.core.implementation.sponge.platform.SpongePlatformServer;
import org.core.implementation.sponge.scheduler.SSchedulerBuilder;
import org.core.platform.Platform;
import org.core.platform.PlatformServer;
import org.core.schedule.SchedulerBuilder;
import org.core.source.command.ConsoleSource;
import org.core.world.boss.ServerBossBar;
import org.spongepowered.plugin.PluginContainer;

import java.io.File;

public class CoreToSponge extends TranslateCore.CoreImplementation {

    protected final SpongePlatform platform = new SpongePlatform();
    protected final SEventManager eventManager = new SEventManager();
    protected final PlatformConsole console = new PlatformConsole();
    protected final SpongePlatformServer server = new SpongePlatformServer(org.spongepowered.api.Sponge.server());

    public CoreToSponge(PluginContainer plugin) {
        CoreImplementation.IMPLEMENTATION = this;
        org.spongepowered.api.Sponge.eventManager().registerListeners(plugin, this.eventManager.getRawListener());
        //TODO CHECK IF CORRECT
        //Task.builder().delayTicks(1).intervalTicks(1).name("tps").execute(getRawServer().getTPSExecutor()).build();
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
        return new SSchedulerBuilder();
    }

    @Override
    public ConfigurationStream.ConfigurationFile createRawConfigurationFile(File file, ConfigurationFormat type) {
        if (file==null) {
            throw new IllegalArgumentException("Unknown file");
        }
        if (type==null) {
            throw new IllegalArgumentException("Unknown Configuration Loader Format");
        }
        if (type.equals(ConfigurationFormat.FORMAT_YAML)) {
            return new YAMLConfigurationFile(file);
        }
        throw new IllegalStateException("Unknown format type of " + type.getName());
    }

    @Override
    public PlatformServer getRawServer() {
        return this.server;
    }

    @Override
    public ServerBossBar bossBuilder() {
        return new SServerBossBar(BossBar.bossBar(Component.empty(), 0, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS));
    }
}

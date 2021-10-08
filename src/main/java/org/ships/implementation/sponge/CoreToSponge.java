package org.ships.implementation.sponge;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.core.TranslateCore;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationStream;
import org.core.event.EventManager;
import org.core.platform.Platform;
import org.core.platform.PlatformServer;
import org.core.schedule.SchedulerBuilder;
import org.core.source.command.ConsoleSource;
import org.core.text.Text;
import org.core.world.boss.ServerBossBar;
import org.ships.implementation.sponge.boss.SServerBossBar;
import org.ships.implementation.sponge.configuration.YAMLConfigurationFile;
import org.ships.implementation.sponge.events.SEventManager;
import org.ships.implementation.sponge.platform.PlatformConsole;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.platform.SpongePlatformServer;
import org.ships.implementation.sponge.scheduler.SSchedulerBuilder;
import org.ships.implementation.sponge.text.SText;
import org.spongepowered.plugin.PluginContainer;

import java.io.File;
import java.io.IOException;

public class CoreToSponge extends TranslateCore.CoreImplementation {

    protected SpongePlatform platform = new SpongePlatform();
    protected SEventManager eventManager = new SEventManager();
    protected PlatformConsole console = new PlatformConsole();
    protected SpongePlatformServer server = new SpongePlatformServer(org.spongepowered.api.Sponge.server());

    public CoreToSponge(PluginContainer plugin) {
        CoreImplementation.IMPLEMENTATION = this;
        org.spongepowered.api.Sponge.eventManager().registerListeners(plugin, eventManager.getRawListener());
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
        if (file == null) {
            new IOException("Unknown file").printStackTrace();
            return null;
        }
        if (type == null) {
            new IOException("Unknown Configuration Loader Format").printStackTrace();
            return null;
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
    @Deprecated
    public Text textBuilder(String chars) {
        return SText.sign(chars);
    }

    @Override
    public ServerBossBar bossBuilder() {
        return new SServerBossBar(BossBar.bossBar(Component.empty(), 0, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS));
    }
}

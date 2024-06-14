package org.core.implementation.sponge;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.core.TranslateCore;
import org.core.config.ConfigManager;
import org.core.eco.CurrencyManager;
import org.core.event.EventManager;
import org.core.implementation.sponge.configuration.SConfigManager;
import org.core.implementation.sponge.currency.SCurrencyManager;
import org.core.implementation.sponge.events.SEventManager;
import org.core.implementation.sponge.platform.PlatformConsole;
import org.core.implementation.sponge.platform.SpongePlatform;
import org.core.implementation.sponge.platform.SpongePlatformServer;
import org.core.implementation.sponge.scheduler.SScheduleManager;
import org.core.platform.Platform;
import org.core.platform.PlatformServer;
import org.core.schedule.ScheduleManager;
import org.core.source.command.ConsoleSource;
import org.core.utils.Singleton;
import org.spongepowered.plugin.PluginContainer;

public class CoreToSponge extends TranslateCore.CoreImplementation {

    protected final SpongePlatform platform = new SpongePlatform();
    protected final SEventManager eventManager = new SEventManager();
    protected final SScheduleManager scheduleManager = new SScheduleManager();
    protected final PlatformConsole console = new PlatformConsole();
    protected final Singleton<SpongePlatformServer> server;
    private final CurrencyManager currencyManager = new SCurrencyManager();
    private final ConfigManager configManager = new SConfigManager();
    private final PluginContainer container;

    public CoreToSponge(PluginContainer plugin) {
        CoreImplementation.IMPLEMENTATION = this;
        org.spongepowered.api.Sponge
                .eventManager()
                .registerListeners(plugin, this.eventManager.getRawGeneralListener());
        org.spongepowered.api.Sponge
                .eventManager()
                .registerListeners(plugin, this.eventManager.getRawEntityInteractionListener());
        container = plugin;
        server = new Singleton<>(() -> new SpongePlatformServer(org.spongepowered.api.Sponge.server()));

        //TODO CHECK IF CORRECT
        //Task.builder().delayTicks(1).intervalTicks(1).name("tps").execute(getRawServer().getTPSExecutor()).build();
    }

    public PluginContainer container() {
        return this.container;
    }

    @Override
    public Platform getRawPlatform() {
        return this.platform;
    }

    @Override
    public ScheduleManager getRawScheduleManager() {
        return this.scheduleManager;
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
    public PlatformServer getRawServer() {
        return this.server.get();
    }

    @Override
    public ConfigManager getRawConfigManager() {
        return this.configManager;
    }

    @Override
    public CurrencyManager getRawCurrencyManager() {
        return this.currencyManager;
    }
}

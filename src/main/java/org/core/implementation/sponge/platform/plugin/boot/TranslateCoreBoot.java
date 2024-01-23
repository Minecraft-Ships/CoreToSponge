package org.core.implementation.sponge.platform.plugin.boot;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.core.TranslateCore;
import org.core.implementation.sponge.CoreToSponge;
import org.core.implementation.sponge.platform.SLogger;
import org.core.implementation.sponge.platform.plugin.boot.inject.LaunchWrapper;
import org.core.implementation.sponge.platform.plugin.boot.inject.PluginContainerWrapper;
import org.core.implementation.sponge.platform.plugin.boot.inject.SpongeInjector;
import org.core.platform.plugin.CorePlugin;
import org.core.platform.plugin.loader.CommonLoad;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Plugin("ships")
public class TranslateCoreBoot {

    private final CoreToSponge core;
    private final SLogger logger;
    private final List<LaunchWrapper> plugins = new ArrayList<>();

    @Inject
    public TranslateCoreBoot(PluginContainer container, Logger logger) {
        this.core = new CoreToSponge(container);
        this.logger = new SLogger(logger);
    }

    public CoreToSponge getCore() {
        return this.core;
    }

    @Listener
    public void onConstruct(ConstructPluginEvent event) {
        Optional<Class<? extends CorePlugin>> opLauncher = TranslateCore.getStandAloneLauncher();
        if (opLauncher.isEmpty()) {
            File folder = this.core.getRawPlatform().getTranslatePluginsFolder();
            this.plugins.addAll(this
                                        .loadPlugins(folder)
                                        .stream()
                                        .map(SpongeInjector::injectPlugin)
                                        .map(PluginContainerWrapper::instance)
                                        .collect(Collectors.toList()));
            return;
        } else {
            Class<? extends CorePlugin> pluginClass = opLauncher.get();
            CorePlugin plugin = CommonLoad.loadStandAlonePlugin(pluginClass);
            LaunchWrapper wrapper = new LaunchWrapper(plugin, new SLogger(LogManager.getLogger(plugin.getPluginId())),
                                                      this.core.container());
            Sponge.eventManager().registerListeners(this.core.container(), wrapper);
            this.plugins.add(wrapper);
        }

        this.plugins.forEach(lw -> lw.getPlugin().onConstruct(TranslateCoreBoot.this, lw.getLogger()));
    }

    private List<CorePlugin> loadPlugins(File folder) {
        if (!folder.exists()) {
            try {
                Files.createDirectories(folder.toPath());
            } catch (IOException e) {
                return Collections.emptyList();
            }
        }
        File[] files = folder.listFiles();
        if (null == files) {
            return Collections.emptyList();
        }
        return CommonLoad.loadPlugin(this.getClass().getClassLoader(), files);
    }

}

package org.core.implementation.sponge.platform.plugin.boot;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.core.TranslateCore;
import org.core.implementation.sponge.CoreToSponge;
import org.core.implementation.sponge.platform.SLogger;
import org.core.implementation.sponge.platform.plugin.boot.inject.SpongeInjector;
import org.core.platform.plugin.CorePlugin;
import org.core.platform.plugin.loader.CommonLoad;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Plugin("translate_core")
public class TranslateCoreBoot {

    private final CoreToSponge core;
    private final SLogger logger;

    @Inject
    public TranslateCoreBoot(PluginContainer container, Logger logger) {
        this.core = new CoreToSponge(container);
        this.logger = new SLogger(logger);
    }

    public void onConstruct(ConstructPluginEvent event) {
        Optional<Class<? extends CorePlugin>> opLauncher = TranslateCore.getStandAloneLauncher();
        if (opLauncher.isEmpty()) {
            File folder = this.core.getRawPlatform().getTranslatePluginsFolder();
            this.loadPlugins(folder);
            return;
        }
        Class<? extends CorePlugin> pluginClass = opLauncher.get();
        CorePlugin plugin = CommonLoad.loadStandAlonePlugin(pluginClass);
        org.core.logger.Logger logger = new SLogger(LogManager.getLogger(plugin.getPluginId()));
        SpongeInjector.injectPlugin(plugin);
        plugin.onConstruct(this, logger);
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
        if (files == null) {
            return Collections.emptyList();
        }
        List<CorePlugin> plugins = CommonLoad.loadPlugin(this.getClass().getClassLoader(), files);
        plugins.forEach(pl -> {
            SpongeInjector.injectPlugin(pl);
            org.core.logger.Logger logger = new SLogger(LogManager.getLogger(pl.getPluginId()));
            pl.onConstruct(TranslateCoreBoot.this, logger);
        });
        return plugins;
    }

}

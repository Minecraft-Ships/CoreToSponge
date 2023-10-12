package org.core.implementation.sponge.platform.plugin.boot.inject.vanilla;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.core.implementation.sponge.platform.SLogger;
import org.core.implementation.sponge.platform.plugin.boot.inject.LaunchWrapper;
import org.core.implementation.sponge.platform.plugin.boot.inject.PluginContainerWrapper;
import org.core.implementation.sponge.platform.plugin.boot.inject.PluginMetadataWrapper;
import org.core.platform.plugin.CorePlugin;
import org.spongepowered.plugin.metadata.PluginMetadata;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

public class VanillaPluginContainer implements PluginContainerWrapper {

    private final CorePlugin plugin;
    private final Logger logger;
    private final LaunchWrapper mainInstance;

    public VanillaPluginContainer(CorePlugin plugin) {
        this.plugin = plugin;
        this.logger = LogManager.getLogger(plugin.getPluginId());
        this.mainInstance = new LaunchWrapper(plugin, new SLogger(this.logger));
    }

    @Override
    public Optional<URI> locateResource(URI relative) {
        URL localPath = this.plugin.getClass().getResource(relative.getPath());
        return Optional.ofNullable(localPath).flatMap(url -> {
            try {
                return Optional.of(url.toURI());
            } catch (URISyntaxException e) {
                return Optional.empty();
            }
        });
    }

    @Override
    public PluginMetadata metadata() {
        return PluginMetadataWrapper.fromPlugin(this.plugin);
    }

    @Override
    public Logger logger() {
        return this.logger;
    }

    @Override
    public LaunchWrapper instance() {
        return this.mainInstance;
    }

    @Override
    public CorePlugin plugin() {
        return this.plugin;
    }
}

package org.core.implementation.sponge.platform.plugin;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.core.platform.plugin.Plugin;
import org.core.platform.plugin.details.CorePluginVersion;
import org.core.platform.plugin.details.PluginVersion;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.metadata.PluginMetadata;

public class SPlugin implements Plugin {

    private final PluginContainer container;

    public SPlugin(PluginContainer container) {
        this.container = container;
    }

    private PluginMetadata metadata() {
        return this.container.metadata();
    }

    @Override
    public @NotNull String getPluginName() {
        return this.metadata().name().orElseGet(() -> this.metadata().id());
    }

    @Override
    public @NotNull String getPluginId() {
        return this.metadata().id();
    }

    @Override
    public @NotNull PluginVersion getPluginVersion() {
        ArtifactVersion version = this.metadata().version();
        return new CorePluginVersion(version.getMajorVersion(), version.getMinorVersion(),
                                     version.getIncrementalVersion());
    }

    @Override
    public @NotNull Object getPlatformLauncher() {
        return this.container.instance();
    }
}

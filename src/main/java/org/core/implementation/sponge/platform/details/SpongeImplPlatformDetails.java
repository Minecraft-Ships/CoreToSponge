package org.core.implementation.sponge.platform.details;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.core.platform.PlatformDetails;
import org.core.platform.plugin.details.CorePluginVersion;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.plugin.metadata.PluginMetadata;

public class SpongeImplPlatformDetails implements PlatformDetails {
    @Override
    public @NotNull String getName() {
        PluginMetadata metadata = Sponge.platform().container(Platform.Component.IMPLEMENTATION).metadata();
        return metadata.name().orElse(metadata.id());
    }

    @Override
    public @NotNull String getIdName() {
        PluginMetadata metadata = Sponge.platform().container(Platform.Component.IMPLEMENTATION).metadata();
        return metadata.id();
    }

    @Override
    public @NotNull CorePluginVersion getVersion() {
        ArtifactVersion metadata = Sponge.platform().container(Platform.Component.IMPLEMENTATION).metadata().version();
        return new CorePluginVersion(metadata.getMajorVersion(), metadata.getMinorVersion(),
                                     metadata.getIncrementalVersion(), metadata.getQualifier(),
                                     metadata.getBuildNumber());
    }
}

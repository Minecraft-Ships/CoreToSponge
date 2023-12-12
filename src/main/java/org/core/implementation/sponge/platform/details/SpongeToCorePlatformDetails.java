package org.core.implementation.sponge.platform.details;

import org.core.platform.PlatformDetails;
import org.core.platform.plugin.details.CorePluginVersion;
import org.jetbrains.annotations.NotNull;

public class SpongeToCorePlatformDetails implements PlatformDetails {
    @Override
    public @NotNull String getName() {
        return "CoreToSponge";
    }

    @Override
    public @NotNull String getIdName() {
        return "sponge_to_core";
    }

    @Override
    public @NotNull CorePluginVersion getVersion() {
        return new CorePluginVersion(0, 0, 1, "snapshot", null);
    }

    @Override
    public char getTagChar() {
        return 'S';
    }
}

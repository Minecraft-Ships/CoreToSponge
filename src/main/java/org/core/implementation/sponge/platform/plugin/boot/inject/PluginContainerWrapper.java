package org.core.implementation.sponge.platform.plugin.boot.inject;

import org.core.platform.plugin.CorePlugin;
import org.spongepowered.plugin.PluginContainer;

public interface PluginContainerWrapper extends PluginContainer {

    @Override
    LaunchWrapper instance();

    CorePlugin plugin();
}

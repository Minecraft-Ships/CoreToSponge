package org.ships.implementation.sponge.configuration;

import org.core.configuration.type.ConfigurationLoaderType;

public class YamlConfigurationLoaderType implements ConfigurationLoaderType {

    @Override
    public String getId() {
        return "minecraft:yaml";
    }

    @Override
    public String getName() {
        return "yaml";
    }

}

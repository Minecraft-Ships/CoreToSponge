package org.ships.implementation.sponge.configuration;

import org.core.configuration.type.ConfigurationLoaderType;

public class JsonConfigurationLoaderType implements ConfigurationLoaderType {

    @Override
    public String getId() {
        return "minecraft:json";
    }

    @Override
    public String getName() {
        return "Json";
    }

    @Override
    public String[] acceptedFileExtensions() {
        return new String[]{"conf", "json", "gson"};
    }
}

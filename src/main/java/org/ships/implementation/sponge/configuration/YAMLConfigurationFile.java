package org.ships.implementation.sponge.configuration;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.core.config.ConfigurationFormat;

import java.io.File;
import java.io.IOException;

public class YAMLConfigurationFile extends AbstractConfigurationFile<ConfigurationNode, YAMLConfigurationLoader>{

    public YAMLConfigurationFile(File file) {
        super(file, YAMLConfigurationLoader.builder().setIndent(4).setFile(file).build());
    }

    @Override
    public ConfigurationFormat getFormat() {
        return ConfigurationFormat.FORMAT_YAML;
    }

    @Override
    public void reload() {
        try {
            this.root = this.loader.load();
        } catch (IOException e) {
            this.root = this.loader.createEmptyNode();
        }
    }
}

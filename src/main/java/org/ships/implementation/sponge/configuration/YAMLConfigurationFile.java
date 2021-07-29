package org.ships.implementation.sponge.configuration;

import org.core.config.ConfigurationFormat;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class YAMLConfigurationFile extends AbstractConfigurationFile<CommentedConfigurationNode, YamlConfigurationLoader> {

    public YAMLConfigurationFile(File file) {
        super(file, YamlConfigurationLoader
                .builder()
                .nodeStyle(NodeStyle.BLOCK)
                .indent(4)
                .file(file)
                .build());
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
            this.root = this.loader.createNode();
        }
    }
}

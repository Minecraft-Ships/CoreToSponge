package org.core.implementation.sponge.configuration;

import org.core.config.ConfigManager;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SConfigManager implements ConfigManager {
    @Override
    public ConfigurationFormat getDefaultFormat() {
        return ConfigurationFormat.FORMAT_YAML;
    }

    @Override
    public Collection<ConfigurationFormat> getSupportedFormats() {
        return List.of(ConfigurationFormat.FORMAT_YAML);
    }

    @Override
    public ConfigurationStream.ConfigurationFile read(@NotNull File file, @Nullable ConfigurationFormat format) {
        if (format == null) {
            format = this
                    .getSupportedFormats()
                    .stream()
                    .filter(conFormat -> Arrays
                            .stream(conFormat.getFileType())
                            .anyMatch(t -> file.getName().toLowerCase().endsWith(t.toLowerCase())))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown filetype for " + file.getName()));
        }
        if (format.equals(ConfigurationFormat.FORMAT_YAML)) {
            return new YAMLConfigurationFile(file);
        }
        throw new IllegalStateException("Unknown format type of " + format.getName());
    }
}

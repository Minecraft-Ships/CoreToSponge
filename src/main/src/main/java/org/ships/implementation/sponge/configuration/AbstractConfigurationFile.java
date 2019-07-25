package org.ships.implementation.sponge.configuration;

import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.core.configuration.ConfigurationFile;
import org.core.configuration.ConfigurationNode;
import org.core.configuration.parser.Parser;
import org.core.configuration.parser.StringMapParser;
import org.core.configuration.parser.StringParser;
import org.core.configuration.type.ConfigurationLoaderType;
import org.core.configuration.type.ConfigurationLoaderTypes;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AbstractConfigurationFile implements ConfigurationFile {

    protected File file;
    protected ConfigurationLoader loader;
    protected ninja.leaping.configurate.ConfigurationNode root;

    public AbstractConfigurationFile(File file, ConfigurationLoader built) {
        this.file = file;
        this.loader = built;
        try {
            this.root = loader.load();
        } catch (IOException e) {
            this.root = loader.createEmptyNode();
        }
    }

    public AbstractConfigurationFile(File file, ConfigurationLoaderType type) {
        this.file = file;
        if ((type.equals(ConfigurationLoaderTypes.DEFAULT)) || (type instanceof JsonConfigurationLoaderType)) {
            this.loader = ninja.leaping.configurate.hocon.HoconConfigurationLoader.builder().setFile(file).build();
        } else if (type instanceof YamlConfigurationLoaderType) {
            this.loader = ninja.leaping.configurate.yaml.YAMLConfigurationLoader.builder().setFile(file).build();
        }
        try {
            this.root = loader.load();
        } catch (IOException e) {
            this.root = loader.createEmptyNode();
        }
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public ConfigurationFile reload() {
        try {
            this.root = loader.load();
        } catch (IOException e) {
            this.root = loader.createEmptyNode();
        }
        return this;
    }

    @Override
    public Map<ConfigurationNode, Object> getKeyValues() {
        Map<ConfigurationNode, Object> map = new HashMap<>();
        root.getChildrenMap().forEach((key, value) -> map.put(new ConfigurationNode((String[]) value.getPath()), key));
        return map;
    }

    @Override
    public <T> Optional<T> parse(ConfigurationNode node, Parser<?, T> parser) {
        if (parser instanceof StringParser) {
            StringParser<T> parser1 = (StringParser<T>) parser;
            Object value = this.root.getNode((Object[]) node.getPath()).getValue();
            if (value == null) {
                return Optional.empty();
            }
            return parser1.parse(value.toString());
        } else if (parser instanceof StringMapParser) {
            StringMapParser<T> parser1 = (StringMapParser) parser;
            Map<String, String> map = new HashMap<>();
            ninja.leaping.configurate.ConfigurationNode node2 = this.root.getNode((Object[])node.getPath());
            parser1.getKeys().forEach(k -> {
                String value2 = node2.getNode(k).getString();
                map.put(k, value2);
            });
            if (map.isEmpty()) {
                return Optional.empty();
            }
            return parser1.parse(map);
        } else {
            System.err.println("Unknown Parser Type: The following are supported: StringParser<> or StringMapParser<>");
            new IOException("Parser " + parser.getClass().getSimpleName() + " failed").printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> parseString(ConfigurationNode node) {
        return Optional.ofNullable(this.root.getNode((Object[]) node.getPath()).getString());
    }

    @Override
    public Optional<Integer> parseInt(ConfigurationNode node) {
        return Optional.of(this.root.getNode((Object[]) node.getPath()).getInt());
    }

    @Override
    public Optional<Double> parseDouble(ConfigurationNode node) {
        return Optional.of(this.root.getNode((Object[]) node.getPath()).getDouble());
    }

    @Override
    public Optional<Boolean> parseBoolean(ConfigurationNode node) {
        return Optional.of(this.root.getNode((Object[]) node.getPath()).getBoolean());
    }

    @Override
    public <T> Optional<List<T>> parseList(ConfigurationNode node, StringParser<T> parser) {
        return Optional.of(this.root.getNode((Object[]) node.getPath()).getList(n -> {
            Optional<T> opValue = parser.parse(n.toString());
            if(opValue.isPresent()){
                return opValue.get();
            }
            return null;
        }));
    }

    @Override
    public <T> void set(ConfigurationNode node, Parser<?, T> parser, T value) {
        ninja.leaping.configurate.ConfigurationNode node2 = this.root.getNode((Object[]) node.getPath());
        if (parser instanceof StringMapParser) {
            StringMapParser<T> mapParser = ((StringMapParser) parser);
            Map<String, String> value2 = mapParser.unparse(value);
            value2.entrySet().forEach(e -> {
                Object[] args = e.getKey().split(".");
                if (args.length == 0) {
                    args = new Object[]{e.getKey()};
                }
                Object[] path = new Object[node2.getPath().length + args.length];
                int A = node2.getPath().length;
                for (int B = 0; B < A; B++) {
                    path[B] = node2.getPath()[B];
                }
                for (int B = 0; B < args.length; B++) {
                    path[A + B] = args[B];
                }
                root.getNode(path).setValue(e.getValue());
            });
        } else {
            Object value2 = parser.unparse(value);
            node2.setValue(value2);
        }
    }

    @Override
    public void set(ConfigurationNode node, Object value) {
        this.root.getNode((Object[])node.getPath()).setValue(value);
    }

    @Override
    public ConfigurationNode getRootNode() {
        return new ConfigurationNode((String[]) this.root.getPath());
    }

    @Override
    public void save() {
        try {
            if (!this.file.exists()) {
                this.file.getParentFile().mkdirs();
                this.file.createNewFile();
            }
            this.loader.save(this.root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package org.ships.implementation.sponge.configuration;

import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.core.config.parser.Parser;
import org.core.config.parser.StringMapParser;
import org.core.config.parser.StringParser;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AbstractConfigurationFile implements ConfigurationStream.ConfigurationFile {

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
    public ConfigurationFormat getFormat() {
        return null;
    }

    @Override
    public Optional<Double> getDouble(ConfigurationNode node) {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getInteger(ConfigurationNode node) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> getBoolean(ConfigurationNode node) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getString(ConfigurationNode node) {
        return Optional.empty();
    }

    @Override
    public <T, C extends Collection<T>> C parseCollection(ConfigurationNode node, Parser<String, T> parser, C collection) {
        return null;
    }

    @Override
    public void set(ConfigurationNode node, int value) {

    }

    @Override
    public void set(ConfigurationNode node, double value) {

    }

    @Override
    public void set(ConfigurationNode node, boolean value) {

    }

    @Override
    public void set(ConfigurationNode node, String value) {

    }

    @Override
    public <T> void set(ConfigurationNode node, Parser<String, T> parser, Collection<T> collection) {

    }

    @Override
    public Set<ConfigurationNode> getChildren(ConfigurationNode node) {
        return null;
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
        root.getChildrenMap().forEach((key, value) -> {
            map.put(new ConfigurationNode(value.getPath()), key);
        });
        return map;
    }

    @Override
    public <T> Optional<T> parse(ConfigurationNode node, Parser<?, T> parser) {
        if (parser instanceof StringParser) {
            StringParser<T> parser1 = (StringParser<T>) parser;
            Object value = this.root.getNode(node.getPath()).getValue();
            if (value == null) {
                return Optional.empty();
            }
            return parser1.parse(value.toString());
        } else if (parser instanceof StringMapParser) {
            StringMapParser<T> parser1 = (StringMapParser) parser;
            Map<String, String> map = new HashMap<>();
            ninja.leaping.configurate.ConfigurationNode node2 = this.root.getNode(node.getPath());
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
        return Optional.ofNullable(this.root.getNode(node.getPath()).getString());
    }

    @Override
    public Optional<Integer> parseInt(ConfigurationNode node) {
        Object[] path = node.getPath();
        ninja.leaping.configurate.ConfigurationNode cNode = this.root.getNode(path);
        if(!cNode.isVirtual()){
            return Optional.empty();
        }
        return Optional.of(cNode.getInt());
    }

    @Override
    public Optional<Double> parseDouble(ConfigurationNode node) {
        Object[] path = node.getPath();
        ninja.leaping.configurate.ConfigurationNode cNode = this.root.getNode(path);
        if(!cNode.isVirtual()){
            return Optional.empty();
        }
        return Optional.of(cNode.getDouble());
    }

    @Override
    public Optional<Boolean> parseBoolean(ConfigurationNode node) {
        Object[] path = node.getPath();
        ninja.leaping.configurate.ConfigurationNode cNode = this.root.getNode(path);
        if(!cNode.isVirtual()){
            return Optional.empty();
        }
        return Optional.of(cNode.getBoolean());
    }

    @Override
    public <T> Optional<List<T>> parseList(ConfigurationNode node, StringParser<T> parser) {
        return Optional.of(this.root.getNode(node.getPath()).getChildrenList().stream().map(c -> {
            String value = c.getString();
            Optional<T> opValue = parser.parse(value);
            if(opValue.isPresent()){
                return opValue.get();
            }
            return null;
        }).collect(Collectors.toList()));
    }

    @Override
    public String parseString(ConfigurationNode node, String defaut) {
        return this.root.getString(defaut);
    }

    @Override
    public int parseInt(ConfigurationNode node, int defaut) {
        return 0;
    }

    @Override
    public double parseDouble(ConfigurationNode node, double defaut) {
        return 0;
    }

    @Override
    public boolean parseBoolean(ConfigurationNode node, boolean defaut) {
        return false;
    }

    @Override
    public <T> List<T> parseList(ConfigurationNode node, StringParser<T> parser, List<T> defaut) {
        return null;
    }

    @Override
    public <T> void set(ConfigurationNode node, Parser<?, T> parser, T value) {
        ninja.leaping.configurate.ConfigurationNode node2 = this.root.getNode(node.getPath());
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
        Object[] path = node.getPath();
        this.root.getNode(path).setValue(value);
    }

    @Override
    public ConfigurationNode getRootNode() {
        return new ConfigurationNode(this.root.getPath());
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

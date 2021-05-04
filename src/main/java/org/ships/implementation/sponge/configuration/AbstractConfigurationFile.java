package org.ships.implementation.sponge.configuration;

import org.array.utils.ArrayUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.core.config.ConfigurationStream;
import org.core.config.parser.Parser;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractConfigurationFile<N extends ConfigurationNode, L extends ConfigurationLoader<N>> implements ConfigurationStream.ConfigurationFile {

    protected File file;
    protected L loader;
    protected N root;

    public AbstractConfigurationFile(File file, L loader) {
        this.file = file;
        this.loader = loader;
        this.reload();
    }

    @Override
    public File getFile() {
        return this.file;
    }

    private <T> Optional<T> get(org.core.config.ConfigurationNode node, Function<ConfigurationNode, T> value) {
        @NonNull ConfigurationNode node1 = this.root.node(node.getObjectPath());
        if (node1.empty()) {
            return Optional.empty();
        }
        return Optional.of(value.apply(node1));
    }

    @Override
    public Optional<Double> getDouble(org.core.config.ConfigurationNode node) {
        return get(node, ConfigurationNode::getDouble);
    }

    @Override
    public Optional<Integer> getInteger(org.core.config.ConfigurationNode node) {
        return get(node, ConfigurationNode::getInt);
    }

    @Override
    public Optional<Boolean> getBoolean(org.core.config.ConfigurationNode node) {
        return get(node, ConfigurationNode::getBoolean);
    }

    @Override
    public Optional<String> getString(org.core.config.ConfigurationNode node) {
        return get(node, ConfigurationNode::getString);
    }

    @Override
    public <T, C extends Collection<T>> C parseCollection(org.core.config.ConfigurationNode node, Parser<String, T> parser, C collection) {
        @NonNull ConfigurationNode node1 = this.root.node(node.getObjectPath());
        if (node1.empty()) {
            return collection;
        }
        if (!node1.isList()) {
            return collection;
        }
        List<T> list = node1
                .childrenList()
                .stream()
                .map(v -> parser.parse(v.toString()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        collection.addAll(list);
        return collection;
    }

    @Override
    public void set(org.core.config.ConfigurationNode node, int value) {
        try {
            this.root.set(value);
        } catch (SerializationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void set(org.core.config.ConfigurationNode node, double value) {
        try {
            this.root.set(value);
        } catch (SerializationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void set(org.core.config.ConfigurationNode node, boolean value) {
        try {
            this.root.set(value);
        } catch (SerializationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void set(org.core.config.ConfigurationNode node, String value) {
        try {
            this.root.set(value);
        } catch (SerializationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public <T> void set(org.core.config.ConfigurationNode node, Parser<String, T> parser, Collection<T> collection) {
        List<String> list = new ArrayList<>();
        collection.forEach(v -> list.add(parser.unparse(v)));
        try {
            this.root.node(node.getObjectPath()).set(list);
        } catch (SerializationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Set<org.core.config.ConfigurationNode> getChildren(org.core.config.ConfigurationNode node) {
        Collection<? extends ConfigurationNode> values = this.root.node(node.getObjectPath()).childrenList();
        Set<org.core.config.ConfigurationNode> set = new HashSet<>();
        values.stream().filter(n -> n.path().size() == (node.getPath().length + 1)).filter(n -> {
            for (int A = 0; A < node.getPath().length; A++) {
                if (!node.getPath()[A].equals(n.path().get(A).toString())) {
                    return false;
                }
            }
            return true;
        }).forEach(v -> set.add(new org.core.config.ConfigurationNode(ArrayUtils.convert(String.class, Object::toString, v.path()))));
        return set;
    }

    @Override
    public void save() {
        try {
            this.loader.save(this.root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

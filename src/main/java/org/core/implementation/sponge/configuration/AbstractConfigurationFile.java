package org.core.implementation.sponge.configuration;

import org.array.utils.ArrayUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.core.config.ConfigurationStream;
import org.core.config.parser.Parser;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractConfigurationFile<N extends ConfigurationNode, L extends ConfigurationLoader<N>>
        implements ConfigurationStream.ConfigurationFile {

    protected final File file;
    protected final L loader;
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

    private <T> Optional<T> get(org.core.config.ConfigurationNode node,
                                Function<? super ConfigurationNode, ? extends T> value) {
        @NonNull ConfigurationNode node1 = this.root.node(node.getObjectPath());
        if (node1.empty()) {
            return Optional.empty();
        }
        return Optional.of(value.apply(node1));
    }

    @Override
    public Optional<Double> getDouble(org.core.config.ConfigurationNode node) {
        return this.get(node, ConfigurationNode::getDouble);
    }

    @Override
    public Optional<Integer> getInteger(org.core.config.ConfigurationNode node) {
        return this.get(node, ConfigurationNode::getInt);
    }

    @Override
    public Optional<Boolean> getBoolean(org.core.config.ConfigurationNode node) {
        return this.get(node, ConfigurationNode::getBoolean);
    }

    @Override
    public Optional<String> getString(org.core.config.ConfigurationNode node) {
        return this.get(node, ConfigurationNode::getString);
    }

    @Override
    public <T, C extends Collection<T>> C parseCollection(org.core.config.@NotNull ConfigurationNode node,
                                                          @NotNull Parser<? super String, T> parser,
                                                          @NotNull C collection,
                                                          C defaultValue) {
        @NonNull ConfigurationNode node1 = this.root.node(node.getObjectPath());
        if (node1.empty()) {
            return defaultValue;
        }
        if (!node1.isList()) {
            return defaultValue;
        }
        List<T> list = node1.childrenList().stream().map(v -> {
            try {
                return parser.parse(v.getString());
            } catch (Throwable e) {
                throw new RuntimeException("Could not parse value at " + Arrays
                        .stream(v.path().array())
                        .map(Object::toString)
                        .collect(Collectors.joining("->")));
            }
        }).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        collection.addAll(list);
        return collection;
    }

    @Override
    public void set(org.core.config.ConfigurationNode node, int value) {
        try {
            this.root.node(node.getObjectPath()).set(value);
        } catch (SerializationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void set(org.core.config.ConfigurationNode node, double value) {
        try {
            this.root.node(node.getObjectPath()).set(value);
        } catch (SerializationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void set(org.core.config.ConfigurationNode node, boolean value) {
        try {
            this.root.node(node.getObjectPath()).set(value);
        } catch (SerializationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void set(org.core.config.ConfigurationNode node, String value) {
        try {
            this.root.node(node.getObjectPath()).set(value);
        } catch (SerializationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public <T> void set(org.core.config.ConfigurationNode node,
                        Parser<String, ? super T> parser,
                        Collection<T> collection) {
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
        values
                .stream()
                .filter(n -> n.path().size() == (node.getPath().length + 1))
                .filter(n -> {
                    for (int a = 0; a < node.getPath().length; a++) {
                        if (!node.getPath()[a].equals(n.path().get(a).toString())) {
                            return false;
                        }
                    }
                    return true;
                })
                .forEach(v -> set.add(new org.core.config.ConfigurationNode(
                        ArrayUtils.convert(String.class, Object::toString, v.path()))));
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

    @Override
    public Optional<Object> get(org.core.config.ConfigurationNode node) {
        ConfigurationNode target = this.root.node(node.getObjectPath());
        return this.get(target);
    }

    @Override
    public boolean isList(org.core.config.ConfigurationNode node) {
        ConfigurationNode target = this.root.node(node.getObjectPath());
        return target.isList();
    }

    @Override
    public boolean isMap(org.core.config.ConfigurationNode node) {
        ConfigurationNode target = this.root.node(node.getObjectPath());
        return target.isMap();
    }

    @Override
    public Map<Object, Object> getMap(org.core.config.ConfigurationNode node) {
        ConfigurationNode target = this.root.node(node.getObjectPath());
        return target
                .childrenMap()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> this.get(entry.getValue())));
    }

    @Override
    public void set(org.core.config.ConfigurationNode node, Map<String, ?> value) {
        value.forEach((key, value1) -> {
            String[] args = key.split(Pattern.quote("."));
            String[] fullArgs = ArrayUtils.join(String.class, node.getPath(), args);
            org.core.config.ConfigurationNode settingNode = new org.core.config.ConfigurationNode(fullArgs);
            if (value1 instanceof Map) {
                this.set(settingNode, (Map<String, ?>) value1);
                return;
            }
            try {
                Object[] fullNode = Arrays.stream(fullArgs).map(t -> (Object) t).toArray();
                this.root.node(fullNode).set(value1);
            } catch (SerializationException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private Optional<Object> get(ConfigurationNode target) {
        if (target.isNull()) {
            return Optional.empty();
        }
        if (target.isList()) {
            List<Object> list = target
                    .childrenList()
                    .stream()
                    .map(this::get)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            return Optional.of(list);
        }
        if (target.isMap()) {
            Map<String, Object> map = target
                    .childrenMap()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(entry -> entry.getKey().toString(), entry -> this.get(entry.getValue())));
            return Optional.of(map);
        }
        String value = target.getString();
        if (value != null) {
            throw new RuntimeException("Unknown parse for " + target.key());
        }
        try {
            if (value.contains(Pattern.quote("."))) {
                double num = Double.parseDouble(value);
                return Optional.of(num);
            }
            int num = Integer.parseInt(value);
            return Optional.of(num);
        } catch (NumberFormatException e) {
        }
        return Optional.of(value);
    }
}

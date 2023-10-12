package org.core.implementation.sponge.platform.plugin.boot.inject;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRange;
import org.core.platform.plugin.CorePlugin;
import org.core.platform.plugin.details.depends.DependsType;
import org.spongepowered.plugin.metadata.Container;
import org.spongepowered.plugin.metadata.PluginMetadata;
import org.spongepowered.plugin.metadata.model.PluginBranding;
import org.spongepowered.plugin.metadata.model.PluginContributor;
import org.spongepowered.plugin.metadata.model.PluginDependency;
import org.spongepowered.plugin.metadata.model.PluginLinks;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PluginMetadataWrapper implements PluginMetadata {

    private final ArtifactVersion version;
    private final CorePlugin plugin;

    private PluginMetadataWrapper(CorePlugin plugin, ArtifactVersion version) {
        this.plugin = plugin;
        this.version = version;
    }

    public static PluginMetadataWrapper fromPlugin(CorePlugin plugin) {
        return new PluginMetadataWrapper(plugin, new DefaultArtifactVersion(plugin.getPluginVersion().asString()));
    }


    @Override
    @Deprecated
    public Container container() {
        throw new RuntimeException("Not needed");
    }

    @Override
    public String id() {
        return this.plugin.getPluginId();
    }

    @Override
    public String entrypoint() {
        return this.plugin.getClass().getName();
    }

    @Override
    public Optional<String> name() {
        return Optional.of(this.plugin.getPluginName());
    }

    @Override
    public Optional<String> description() {
        return Optional.empty();
    }

    @Override
    public ArtifactVersion version() {
        return version;
    }

    @Override
    public PluginBranding branding() {
        return new PluginBranding() {
            @Override
            public Optional<String> icon() {
                return Optional.empty();
            }

            @Override
            public Optional<String> logo() {
                return Optional.empty();
            }
        };
    }

    @Override
    public PluginLinks links() {
        return new PluginLinks() {
            @Override
            public Optional<URL> homepage() {
                return Optional.empty();
            }

            @Override
            public Optional<URL> source() {
                return Optional.empty();
            }

            @Override
            public Optional<URL> issues() {
                return Optional.empty();
            }
        };
    }

    @Override
    public List<PluginContributor> contributors() {
        return Collections.emptyList();
    }

    @Override
    public Optional<PluginDependency> dependency(String id) {
        return dependencies().stream().filter(pd -> pd.id().equals(id)).findFirst();

    }

    @Override
    public Set<PluginDependency> dependencies() {
        return Arrays.stream(this.plugin.getDependingOn()).map(depend -> new PluginDependency() {
            @Override
            public String id() {
                return depend.platform();
            }

            @Override
            public VersionRange version() {
                String min = IntStream.of(depend.minVersion()).mapToObj(t -> t + "").collect(Collectors.joining("."));
                String max = IntStream.of(depend.maxVersion()).mapToObj(t -> t + "").collect(Collectors.joining("."));
                return VersionRange.createFromVersion(min + "-" + max);
            }

            @Override
            public LoadOrder loadOrder() {
                return LoadOrder.valueOf(depend.getLoadType().name());
            }

            @Override
            public boolean optional() {
                return DependsType.SOFT == depend.getDependsType();
            }
        }).collect(Collectors.toSet());
    }

    @Override
    public <T> Optional<T> property(String key) {
        throw new RuntimeException("Not needed");
    }

    @Override
    public Map<String, Object> properties() {
        throw new RuntimeException("Not needed");
    }
}

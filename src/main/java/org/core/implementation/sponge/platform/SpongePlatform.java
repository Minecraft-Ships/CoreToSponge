package org.core.implementation.sponge.platform;

import org.core.config.ConfigurationFormat;
import org.core.config.parser.unspecific.UnspecificParser;
import org.core.config.parser.unspecific.UnspecificParsers;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.LiveEntity;
import org.core.entity.living.animal.parrot.ParrotType;
import org.core.entity.living.animal.parrot.ParrotTypes;
import org.core.event.CustomEvent;
import org.core.implementation.sponge.entity.SEntityType;
import org.core.implementation.sponge.entity.forge.live.SForgeEntity;
import org.core.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.core.implementation.sponge.events.SpongeListener;
import org.core.implementation.sponge.inventory.SItemType;
import org.core.implementation.sponge.world.position.block.SBlockType;
import org.core.implementation.sponge.world.position.block.entity.furnace.SLiveFurnaceEntity;
import org.core.implementation.sponge.world.position.block.entity.sign.SLiveSignEntity;
import org.core.implementation.sponge.world.position.block.entity.sign.SSignTileEntitySnapshot;
import org.core.implementation.sponge.world.position.flags.SApplyPhysicsFlag;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.data.dye.DyeType;
import org.core.inventory.item.data.dye.DyeTypes;
import org.core.inventory.item.type.ItemTypeCommon;
import org.core.permission.CorePermission;
import org.core.permission.Permission;
import org.core.platform.Platform;
import org.core.platform.PlatformDetails;
import org.core.platform.plugin.Plugin;
import org.core.platform.plugin.details.CorePluginVersion;
import org.core.platform.update.PlatformUpdate;
import org.core.utils.Singleton;
import org.core.world.boss.colour.BossColour;
import org.core.world.boss.colour.BossColours;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.block.entity.banner.pattern.PatternLayerType;
import org.core.world.position.block.entity.banner.pattern.PatternLayerTypes;
import org.core.world.position.block.grouptype.BlockGroup;
import org.core.world.position.block.grouptype.BlockGroups;
import org.core.world.position.flags.physics.ApplyPhysicsFlag;
import org.core.world.position.flags.physics.ApplyPhysicsFlags;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.core.world.structure.Structure;
import org.core.world.structure.StructureBuilder;
import org.core.world.structure.StructureFileBuilder;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.registry.RegistryEntry;
import org.spongepowered.api.registry.RegistryTypes;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SpongePlatform implements Platform {

    protected final Set<? extends EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> entityTypes = new HashSet<>();
    protected final Map<Class<? extends org.spongepowered.api.entity.Entity>, Class<? extends LiveEntity>> entityToEntityMap = new HashMap<>();
    protected final Map<Class<? extends org.spongepowered.api.block.entity.BlockEntity>, Class<? extends LiveTileEntity>> blockStateToTileEntity = new HashMap<>();
    protected final Collection<TileEntitySnapshot<? extends TileEntity>> defaultTileEntities = new HashSet<>();
    protected final Collection<UnspecificParser<?>> unspecificParsers = new HashSet<>();
    protected final Set<Permission> permissions = new HashSet<>();

    public SpongePlatform() {
        this.entityToEntityMap.put(org.spongepowered.api.entity.living.player.Player.class, SLivePlayer.class);

        this.blockStateToTileEntity.put(org.spongepowered.api.block.entity.Sign.class, SLiveSignEntity.class);
        this.blockStateToTileEntity.put(org.spongepowered.api.block.entity.carrier.furnace.Furnace.class,
                                        SLiveFurnaceEntity.class);

        this.defaultTileEntities.add(new SSignTileEntitySnapshot());
    }

    public <E extends LiveEntity, S extends EntitySnapshot<E>> Optional<S> createSnapshot(EntityType<E, ? extends S> type,
                                                                                          SyncExactPosition pos) {
        try {
            S snapshot = type.getSnapshotClass().getConstructor(SyncExactPosition.class).newInstance(pos);
            return Optional.of(snapshot);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public LiveEntity createEntityInstance(org.spongepowered.api.entity.Entity entity) {
        Optional<Map.Entry<Class<? extends org.spongepowered.api.entity.Entity>, Class<? extends LiveEntity>>> opEntry = this.entityToEntityMap
                .entrySet()
                .stream()
                .filter(e -> e.getKey().isInstance(entity))
                .findAny();
        if (opEntry.isEmpty()) {
            return new SForgeEntity(entity);
        }
        Class<? extends LiveEntity> bdclass = opEntry.get().getValue();
        try {
            Constructor<? extends LiveEntity> constructor = bdclass.getConstructor(
                    org.spongepowered.api.entity.Entity.class);
            return constructor.newInstance(entity);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<LiveTileEntity> createTileEntityInstance(org.spongepowered.api.block.entity.BlockEntity tileEntity) {
        Optional<Map.Entry<Class<? extends org.spongepowered.api.block.entity.BlockEntity>, Class<? extends LiveTileEntity>>> opEntry = this.blockStateToTileEntity
                .entrySet()
                .stream()
                .filter(e -> e.getKey().isInstance(tileEntity))
                .findAny();
        if (opEntry.isEmpty()) {
            return Optional.empty();
        }
        Class<? extends LiveTileEntity> bdclass = opEntry.get().getValue();
        try {
            return Optional.of(bdclass
                                       .getConstructor(org.spongepowered.api.block.entity.BlockEntity.class)
                                       .newInstance(tileEntity));
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public @NotNull CorePluginVersion getMinecraftVersion() {
        return new CorePluginVersion(1, 16, 5);
    }

    @Override
    public @NotNull PlatformDetails getDetails() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NotNull ConfigurationFormat getConfigFormat() {
        return ConfigurationFormat.FORMAT_YAML;
    }

    @Override
    public @NotNull Set<Plugin> getPlugins() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public @NotNull File getPlatformPluginsFolder() {
        return new File("mods");
    }

    @Override
    public @NotNull File getPlatformConfigFolder() {
        return new File("configuration");
    }

    @Override
    public <E extends CustomEvent> @NotNull E callEvent(@NotNull E event) {
        return SpongeListener.call(event);
    }

    @Override
    public @NotNull Optional<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getEntityType(
            @NotNull String id) {
        if (id.equals("minecraft:player")) {
            return Optional.of(new SEntityType.SPlayerType());
        }
        Optional<? extends EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> opEntityType = this.entityTypes
                .parallelStream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
        if (opEntityType.isPresent()) {
            return opEntityType.map(e -> e);
        }
        String[] split = id.split(":");
        Optional<RegistryEntry<org.spongepowered.api.entity.EntityType<?>>> opType = RegistryTypes.ENTITY_TYPE
                .get()
                .findEntry(ResourceKey.of(split[0], split[1]));
        return opType.map(t -> new SEntityType.SForgedEntityType(t.value()));
    }

    @Override
    public @NotNull Optional<BlockType> getBlockType(@NotNull String id) {
        String[] splitId = id.split(":");
        Optional<RegistryEntry<org.spongepowered.api.block.BlockType>> opType = RegistryTypes.BLOCK_TYPE
                .get()
                .findEntry(ResourceKey.of(splitId[0], splitId[1]));
        return opType.map(r -> new SBlockType(r.value()));
    }

    @Override
    public @NotNull Optional<ItemType> getItemType(@NotNull String id) {
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<DyeType> getDyeType(@NotNull String id) {
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<PatternLayerType> getPatternLayerType(@NotNull String id) {
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<BossColour> getBossColour(@NotNull String id) {
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<ParrotType> getParrotType(@NotNull String id) {
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<ApplyPhysicsFlag> getApplyPhysics(@NotNull String id) {
        return Optional.empty();
    }

    @Override
    @Deprecated
    public @NotNull Optional<UnspecificParser<?>> getUnspecifiedParser(@NotNull String id) {
        return this.unspecificParsers.stream().filter(f -> f.getId().equals(id)).findFirst();
    }

    @Override
    public @NotNull Collection<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getEntityTypes() {
        return RegistryTypes.ENTITY_TYPE
                .get()
                .stream()
                .map(e -> this.getEntityType(e.key(RegistryTypes.ENTITY_TYPE).asString()))
                .filter(Optional::isEmpty)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public @NotNull Collection<BlockType> getBlockTypes() {
        return RegistryTypes.BLOCK_TYPE.get().stream().map(SBlockType::new).collect(Collectors.toSet());
    }

    @Override
    public @NotNull Collection<ItemType> getItemTypes() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NotNull Collection<DyeType> getDyeTypes() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NotNull Collection<PatternLayerType> getPatternLayerTypes() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NotNull Collection<BlockGroup> getBlockGroups() {
        return BlockGroups.values();
    }

    @Override
    public @NotNull Collection<BossColour> getBossColours() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NotNull Collection<ParrotType> getParrotType() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NotNull Collection<ApplyPhysicsFlag> getApplyPhysics() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NotNull Collection<Permission> getPermissions() {
        return this.permissions;
    }

    @Override
    public @NotNull Collection<Structure> getStructures() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public @NotNull Structure register(StructureBuilder builder) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public @NotNull Structure register(StructureFileBuilder builder) throws IOException {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public @NotNull Permission register(@NotNull String permissionNode) {
        return this.register(new CorePermission(false, permissionNode.split("\\.")));
    }

    @Override
    public @NotNull CorePermission register(CorePermission permissionNode) {
        this.permissions.add(permissionNode);
        return permissionNode;
    }

    @Override
    @Deprecated
    public @NotNull Collection<UnspecificParser<?>> getUnspecifiedParsers() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NotNull Collection<TileEntitySnapshot<? extends TileEntity>> getDefaultTileEntities() {
        return this.defaultTileEntities;
    }

    @Override
    public @NotNull Collection<PlatformUpdate<?>> getUpdateCheckers() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull Singleton<BossColour> get(BossColours colours) {
        return new Singleton<>(() -> {
            throw new RuntimeException("Not implemented");
        });
    }

    @Override
    @Deprecated
    public <T> @NotNull UnspecificParser<T> get(@NotNull UnspecificParsers<T> parsers) {
        return (UnspecificParser<T>) this
                .getUnspecifiedParser(parsers.getId())
                .orElseThrow(() -> new RuntimeException("Cannot find unspecified parser"));
    }

    @Override
    public @NotNull Singleton<ApplyPhysicsFlag> get(@NotNull ApplyPhysicsFlags flags) {
        return switch (flags.getId()) {
            case "none" -> new Singleton<>(() -> SApplyPhysicsFlag.NONE);
            case "default" -> new Singleton<>(() -> SApplyPhysicsFlag.DEFAULT);
            default -> throw new RuntimeException("Unknown Applied Physics Flag of: " + flags.getId());
        };
    }

    @Override
    public @NotNull Singleton<ItemType> get(@NotNull ItemTypeCommon itemId) {
        Supplier<ItemType> supplier = () -> RegistryTypes.ITEM_TYPE
                .get()
                .stream()
                .filter(i -> i.key(RegistryTypes.ITEM_TYPE).asString().equals(itemId.getId()))
                .findAny()
                .map(SItemType::new)
                .orElseThrow(() -> new IllegalStateException("Unknown item of " + itemId.getId()));
        return new Singleton<>(supplier);
    }

    @Override
    public @NotNull Singleton<ParrotType> get(@NotNull ParrotTypes parrotID) {
        return new Singleton<>(() -> {
            throw new RuntimeException("Not implemented");
        });
    }

    @Override
    public @NotNull Singleton<DyeType> get(@NotNull DyeTypes id) {
        return new Singleton<>(() -> {
            throw new RuntimeException("Not implemented");
        });
    }

    @Override
    public @NotNull Singleton<PatternLayerType> get(@NotNull PatternLayerTypes id) {
        return new Singleton<>(() -> {
            throw new RuntimeException("Not implemented");
        });
    }

    @Override
    public <E extends LiveEntity, S extends EntitySnapshot<E>> @NotNull Singleton<EntityType<E, S>> get(@NotNull EntityTypes<E, S> entityId) {
        return new Singleton<>(() -> {
            throw new RuntimeException("Not implemented");
        });
    }

}

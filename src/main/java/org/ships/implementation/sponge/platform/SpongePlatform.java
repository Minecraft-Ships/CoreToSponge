package org.ships.implementation.sponge.platform;

import net.kyori.adventure.text.format.NamedTextColor;
import org.array.utils.ArrayUtils;
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
import org.core.inventory.item.ItemType;
import org.core.inventory.item.data.dye.DyeType;
import org.core.inventory.item.data.dye.DyeTypes;
import org.core.inventory.item.type.ItemTypeCommon;
import org.core.permission.Permission;
import org.core.platform.Platform;
import org.core.platform.PlatformDetails;
import org.core.platform.plugin.Plugin;
import org.core.text.TextColour;
import org.core.text.TextColours;
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
import org.jetbrains.annotations.NotNull;
import org.ships.implementation.sponge.entity.SEntityType;
import org.ships.implementation.sponge.entity.forge.live.SForgeEntity;
import org.ships.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.ships.implementation.sponge.events.SpongeListener;
import org.ships.implementation.sponge.inventory.SItemType;
import org.ships.implementation.sponge.text.STextColour;
import org.ships.implementation.sponge.world.position.block.SBlockType;
import org.ships.implementation.sponge.world.position.block.entity.furnace.SLiveFurnaceEntity;
import org.ships.implementation.sponge.world.position.block.entity.sign.SLiveSignEntity;
import org.ships.implementation.sponge.world.position.block.entity.sign.SSignTileEntitySnapshot;
import org.ships.implementation.sponge.world.position.flags.SApplyPhysicsFlag;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.registry.RegistryEntry;
import org.spongepowered.api.registry.RegistryTypes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SpongePlatform implements Platform {

    protected Set<? extends EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> entityTypes = new HashSet<>();
    protected Map<Class<? extends org.spongepowered.api.entity.Entity>, Class<? extends LiveEntity>> entityToEntityMap = new HashMap<>();
    protected Map<Class<? extends org.spongepowered.api.block.entity.BlockEntity>, Class<? extends LiveTileEntity>> blockStateToTileEntity = new HashMap<>();
    protected Collection<TileEntitySnapshot<? extends TileEntity>> defaultTileEntities = new HashSet<>();
    protected Collection<UnspecificParser<? extends Object>> unspecificParsers = new HashSet<>();
    protected Set<Permission> permissions = new HashSet<>();

    public SpongePlatform() {
        this.entityToEntityMap.put(org.spongepowered.api.entity.living.player.Player.class, SLivePlayer.class);

        this.blockStateToTileEntity.put(org.spongepowered.api.block.entity.Sign.class, SLiveSignEntity.class);
        this.blockStateToTileEntity.put(org.spongepowered.api.block.entity.carrier.furnace.Furnace.class, SLiveFurnaceEntity.class);

        this.defaultTileEntities.add(new SSignTileEntitySnapshot());
    }

    /*public CommandSource get(org.spongepowered.api.command.CommandSource source){
        if (source instanceof org.spongepowered.api.entity.living.player.Player) {
            return new SLivePlayer((org.spongepowered.api.entity.living.player.Player)source);
        }
        if(source instanceof org.spongepowered.api.command.source.CommandBlockSource){
            //no command block yet
            return null;
        }
        if(source instanceof org.spongepowered.api.command.source.ConsoleSource){
            return CorePlugin.getConsole();
        }
        return null;
    }*/

    public <E extends LiveEntity, S extends EntitySnapshot<E>> Optional<S> createSnapshot(EntityType<E, S> type, SyncExactPosition pos) {
        try {
            S snapshot = type.getSnapshotClass().getConstructor(SyncExactPosition.class).newInstance(pos);
            return Optional.of(snapshot);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public LiveEntity createEntityInstance(org.spongepowered.api.entity.Entity entity) {
        Optional<Map.Entry<Class<? extends org.spongepowered.api.entity.Entity>, Class<? extends LiveEntity>>> opEntry = this.entityToEntityMap.entrySet().stream().filter(e -> e.getKey().isInstance(entity)).findAny();
        if (!opEntry.isPresent()) {
            //System.err.println("\tFailed to find entity (" + entity.getType().getId() + ") in map. Using forge Entity");
            return new SForgeEntity(entity);
        }
        Class<? extends LiveEntity> bdclass = opEntry.get().getValue();
        try {
            Constructor<? extends LiveEntity> constructor = bdclass.getConstructor(org.spongepowered.api.entity.Entity.class);
            return constructor.newInstance(entity);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<LiveTileEntity> createTileEntityInstance(org.spongepowered.api.block.entity.BlockEntity tileEntity) {
        Optional<Map.Entry<Class<? extends org.spongepowered.api.block.entity.BlockEntity>, Class<? extends LiveTileEntity>>> opEntry = blockStateToTileEntity.entrySet().stream().filter(e -> e.getKey().isInstance(tileEntity)).findAny();
        if (!opEntry.isPresent()) {
            return Optional.empty();
        }
        Class<? extends LiveTileEntity> bdclass = opEntry.get().getValue();
        try {
            return Optional.of(bdclass.getConstructor(org.spongepowered.api.block.entity.BlockEntity.class).newInstance(tileEntity));
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int[] getMinecraftVersion() {
        return new int[]{1, 16, 5};
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
    public Set<Plugin> getPlugins() {
        return null;
    }

    @Override
    public <E extends CustomEvent> E callEvent(E event) {
        return SpongeListener.call(event);
    }

    @Override
    public Optional<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getEntityType(String id) {
        if (id.equals("minecraft:player")) {
            return Optional.of(new SEntityType.SPlayerType());
        }
        Optional<? extends EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> opEntityType = this.entityTypes.parallelStream().filter(e -> e.getId().equals(id)).findFirst();
        if (opEntityType.isPresent()) {
            return opEntityType.map(e -> e);
        }
        String[] split = id.split(":");
        Optional<RegistryEntry<org.spongepowered.api.entity.EntityType<?>>> opType = RegistryTypes.ENTITY_TYPE.get().findEntry(ResourceKey.of(split[0], split[1]));
        return opType.map(t -> new SEntityType.SForgedEntityType(t.value()));
    }

    @Override
    public Optional<BlockType> getBlockType(String id) {
        String[] splitId = id.split(":");
        Optional<RegistryEntry<org.spongepowered.api.block.BlockType>> opType = RegistryTypes.BLOCK_TYPE.get().findEntry(ResourceKey.of(splitId[0], splitId[1]));
        return opType.map(r -> new SBlockType(r.value()));
    }

    @Override
    public Optional<ItemType> getItemType(String id) {
        return Optional.empty();
    }

    @Override
    @Deprecated
    public Optional<TextColour> getTextColour(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<DyeType> getDyeType(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<PatternLayerType> getPatternLayerType(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<BossColour> getBossColour(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<ParrotType> getParrotType(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<ApplyPhysicsFlag> getApplyPhysics(String id) {
        return Optional.empty();
    }

    @Override
    @Deprecated
    public Optional<UnspecificParser<?>> getUnspecifiedParser(String id) {
        return this.unspecificParsers.stream().filter(f -> f.getId().equals(id)).findFirst();
    }

    @Override
    public Collection<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getEntityTypes() {
        return RegistryTypes
                .ENTITY_TYPE
                .get()
                .stream()
                .map(e -> getEntityType(e.key(RegistryTypes.ENTITY_TYPE).asString()))
                .filter(e -> !e.isPresent())
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<BlockType> getBlockTypes() {
        return RegistryTypes.BLOCK_TYPE.get().stream().map(SBlockType::new).collect(Collectors.toSet());
    }

    @Override
    public Collection<ItemType> getItemTypes() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    @Deprecated
    public Collection<TextColour> getTextColours() {
        return ArrayUtils.convert(STextColour::getInstance, NamedTextColor.NAMES.values());
    }

    @Override
    public Collection<DyeType> getDyeTypes() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Collection<PatternLayerType> getPatternLayerTypes() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Collection<BlockGroup> getBlockGroups() {
        return BlockGroups.values();
    }

    @Override
    public Collection<BossColour> getBossColours() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Collection<ParrotType> getParrotType() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Collection<ApplyPhysicsFlag> getApplyPhysics() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Collection<Permission> getPermissions() {
        return this.permissions;
    }

    @Override
    public @NotNull Permission register(@NotNull String permissionNode) {
        SPermission permission = new SPermission(permissionNode);
        this.permissions.add(permission);
        return permission;
    }

    @Override
    @Deprecated
    public Collection<UnspecificParser<?>> getUnspecifiedParsers() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Collection<TileEntitySnapshot<? extends TileEntity>> getDefaultTileEntities() {
        return this.defaultTileEntities;
    }

    @Override
    public @NotNull Singleton<BossColour> get(BossColours colours) {
        return new Singleton<>(() -> {
            throw new RuntimeException("Not implemented");
        });
    }

    @Override
    @Deprecated
    public <T> UnspecificParser<T> get(UnspecificParsers<T> parsers) {
        return (UnspecificParser<T>) this.getUnspecifiedParser(parsers.getId()).get();
    }

    @Override
    public @NotNull Singleton<ApplyPhysicsFlag> get(ApplyPhysicsFlags flags) {
        switch (flags.getId()) {
            case "none":
                return new Singleton<>(() -> SApplyPhysicsFlag.NONE);
            case "default":
                return new Singleton<>(() -> SApplyPhysicsFlag.DEFAULT);
            default:
                throw new RuntimeException("Unknown Applied Physics Flag of: " + flags.getId());
        }
    }

    @Override
    public @NotNull Singleton<ItemType> get(ItemTypeCommon itemId) {
        Supplier<ItemType> supplier = () -> RegistryTypes.ITEM_TYPE.get()
                .stream()
                .filter(i -> i.key(RegistryTypes.ITEM_TYPE)
                        .asString()
                        .equals(itemId.getId()))
                .findAny()
                .map(SItemType::new)
                .orElseThrow(() -> new IllegalStateException("Unknown item of " + itemId.getId()));
        return new Singleton<>(supplier);
    }

    @Override
    public @NotNull Singleton<ParrotType> get(ParrotTypes parrotID) {
        return new Singleton<>(() -> {
            throw new RuntimeException("Not implemented");
        });
    }

    @Override
    @Deprecated
    public @NotNull TextColour get(TextColours id) {
        return STextColour.getInstance(NamedTextColor.NAMES.value(id.getName()));
    }

    @Override
    public @NotNull Singleton<DyeType> get(DyeTypes id) {
        return new Singleton<>(() -> {
            throw new RuntimeException("Not implemented");
        });
    }

    @Override
    public @NotNull Singleton<PatternLayerType> get(PatternLayerTypes id) {
        return new Singleton<>(() -> {
            throw new RuntimeException("Not implemented");
        });
    }

    @Override
    public <E extends LiveEntity, S extends EntitySnapshot<E>> @NotNull Singleton<EntityType<E, S>> get(EntityTypes<E, S> entityId) {
        return new Singleton<>(() -> {
            throw new RuntimeException("Not implemented");
        });
    }

}

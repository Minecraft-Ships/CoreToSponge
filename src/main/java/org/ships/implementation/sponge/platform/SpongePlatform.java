package org.ships.implementation.sponge.platform;

import net.kyori.adventure.text.format.NamedTextColor;
import org.array.utils.ArrayUtils;
import org.core.CorePlugin;
import org.core.config.ConfigurationFormat;
import org.core.config.parser.unspecific.UnspecificParser;
import org.core.config.parser.unspecific.UnspecificParsers;
import org.core.entity.*;
import org.core.entity.living.animal.parrot.ParrotType;
import org.core.entity.living.animal.parrot.ParrotTypes;
import org.core.event.CustomEvent;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.data.dye.DyeType;
import org.core.inventory.item.data.dye.DyeTypes;
import org.core.inventory.item.type.ItemTypeCommon;
import org.core.platform.Platform;
import org.core.platform.PlatformDetails;
import org.core.platform.Plugin;
import org.core.source.command.CommandSource;
import org.core.text.TextColour;
import org.core.text.TextColours;
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
import org.spongepowered.api.Sponge;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SpongePlatform implements Platform {

    protected Set<? extends EntityType<? extends Entity, ? extends EntitySnapshot<? extends Entity>>> entityTypes = new HashSet<>();
    protected Map<Class<? extends org.spongepowered.api.entity.Entity>, Class<? extends LiveEntity>> entityToEntityMap = new HashMap<>();
    protected Map<Class<? extends org.spongepowered.api.block.entity.BlockEntity>, Class<? extends LiveTileEntity>> blockStateToTileEntity = new HashMap<>();
    protected Collection<TileEntitySnapshot<? extends TileEntity>> defaultTileEntities = new HashSet<>();
    protected Collection<UnspecificParser<? extends Object>> unspecificParsers = new HashSet<>();

    public SpongePlatform(){
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

    public <E extends LiveEntity, S extends EntitySnapshot<E>> Optional<S> createSnapshot(EntityType<E, S> type, SyncExactPosition pos){
        try {
            S snapshot = type.getSnapshotClass().getConstructor(SyncExactPosition.class).newInstance(pos);
            return Optional.of(snapshot);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public LiveEntity createEntityInstance(org.spongepowered.api.entity.Entity entity){
        Optional<Map.Entry<Class<? extends org.spongepowered.api.entity.Entity>, Class<? extends LiveEntity>>> opEntry = this.entityToEntityMap.entrySet().stream().filter(e -> e.getKey().isInstance(entity)).findAny();
        if(!opEntry.isPresent()){
            //System.err.println("\tFailed to find entity (" + entity.getType().getId() + ") in map. Using forge Entity");
            return new SForgeEntity(entity);
        }
        Class<? extends LiveEntity> bdclass = opEntry.get().getValue();
        try{
            Constructor<? extends LiveEntity> constructor = bdclass.getConstructor(org.spongepowered.api.entity.Entity.class);
            return constructor.newInstance(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<LiveTileEntity> createTileEntityInstance(org.spongepowered.api.block.entity.BlockEntity tileEntity){
        Optional<Map.Entry<Class<? extends org.spongepowered.api.block.entity.BlockEntity>, Class<? extends LiveTileEntity>>> opEntry = blockStateToTileEntity.entrySet().stream().filter(e -> e.getKey().isInstance(tileEntity)).findAny();
        if(!opEntry.isPresent()){
            return Optional.empty();
        }
        Class<? extends LiveTileEntity> bdclass = opEntry.get().getValue();
        try {
            return Optional.of(bdclass.getConstructor(org.spongepowered.api.block.entity.BlockEntity.class).newInstance(tileEntity));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int[] getMinecraftVersion() {
        return new int[]{1, 12, 2};
    }

    @Override
    public PlatformDetails getDetails() {
        return null;
    }

    @Override
    public ConfigurationFormat getConfigFormat() {
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
    public Optional<EntityType<? extends Entity, ? extends EntitySnapshot<? extends Entity>>> getEntityType(String id) {
        if(id.equals("minecraft:player")){
            return Optional.of(new SEntityType.SPlayerType());
        }
        Optional<org.spongepowered.api.entity.EntityType> opEntity = org.spongepowered.api.Sponge.getRegistry().getCatalogRegistry().streamAllOf(org.spongepowered.api.entity.EntityType.class).filter(et -> et.getKey().asString().equals(id)).findAny();
        return opEntity.map(SEntityType.SForgedEntityType::new);
    }

    @Override
    public Optional<BlockType> getBlockType(String id) {
        Optional<org.spongepowered.api.block.BlockType> opBlock = org.spongepowered.api.Sponge.getRegistry().getCatalogRegistry().streamAllOf(org.spongepowered.api.block.BlockType.class).filter(b -> b.getKey().asString().equals(id)).findAny();
        return opBlock.map(SBlockType::new);
    }

    @Override
    public Optional<ItemType> getItemType(String id) {
        return Optional.empty();
    }

    @Override
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
    public Optional<UnspecificParser<? extends Object>> getUnspecifiedParser(String id) {
        return this.unspecificParsers.stream().filter(f -> f.getId().equals(id)).findFirst();
    }

    @Override
    public Collection<EntityType<? extends Entity, ? extends EntitySnapshot<? extends Entity>>> getEntityTypes() {
        Set<EntityType<? extends Entity, ? extends EntitySnapshot<? extends Entity>>> set = new HashSet<>();
        org.spongepowered.api.Sponge.getRegistry().getCatalogRegistry().streamAllOf(org.spongepowered.api.entity.EntityType.class).forEach(e -> {
            getEntityType(e.getKey().asString()).ifPresent(set::add);
        });
        return set;
    }

    @Override
    public Collection<BlockType> getBlockTypes() {
        Set<BlockType> blockTypes = new HashSet<>();
        Sponge.getRegistry().getCatalogRegistry().streamAllOf(org.spongepowered.api.block.BlockType.class).forEach(b -> blockTypes.add(new SBlockType(b)));
        return blockTypes;
    }

    @Override
    public Collection<ItemType> getItemTypes() {
        return null;
    }

    @Override
    public Collection<TextColour> getTextColours() {
        return ArrayUtils.convert(STextColour::getInstance, NamedTextColor.NAMES.values());
    }

    @Override
    public Collection<DyeType> getDyeTypes() {
        return null;
    }

    @Override
    public Collection<PatternLayerType> getPatternLayerTypes() {
        return null;
    }

    @Override
    public Collection<BlockGroup> getBlockGroups() {
        return BlockGroups.values();
    }

    @Override
    public Collection<BossColour> getBossColours() {
        return null;
    }

    @Override
    public Collection<ParrotType> getParrotType() {
        return null;
    }

    @Override
    public Collection<ApplyPhysicsFlag> getApplyPhysics() {
        return null;
    }

    @Override
    public Collection<UnspecificParser<? extends Object>> getUnspecifiedParsers() {
        return null;
    }

    @Override
    public Collection<TileEntitySnapshot<? extends TileEntity>> getDefaultTileEntities() {
        return this.defaultTileEntities;
    }

    @Override
    public BossColour get(BossColours colours) {
        return null;
    }

    @Override
    public <T> UnspecificParser<T> get(UnspecificParsers<T> parsers) {
        return (UnspecificParser<T>) this.getUnspecifiedParser(parsers.getId()).get();
    }

    @Override
    public ApplyPhysicsFlag get(ApplyPhysicsFlags flags) {
        switch (flags.getId()){
            case "none": return SApplyPhysicsFlag.NONE;
            case "default": return SApplyPhysicsFlag.DEFAULT;
            default: System.err.println("Unknown Applied Physics Flag of: " + flags.getId());
        }

        return null;
    }

    @Override
    public ItemType get(ItemTypeCommon itemId) {
        Optional<org.spongepowered.api.item.ItemType> opItem = Sponge.getRegistry().getCatalogRegistry().streamAllOf(org.spongepowered.api.item.ItemType.class).filter(i -> i.getKey().asString().equals(itemId.getId())).findAny();
        if(opItem.isPresent()){
            return new SItemType(opItem.get());
        }
        throw new IllegalStateException("Unknown item of " + itemId.getId() + ArrayUtils.toString("", t -> "\n - " + t.getKey().asString(), org.spongepowered.api.Sponge.getRegistry().getCatalogRegistry().getAllOf(org.spongepowered.api.item.ItemType.class)));

    }

    @Override
    public ParrotType get(ParrotTypes parrotID) {
        return null;
    }

    @Override
    public TextColour get(TextColours id) {
        return STextColour.getInstance(NamedTextColor.NAMES.value(id.getName()));
    }

    @Override
    public DyeType get(DyeTypes id) {
        return null;
    }

    @Override
    public PatternLayerType get(PatternLayerTypes id) {
        return null;
    }

    @Override
    public <E extends LiveEntity, S extends EntitySnapshot<E>> EntityType<E, S> get(EntityTypes<E, S> entityId) {
        return null;
    }

}

package org.ships.implementation.sponge.world;

import org.core.CorePlugin;
import org.core.entity.LiveEntity;
import org.core.vector.type.Vector3;
import org.core.world.ChunkExtent;
import org.core.world.WorldExtent;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.async.ASyncExactPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.world.position.asynced.SAsyncedBlockPosition;
import org.ships.implementation.sponge.world.position.asynced.SAsyncedExactPosition;
import org.ships.implementation.sponge.world.position.synced.SBlockPosition;
import org.ships.implementation.sponge.world.position.synced.SExactPosition;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.util.AABB;
import org.spongepowered.api.world.WorldType;
import org.spongepowered.api.world.WorldTypes;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3i;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SWorldExtent implements WorldExtent {

    protected org.spongepowered.api.world.World<?, ?> world;

    public SWorldExtent(org.spongepowered.api.world.World<?, ?> world) {
        this.world = world;
    }

    public org.spongepowered.api.world.World<?, ?> getSpongeWorld() {
        return this.world;
    }

    @Override
    public String getName() {
        if (this.world instanceof ServerWorld) {
            return ((ServerWorld) this.world).key().value();
        }
        WorldType type = this.world.worldType();
        if (type.equals(WorldTypes.OVERWORLD.get())) {
            return "World";
        }
        if (type.equals(WorldTypes.THE_END.get())) {
            return "The_End";
        }
        if (type.equals(WorldTypes.THE_NETHER.get())) {
            return "The_Nether";
        }
        throw new IllegalStateException("Unknown World name of " + this.world.worldType().key(RegistryTypes.WORLD_TYPE).asString() + " | " + this.world.seed());
    }

    @Override
    @Deprecated
    public UUID getUniqueId() {
        if (this.world instanceof ServerWorld) {
            return ((ServerWorld) this.world).uniqueId();
        }
        throw new IllegalStateException("UUID not accessible via client");
    }

    @Override
    public String getPlatformUniqueId() {
        if (this.world instanceof ServerWorld) {
            return ((ServerWorld) this.world).key().asString();
        }
        return getName();
    }

    @Override
    public Set<ChunkExtent> getChunks() {
        return null;
    }

    @Override
    public Optional<ChunkExtent> getChunk(Vector3<Integer> vector) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ChunkExtent loadChunk(Vector3<Integer> vector) {
        return new SLoadedChunkExtent(this.world.loadChunk(vector.getX(), vector.getY(), vector.getZ(), true).orElseThrow(() -> new IllegalStateException("Could not load the chunk")));
    }

    @Override
    public SyncExactPosition getPosition(double x, double y, double z) {
        return new SExactPosition(this.world.location(x, y, z));
    }

    @Override
    public ASyncExactPosition getAsyncPosition(double x, double y, double z) {
        return new SAsyncedExactPosition(this.world.location(x, y, z));
    }

    @Override
    public SyncBlockPosition getPosition(int x, int y, int z) {
        return new SBlockPosition(this.world.location(x, y, z));
    }

    @Override
    public ASyncBlockPosition getAsyncPosition(int x, int y, int z) {
        return new SAsyncedBlockPosition(this.world.location(x, y, z));
    }

    @Override
    public boolean isLoaded() {
        return this.world.isLoaded();
    }

    @Override
    public Set<LiveEntity> getEntities() {
        Set<LiveEntity> set = new HashSet<>();
        SpongePlatform platform = ((SpongePlatform) CorePlugin.getPlatform());


        if (this.world instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) this.world;
            return world.entities().stream().map(platform::createEntityInstance).collect(Collectors.toSet());
        }
        Vector3i min = this.getSpongeWorld().min();
        Vector3i max = this.getSpongeWorld().max();
        AABB aabb = AABB.of(min, max);
        return this
                .world
                .entities(aabb)
                .stream()
                .map(e -> ((SpongePlatform) CorePlugin.getPlatform()).createEntityInstance(e))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<LiveTileEntity> getTileEntities() {
        return this
                .world
                .blockEntities()
                .stream()
                .map(te -> ((SpongePlatform) CorePlugin.getPlatform()).createTileEntityInstance(te))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WorldExtent)) {
            return false;
        }
        WorldExtent extent = (WorldExtent) object;
        return extent.getPlatformUniqueId().equals(this.getPlatformUniqueId());
    }
}

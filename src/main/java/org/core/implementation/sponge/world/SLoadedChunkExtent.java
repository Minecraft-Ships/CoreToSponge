package org.core.implementation.sponge.world;

import org.core.TranslateCore;
import org.core.entity.LiveEntity;
import org.core.implementation.sponge.platform.SpongePlatform;
import org.core.implementation.sponge.world.position.synced.SBlockPosition;
import org.core.implementation.sponge.world.position.synced.SExactPosition;
import org.core.world.ChunkExtent;
import org.core.world.WorldExtent;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.async.ASyncExactPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.util.AABB;
import org.spongepowered.api.world.chunk.WorldChunk;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SLoadedChunkExtent implements ChunkExtent {

    private final @NotNull WorldChunk chunk;

    public SLoadedChunkExtent(@NotNull WorldChunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public WorldExtent getWorld() {
        return new SWorldExtent(this.chunk.world());
    }

    @Override
    public SyncExactPosition getPosition(double x, double y, double z) {
        return new SExactPosition(this.chunk.world().location(this.chunk.min().add(x, y, z)));
    }

    @Override
    public ASyncExactPosition getAsyncPosition(double x, double y, double z) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public SyncBlockPosition getPosition(int x, int y, int z) {
        return new SBlockPosition(this.chunk.world().location(this.chunk.min().add(x, y, z)));
    }

    @Override
    public ASyncBlockPosition getAsyncPosition(int x, int y, int z) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public Set<LiveEntity> getEntities() {
        SpongePlatform platform = ((SpongePlatform) TranslateCore.getPlatform());
        return this.chunk
                .entities(AABB.of(this.chunk.min(), this.chunk.max()))
                .stream()
                .map(platform::createEntityInstance)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<LiveTileEntity> getTileEntities() {
        SpongePlatform platform = ((SpongePlatform) TranslateCore.getPlatform());
        return this.chunk
                .blockEntities()
                .stream()
                .map(platform::createTileEntityInstance)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }
}

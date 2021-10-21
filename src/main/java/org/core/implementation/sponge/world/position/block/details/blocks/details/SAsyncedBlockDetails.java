package org.core.implementation.sponge.world.position.block.details.blocks.details;

import org.core.implementation.sponge.world.position.synced.SSyncedPosition;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.implementation.sponge.world.position.block.details.blocks.snapshot.SBlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.server.ServerWorld;

public class SAsyncedBlockDetails extends SBlockDetails {

    public SAsyncedBlockDetails(BlockState state) {
        super(state);
    }

    @Override
    public BlockSnapshot.AsyncBlockSnapshot createSnapshot(ASyncBlockPosition position) {
        return new SBlockSnapshot.SAsyncedBlockSnapshot(t(((SSyncedPosition<Integer>) position).getSpongeLocation()));
    }

    @Override
    public BlockSnapshot.SyncBlockSnapshot createSnapshot(SyncBlockPosition position) {
        return new SBlockSnapshot.SSyncedBlockSnapshot(t(((SSyncedPosition<Integer>) position).getSpongeLocation()));
    }

    @Override
    public BlockDetails createCopyOf() {
        return new SAsyncedBlockDetails(this.blockstate.copy());
    }

    private org.spongepowered.api.block.BlockSnapshot t(org.spongepowered.api.world.Location<? extends World<?, ?>, ?> loc) {
        if (loc.world() instanceof ServerWorld) {
            return org.spongepowered.api.block.BlockSnapshot.builder()
                    .blockState(this.blockstate)
                    .world(((ServerWorld) loc.world()).properties())
                    .position(loc.blockPosition())
                    .build();
        }
        return org.spongepowered.api.block.BlockSnapshot.builder()
                .blockState(this.blockstate)
                .position(loc.blockPosition())
                .build();
    }
}

package org.core.implementation.sponge.world.position.asynced;

import org.core.entity.living.human.player.LivePlayer;
import org.core.implementation.sponge.world.position.SPosition;
import org.core.implementation.sponge.world.position.synced.SBlockPosition;
import org.core.implementation.sponge.world.position.synced.SSyncedPosition;
import org.core.platform.plugin.Plugin;
import org.core.threadsafe.FutureResult;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.flags.PositionFlag;
import org.core.world.position.impl.async.ASyncPosition;
import org.core.world.position.impl.sync.SyncPosition;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.plugin.PluginContainer;

import java.util.function.Function;

public abstract class SAsyncedPosition<N extends Number> extends SPosition<N> implements ASyncPosition<N> {

    public SAsyncedPosition(Location<? extends World<?, ?>, ?> location,
            Function<? extends Location<? extends World<?, ?>, ?>, ? extends SPosition<N>> newInstance) {
        super(location, newInstance);
    }

    protected abstract SSyncedPosition<N> toSynced();

    @Override
    public FutureResult<SyncPosition<N>> scheduleBlock(Plugin plugin, BlockDetails details,
            PositionFlag.SetFlag... flags) {
        FutureResult<SyncPosition<N>> fr = new FutureResult<>();
        Task task = Task
                .builder()
                .execute(() ->
                        fr.run(this.toSynced().setBlock(details, flags)))
                .delay(Ticks.of(0))
                .plugin((PluginContainer) plugin.getPlatformLauncher())
                .build();
        Sponge.server().scheduler().submit(task);
        return fr;
    }

    @Override
    public FutureResult<SyncPosition<N>> scheduleBlock(Plugin plugin, BlockDetails details, LivePlayer... player) {
        FutureResult<SyncPosition<N>> fr = new FutureResult<>();
        Task task = Task
                .builder()
                .execute(() ->
                        fr.run(this.toSynced().setBlock(details, player)))
                .delay(Ticks.of(0))
                .plugin((PluginContainer) plugin.getPlatformLauncher())
                .build();
        Sponge.server().scheduler().submit(task);
        return fr;
    }

    @Override
    public FutureResult<SyncPosition<N>> scheduleReset(Plugin plugin, LivePlayer... player) {
        FutureResult<SyncPosition<N>> fr = new FutureResult<>();
        Task task = Task
                .builder()
                .execute(() ->
                        fr.run(this.toSynced().resetBlock(player)))
                .delay(Ticks.of(0))
                .plugin((PluginContainer) plugin.getPlatformLauncher())
                .build();
        Sponge.server().scheduler().submit(task);
        return fr;
    }

    @Override
    public FutureResult<LiveTileEntity> getTileEntity(Plugin plugin) {
        FutureResult<LiveTileEntity> fr = new FutureResult<>();
        Task task = Task
                .builder()
                .execute(() ->
                        new SBlockPosition(this.location)
                                .getTileEntity()
                                .ifPresent(fr::run))
                .delay(Ticks.of(0))
                .plugin((PluginContainer) plugin.getPlatformLauncher())
                .build();
        Sponge.server().scheduler().submit(task);
        return fr;
    }
}

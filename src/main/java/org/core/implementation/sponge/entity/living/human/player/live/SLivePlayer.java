package org.core.implementation.sponge.entity.living.human.player.live;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.core.TranslateCore;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.implementation.sponge.entity.SEntityType;
import org.core.implementation.sponge.entity.SLiveEntity;
import org.core.implementation.sponge.entity.living.human.player.snapshot.SPlayerSnapshot;
import org.core.implementation.sponge.inventory.inventories.live.SLivePlayerInventory;
import org.core.implementation.sponge.platform.SpongePlatformServer;
import org.core.implementation.sponge.world.position.block.details.blocks.details.SBlockDetails;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.core.permission.Permission;
import org.core.vector.type.Vector3;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.impl.BlockPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Server;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.permission.Subject;

import java.util.*;
import java.util.concurrent.LinkedTransferQueue;

public class SLivePlayer extends SLiveEntity implements LivePlayer, ForwardingAudience {

    //private final LinkedTransferQueue<BossBar> bossBars = new LinkedTransferQueue<>();

    private static final Map<UUID, Collection<BossBar>> BARS = new LinkedHashMap<>();

    @Deprecated
    public SLivePlayer(org.spongepowered.api.entity.Entity entity) {
        this((org.spongepowered.api.entity.living.player.Player) entity);
    }

    public SLivePlayer(org.spongepowered.api.entity.living.player.Player entity) {
        super(entity);
    }

    @Override
    public org.spongepowered.api.entity.living.player.Player getSpongeEntity() {
        return (org.spongepowered.api.entity.living.player.Player) super.getSpongeEntity();
    }

    @Override
    public boolean isOnGround() {
        return false;
    }

    @Override
    public boolean isViewingInventory() {
        Player entity = this.getSpongeEntity();
        if (entity instanceof LivePlayer) {
            return ((org.core.entity.living.human.player.Player<LiveEntity>) entity).isViewingInventory();
        }
        return false;
    }

    @Override
    public PlayerInventory getInventory() {
        return new SLivePlayerInventory(this);
    }

    @Override
    public int getFoodLevel() {
        return this.getSpongeEntity().get(Keys.FOOD_LEVEL).get();
    }

    @Override
    public double getExhaustionLevel() {
        return this.getSpongeEntity().get(Keys.EXHAUSTION).get();
    }

    @Override
    public SLivePlayer setExhaustionLevel(double value) throws IndexOutOfBoundsException {
        this.getSpongeEntity().offer(Keys.EXHAUSTION, value);
        return this;
    }

    @Override
    public double getSaturationLevel() {
        return this.getSpongeEntity().get(Keys.SATURATION).get();
    }

    @Override
    public SLivePlayer setSaturationLevel(double value) throws IndexOutOfBoundsException {
        this.getSpongeEntity().offer(Keys.SATURATION, value);
        return this;
    }

    @Override
    public String getName() {
        return this.getSpongeEntity().name();
    }

    @Override
    public boolean isSneaking() {
        return this.getSpongeEntity().get(Keys.IS_SNEAKING).get();
    }

    @Override
    public SLivePlayer setSneaking(boolean sneaking) {
        this.getSpongeEntity().offer(Keys.IS_SNEAKING, sneaking);
        return this;
    }

    @Override
    public SLivePlayer setFood(int value) throws IndexOutOfBoundsException {
        this.getSpongeEntity().offer(Keys.FOOD_LEVEL, value);
        return this;
    }

    @Override
    public UUID getUniqueId() {
        return this.getSpongeEntity().uniqueId();
    }

    @Override
    public PlayerSnapshot createSnapshot() {
        return new SPlayerSnapshot(this);
    }

    @Override
    public EntityType<LivePlayer, PlayerSnapshot> getType() {
        return new SEntityType.SPlayerType();
    }

    @Override
    public Optional<BlockPosition> getBlockLookingAt(int scanLength) {
        return Optional.empty();
    }

    @Override
    public void setBlock(@NotNull BlockDetails details, @NotNull Vector3<Integer> position) {
        BlockState state = ((SBlockDetails) details).getState();
        this.getSpongeEntity().sendBlockChange(position.getX(), position.getY(), position.getZ(), state);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        if (!(this.entity instanceof ServerPlayer)) {
            return true;
        }
        return ((Subject) this.getSpongeEntity()).hasPermission(permission.getPermissionValue());
    }

    @Override
    public @NotNull Collection<? extends BossBar> bossBars() {
        return Collections.unmodifiableCollection(BARS.getOrDefault(this.getUniqueId(), Collections.emptyList()));
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        this.getSpongeEntity().sendMessage(message);
    }

    @Override
    public void showBossBar(@NotNull BossBar bar) {
        Collection<BossBar> bars = BARS.getOrDefault(this.getUniqueId(), new LinkedTransferQueue<>());
        bars.add(bar);
        BARS.putIfAbsent(this.getUniqueId(), bars);
        ForwardingAudience.super.showBossBar(bar);
    }

    @Override
    public void hideBossBar(@NotNull BossBar bar) {
        if (BARS.containsKey(this.getUniqueId())) {
            Collection<BossBar> bars = BARS.get(this.getUniqueId());
            bars.remove(bar);
        }
        ForwardingAudience.super.hideBossBar(bar);
    }

    @Override
    public void sendMessage(@NotNull Component message, @Nullable UUID uuid) {
        this.getSpongeEntity().sendMessage(uuid == null ? Identity.nil() : Identity.identity(uuid), message);
    }

    @Override
    public boolean sudo(String wholeCommand) {
        CommandResult result;
        Server server = ((SpongePlatformServer) TranslateCore.getServer()).getServer();
        try {
            result = server
                    .commandManager()
                    .process((Subject) this.getSpongeEntity(), server.broadcastAudience(), wholeCommand);
        } catch (CommandException e) {
            throw new IllegalStateException(e);
        }
        return result.isSuccess();
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return Collections.singleton(this.getSpongeEntity());
    }
}

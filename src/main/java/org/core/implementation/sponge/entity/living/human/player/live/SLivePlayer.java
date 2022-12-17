package org.core.implementation.sponge.entity.living.human.player.live;

import net.kyori.adventure.identity.Identity;
import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.entity.EntityType;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.implementation.sponge.entity.SEntityType;
import org.core.implementation.sponge.entity.SLiveEntity;
import org.core.implementation.sponge.entity.living.human.player.snapshot.SPlayerSnapshot;
import org.core.implementation.sponge.platform.SpongePlatformServer;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.core.permission.Permission;
import org.core.source.viewer.CommandViewer;
import org.core.world.position.impl.BlockPosition;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.ServiceRegistration;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.permission.Subject;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class SLivePlayer extends SLiveEntity implements LivePlayer {

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
    public boolean isViewingInventory() {
        return false;
        /*TODO return getSpongeEntity().isViewingInventory();*/
    }

    @Override
    public PlayerInventory getInventory() {
        throw new RuntimeException("Not implemented yet");
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
    public UUID getUniqueId() {
        return this.getSpongeEntity().uniqueId();
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
    public EntityType<LivePlayer, PlayerSnapshot> getType() {
        return new SEntityType.SPlayerType();
    }

    @Override
    public boolean hasPermission(Permission permission) {
        if (!(this.entity instanceof ServerPlayer)) {
            return true;
        }
        return ((Subject) this.getSpongeEntity()).hasPermission(permission.getPermissionValue());
    }

    @Override
    public Optional<BlockPosition> getBlockLookingAt(int scanLength) {
        return Optional.empty();
    }

    @Override
    public PlayerSnapshot createSnapshot() {
        return new SPlayerSnapshot(this);
    }

    @Override
    public boolean isOnGround() {
        return false;
    }

    @Override
    public CommandViewer sendMessage(AText message, UUID uuid) {
        this.getSpongeEntity().sendMessage(Identity.identity(uuid), ((AdventureText) message).getComponent());
        return this;
    }

    @Override
    public CommandViewer sendMessage(AText message) {
        this.getSpongeEntity().sendMessage(((AdventureText) message).getComponent());
        return this;
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
    public BigDecimal getBalance() {
        Optional<UniqueAccount> opAccount = this.getAccount();
        if (!opAccount.isPresent()) {
            return new BigDecimal(0);
        }
        return opAccount
                .get()
                .balance(Sponge.serviceProvider().registration(EconomyService.class).get().service().defaultCurrency());
    }

    @Override
    public void setBalance(BigDecimal decimal) {
        Optional<UniqueAccount> opAccount = this.getAccount();
        if (!opAccount.isPresent()) {
            return;
        }
        opAccount
                .get()
                .setBalance(
                        Sponge.serviceProvider().registration(EconomyService.class).get().service().defaultCurrency(),
                        decimal);
    }

    private Optional<UniqueAccount> getAccount() {
        Optional<ServiceRegistration<EconomyService>> opReg = Sponge
                .serviceProvider()
                .registration(EconomyService.class);
        return opReg.flatMap(economyServiceProviderRegistration -> economyServiceProviderRegistration
                .service()
                .findOrCreateAccount(this.getUniqueId()));
    }
}

package org.ships.implementation.sponge.entity.living.human.player.live;

import net.kyori.adventure.identity.Identity;
import org.core.CorePlugin;
import org.core.entity.EntityType;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.core.source.viewer.CommandViewer;
import org.core.text.Text;
import org.core.world.position.impl.BlockPosition;
import org.ships.implementation.sponge.entity.SEntityType;
import org.ships.implementation.sponge.entity.SLiveEntity;
import org.ships.implementation.sponge.entity.living.human.player.snapshot.SPlayerSnapshot;
import org.ships.implementation.sponge.platform.SpongePlatformServer;
import org.ships.implementation.sponge.text.SText;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.ServiceRegistration;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

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
        return null;
    }

    @Override
    public int getFoodLevel() {
        return getSpongeEntity().get(Keys.FOOD_LEVEL).get();
    }

    @Override
    public double getExhaustionLevel() {
        return getSpongeEntity().get(Keys.EXHAUSTION).get();
    }

    @Override
    public double getSaturationLevel() {
        return getSpongeEntity().get(Keys.SATURATION).get();
    }

    @Override
    public String getName() {
        return getSpongeEntity().name();
    }

    @Override
    public UUID getUniqueId() {
        return getSpongeEntity().uniqueId();
    }

    @Override
    public boolean isSneaking() {
        return getSpongeEntity().get(Keys.IS_SNEAKING).get();
    }

    @Override
    public SLivePlayer setFood(int value) throws IndexOutOfBoundsException {
        getSpongeEntity().offer(Keys.FOOD_LEVEL, value);
        return this;
    }

    @Override
    public SLivePlayer setExhaustionLevel(double value) throws IndexOutOfBoundsException {
        getSpongeEntity().offer(Keys.EXHAUSTION, value);
        return this;
    }

    @Override
    public SLivePlayer setSaturationLevel(double value) throws IndexOutOfBoundsException {
        getSpongeEntity().offer(Keys.SATURATION, value);
        return this;
    }

    @Override
    public SLivePlayer setSneaking(boolean sneaking) {
        this.getSpongeEntity().offer(Keys.IS_SNEAKING, sneaking);
        return this;
    }

    @Override
    public EntityType<LivePlayer, PlayerSnapshot> getType() {
        return new SEntityType.SPlayerType();
    }

    @Override
    public boolean hasPermission(String permission) {
        if (!(this.entity instanceof ServerPlayer)) {
            return true;
        }
        return ((ServerPlayer) getSpongeEntity()).hasPermission(permission);
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
    @Deprecated
    public CommandViewer sendMessage(Text message, UUID uuid) {
        if (uuid == null) {
            return sendMessage(message);
        }
        this.getSpongeEntity().sendMessage(Identity.identity(uuid), ((SText<?>) message).toSponge());
        return this;
    }

    @Override
    @Deprecated
    public CommandViewer sendMessage(org.core.text.Text message) {
        getSpongeEntity().sendMessage(((SText<?>) message).toSponge());
        return this;
    }

    @Override
    @Deprecated
    public CommandViewer sendMessagePlain(String message) {
        /*TODO getSpongeEntity().sendMessage(Text.of(TextSerializers.FORMATTING_CODE.stripCodes(message)));*/
        return this;
    }

    @Override
    public boolean sudo(String wholeCommand) {
        CommandResult result;
        Server server = ((SpongePlatformServer) CorePlugin.getServer()).getServer();
        try {
            result = server.commandManager().process((ServerPlayer) this.getSpongeEntity(), server.broadcastAudience(), wholeCommand);
        } catch (CommandException e) {
            throw new IllegalStateException(e);
        }
        return result.isSuccess();
    }

    @Override
    public BigDecimal getBalance() {
        Optional<UniqueAccount> opAccount = getAccount();
        if (!opAccount.isPresent()) {
            return new BigDecimal(0);
        }
        return opAccount.get().balance(Sponge.serviceProvider().registration(EconomyService.class).get().service().defaultCurrency());
    }

    @Override
    public void setBalance(BigDecimal decimal) {
        Optional<UniqueAccount> opAccount = getAccount();
        if (!opAccount.isPresent()) {
            return;
        }
        opAccount.get()
                .setBalance(Sponge
                                .serviceProvider()
                                .registration(EconomyService.class)
                                .get()
                                .service()
                                .defaultCurrency(),
                        decimal);
    }

    private Optional<UniqueAccount> getAccount() {
        Optional<ServiceRegistration<EconomyService>> opReg = Sponge.serviceProvider().registration(EconomyService.class);
        return opReg.flatMap(economyServiceProviderRegistration -> economyServiceProviderRegistration.service().findOrCreateAccount(this.getUniqueId()));
    }
}

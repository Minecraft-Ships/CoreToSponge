package org.ships.implementation.sponge.entity.living.human.player.live;

import org.core.entity.EntityType;
import org.core.entity.living.human.AbstractHuman;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.core.source.viewer.CommandViewer;
import org.ships.implementation.sponge.entity.SEntityType;
import org.ships.implementation.sponge.entity.SLiveEntity;
import org.ships.implementation.sponge.entity.living.human.player.snapshot.SPlayerSnapshot;
import org.ships.implementation.sponge.text.SText;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class SLivePlayer extends SLiveEntity implements LivePlayer {

    @Deprecated
    public SLivePlayer(org.spongepowered.api.entity.Entity entity){
        this((org.spongepowered.api.entity.living.player.Player) entity);
    }

    public SLivePlayer(org.spongepowered.api.entity.living.player.Player entity) {
        super(entity);
    }

    @Override
    public org.spongepowered.api.entity.living.player.Player getSpongeEntity(){
        return (org.spongepowered.api.entity.living.player.Player)super.getSpongeEntity();
    }

    @Override
    public boolean isViewingInventory() {
        return getSpongeEntity().isViewingInventory();
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
        return getSpongeEntity().getName();
    }

    @Override
    public UUID getUniqueId() {
        return getSpongeEntity().getUniqueId();
    }

    @Override
    public boolean isSneaking() {
        return getSpongeEntity().get(Keys.IS_SNEAKING).get();
    }

    @Override
    public AbstractHuman setFood(int value) throws IndexOutOfBoundsException {
        getSpongeEntity().offer(Keys.FOOD_LEVEL, value);
        return this;
    }

    @Override
    public AbstractHuman setExhaustionLevel(double value) throws IndexOutOfBoundsException {
        getSpongeEntity().offer(Keys.EXHAUSTION, value);
        return this;
    }

    @Override
    public AbstractHuman setSaturationLevel(double value) throws IndexOutOfBoundsException {
        getSpongeEntity().offer(Keys.SATURATION, value);
        return this;
    }

    @Override
    public AbstractHuman setSneaking(boolean sneaking) {
        this.getSpongeEntity().offer(Keys.IS_SNEAKING, sneaking);
        return this;
    }

    @Override
    public EntityType<LivePlayer, PlayerSnapshot> getType() {
        return new SEntityType.SPlayerType();
    }

    @Override
    public boolean hasPermission(String permission) {
        return getSpongeEntity().hasPermission(permission);
    }

    @Override
    public PlayerSnapshot createSnapshot() {
        return new SPlayerSnapshot(this);
    }

    @Override
    public CommandViewer sendMessage(org.core.text.Text message) {
        getSpongeEntity().sendMessage(((SText)message).toSponge());
        return this;
    }

    @Override
    public CommandViewer sendMessagePlain(String message) {
        getSpongeEntity().sendMessage(Text.of(TextSerializers.FORMATTING_CODE.stripCodes(message)));
        return this;
    }

    @Override
    public boolean sudo(String wholeCommand) {
        CommandResult result = Sponge.getCommandManager().process(getSpongeEntity(), wholeCommand);
        return result.getSuccessCount().isPresent();
    }

    @Override
    public BigDecimal getBalance() {
        Optional<UniqueAccount> opAccount = getAccount();
        if (!opAccount.isPresent()){
            return new BigDecimal(0);
        }
        return opAccount.get().getBalance(Sponge.getServiceManager().getRegistration(EconomyService.class).get().getProvider().getDefaultCurrency());
    }

    @Override
    public void setBalance(BigDecimal decimal) {
        Optional<UniqueAccount> opAccount = getAccount();
        if (!opAccount.isPresent()){
            return;
        }
        opAccount.get()
                .setBalance(Sponge
                        .getServiceManager()
                        .getRegistration(EconomyService.class)
                        .get()
                        .getProvider()
                        .getDefaultCurrency(),
                        decimal,
                        Cause
                                .builder()
                                .build(EventContext
                                        .builder()
                                        .add(EventContextKeys.PLAYER, this.getSpongeEntity())
                                        .build()));
    }

    private Optional<UniqueAccount> getAccount(){
        Optional<ProviderRegistration<EconomyService>> opReg = Sponge.getServiceManager().getRegistration(EconomyService.class);
        return opReg.flatMap(economyServiceProviderRegistration -> economyServiceProviderRegistration.getProvider().getOrCreateAccount(this.getUniqueId()));
    }
}

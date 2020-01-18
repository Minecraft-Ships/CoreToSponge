package org.ships.implementation.sponge.entity.living.human.player.snapshot;

import org.core.CorePlugin;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.ships.implementation.sponge.entity.SEntitySnapshot;
import org.ships.implementation.sponge.entity.SEntityType;
import org.ships.implementation.sponge.entity.living.human.player.live.SUser;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class SPlayerSnapshot extends SEntitySnapshot<LivePlayer> implements PlayerSnapshot {

    protected int foodLevel;
    protected double exhaustionLevel;
    protected double saturationLevel;
    protected String name;
    protected boolean isSneaking;


    public SPlayerSnapshot(PlayerSnapshot snapshot) {
        super(snapshot);
    }

    public SPlayerSnapshot(LivePlayer entity) {
        super(entity);
    }

    @Override
    public boolean isViewingInventory() {
        return false;
    }

    @Override
    public SEntityType.SPlayerType getType() {
        return new SEntityType.SPlayerType();
    }

    @Override
    public PlayerInventory getInventory() {
        return null;
    }

    @Override
    public PlayerSnapshot createSnapshot() {
        return new SPlayerSnapshot(this);
    }

    @Override
    public int getFoodLevel() {
        return this.foodLevel;
    }

    @Override
    public double getExhaustionLevel() {
        return this.exhaustionLevel;
    }

    @Override
    public double getSaturationLevel() {
        return this.saturationLevel;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUniqueId() {
        return getCreatedFrom().get().getUniqueId();
    }

    @Override
    public boolean isSneaking() {
        return this.isSneaking;
    }

    @Override
    public PlayerSnapshot setFood(int value) throws IndexOutOfBoundsException {
        this.foodLevel = value;
        return this;
    }

    @Override
    public PlayerSnapshot setExhaustionLevel(double value) throws IndexOutOfBoundsException {
        this.exhaustionLevel = value;
        return this;
    }

    @Override
    public PlayerSnapshot setSaturationLevel(double value) throws IndexOutOfBoundsException {
        this.saturationLevel = value;
        return this;
    }

    @Override
    public PlayerSnapshot setSneaking(boolean sneaking) {
        this.isSneaking = sneaking;
        return this;
    }

    @Override
    public LivePlayer spawnEntity() {
        applyDefault(this.createdFrom);
        this.createdFrom.setExhaustionLevel(this.exhaustionLevel);
        this.createdFrom.setFood(this.foodLevel);
        this.createdFrom.setSaturationLevel(this.saturationLevel);
        this.createdFrom.setSneaking(this.isSneaking);
        return this.createdFrom;
    }

    @Override
    public BigDecimal getBalance() {
        Optional<UniqueAccount> opAccount = getAccount();
        return opAccount.map(uniqueAccount -> uniqueAccount.getBalance(Sponge.getServiceManager().getRegistration(EconomyService.class).get().getProvider().getDefaultCurrency())).orElseGet(() -> new BigDecimal(0));
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
                                        .add(EventContextKeys.OWNER, ((SUser)CorePlugin.getServer().getOfflineUser(this.getUniqueId()).get()).getUser())
                                        .build()));
    }

    private Optional<UniqueAccount> getAccount(){
        Optional<ProviderRegistration<EconomyService>> opReg = Sponge.getServiceManager().getRegistration(EconomyService.class);
        return opReg.flatMap(economyServiceProviderRegistration -> economyServiceProviderRegistration.getProvider().getOrCreateAccount(this.getUniqueId()));
    }
}

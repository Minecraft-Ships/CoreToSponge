package org.ships.implementation.sponge.entity.living.human.player.live;

import org.core.entity.living.human.player.User;
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

public class SUser implements User {

    protected org.spongepowered.api.entity.living.player.User user;

    public SUser(org.spongepowered.api.entity.living.player.User user){
        this.user = user;
    }

    public org.spongepowered.api.entity.living.player.User getUser(){
        return this.user;
    }

    @Override
    public String getName() {
        return this.user.getName();
    }

    @Override
    public UUID getUniqueId() {
        return this.user.getUniqueId();
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
                                        .add(EventContextKeys.OWNER, this.user)
                                        .build()));
    }

    private Optional<UniqueAccount> getAccount(){
        Optional<ProviderRegistration<EconomyService>> opReg = Sponge.getServiceManager().getRegistration(EconomyService.class);
        return opReg.flatMap(economyServiceProviderRegistration -> economyServiceProviderRegistration.getProvider().getOrCreateAccount(this.getUniqueId()));
    }
}

package org.core.implementation.sponge.currency.account;

import org.core.eco.Currency;
import org.core.entity.living.human.player.User;
import org.core.implementation.sponge.currency.SCurrency;
import org.core.source.eco.EcoSource;
import org.core.source.eco.PlayerAccount;
import org.core.utils.ComponentUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.math.BigDecimal;
import java.util.UUID;

public class SEcoUniqueAccount implements PlayerAccount {

    private final UniqueAccount account;

    public SEcoUniqueAccount(UniqueAccount uniqueAccount) {
        this.account = uniqueAccount;
    }

    @Override
    public BigDecimal getBalance(@NotNull Currency currency) {
        if (!(currency instanceof SCurrency cur)) {
            throw new RuntimeException("Unknown type of currency: " + currency.getClass().getName());
        }
        return account.balance(cur.getSponge());
    }

    @Override
    public void setBalance(@NotNull Currency currency, @NotNull BigDecimal decimal) {
        if (!(currency instanceof SCurrency cur)) {
            throw new RuntimeException("Unknown type of currency: " + currency.getClass().getName());
        }
        account.setBalance(cur.getSponge(), decimal);
    }

    @Override
    public @NotNull UUID getId() {
        return this.account.uniqueId();
    }

    @Override
    public @NotNull String getName() {
        return ComponentUtils.toPlain(this.account.displayName());
    }
}

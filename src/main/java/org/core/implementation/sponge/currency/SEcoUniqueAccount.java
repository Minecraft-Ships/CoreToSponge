package org.core.implementation.sponge.currency;

import org.core.eco.Currency;
import org.core.source.eco.EcoSource;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.math.BigDecimal;

public class SEcoUniqueAccount implements EcoSource {

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
}

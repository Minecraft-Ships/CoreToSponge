package org.core.implementation.sponge.currency.account;

import org.core.eco.Currency;
import org.core.implementation.sponge.currency.SCurrency;
import org.core.source.eco.Account;
import org.core.source.eco.PlayerAccount;
import org.core.utils.ComponentUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.math.BigDecimal;
import java.util.UUID;

public class SEcoAccount<SAccount extends org.spongepowered.api.service.economy.account.Account> implements Account {

    private final SAccount account;

    public SEcoAccount(SAccount account) {
        this.account = account;
    }

    public SAccount getAccount() {
        return this.account;
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
    public @NotNull String getName() {
        return ComponentUtils.toPlain(this.account.displayName());
    }
}

package org.core.implementation.sponge.currency.account;

import org.core.eco.account.PlayerAccount;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.util.UUID;

public class SEcoUniqueAccount extends SEcoAccount<UniqueAccount> implements PlayerAccount {

    public SEcoUniqueAccount(UniqueAccount account) {
        super(account);
    }

    @Override
    public @NotNull UUID getId() {
        return this.getAccount().uniqueId();
    }
}

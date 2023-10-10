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

public class SEcoUniqueAccount extends SEcoAccount<UniqueAccount> implements PlayerAccount {

    public SEcoUniqueAccount(UniqueAccount account) {
        super(account);
    }

    @Override
    public @NotNull UUID getId() {
        return this.getAccount().uniqueId();
    }
}

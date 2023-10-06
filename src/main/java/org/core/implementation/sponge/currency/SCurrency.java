package org.core.implementation.sponge.currency;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;

public class SCurrency implements org.core.eco.Currency {

    private final Currency spongeCurrency;

    public SCurrency(@NotNull Currency currency) {
        this.spongeCurrency = currency;
    }

    public Currency getSponge() {
        return this.spongeCurrency;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return this.spongeCurrency.displayName();
    }

    @Override
    public boolean isDefault() {
        return this.spongeCurrency.isDefault();
    }

    @Override
    public @NotNull Component getSymbol() {
        return this.spongeCurrency.symbol();
    }

    @Override
    public Component asDisplay(@NotNull BigDecimal amount) {
        return this.spongeCurrency.format(amount);
    }
}

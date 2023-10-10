package org.core.implementation.sponge.currency;

import org.core.TranslateCore;
import org.core.eco.Currency;
import org.core.eco.CurrencyManager;
import org.core.entity.living.human.player.User;
import org.core.implementation.sponge.currency.account.SEcoNamedAccount;
import org.core.implementation.sponge.currency.account.SEcoUniqueAccount;
import org.core.source.eco.NamedAccount;
import org.core.source.eco.PlayerAccount;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.registry.Registry;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.VirtualAccount;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SCurrencyManager implements CurrencyManager {
    @Override
    public boolean areCurrenciesSupported() {
        return true;
    }

    @Override
    public boolean isEconomyEnabled() {
        return getService().isPresent();
    }

    @Override
    public @NotNull Currency getDefaultCurrency() {
        org.spongepowered.api.service.economy.Currency currency = getService()
                .orElseThrow(() -> new IllegalStateException("Economy not enabled"))
                .defaultCurrency();
        return new SCurrency(currency);
    }

    @Override
    public @NotNull Collection<Currency> getCurrencies() {
        return RegistryTypes.CURRENCY
                .find()
                .stream()
                .flatMap(Registry::stream)
                .map(SCurrency::new)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull CompletableFuture<Optional<NamedAccount>> getSourceFor(@NotNull String accountName) {
        if (!this.isEconomyEnabled()) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        return CompletableFuture.completedFuture(this
                                                         .getService()
                                                         .flatMap(service -> service.findOrCreateAccount(accountName))
                                                         .map(a -> new SEcoNamedAccount((VirtualAccount) a)));
    }

    @Override
    public @NotNull CompletableFuture<Optional<PlayerAccount>> getSourceFor(@NotNull UUID uuid) {
        if (!this.isEconomyEnabled()) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        return CompletableFuture.completedFuture(
                this.getService().flatMap(service -> service.findOrCreateAccount(uuid)).map(SEcoUniqueAccount::new));
    }

    private Optional<EconomyService> getService() {
        return Sponge.server().serviceProvider().economyService();
    }
}

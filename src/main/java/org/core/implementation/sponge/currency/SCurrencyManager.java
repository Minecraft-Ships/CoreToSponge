package org.core.implementation.sponge.currency;

import org.core.TranslateCore;
import org.core.eco.Currency;
import org.core.eco.CurrencyManager;
import org.core.source.eco.EcoSource;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.registry.Registry;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.service.economy.EconomyService;

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
    public CompletableFuture<Optional<EcoSource>> getSourceFor(@NotNull UUID uuid) {
        if (!this.isEconomyEnabled()) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
        var isUser = Sponge.server().gameProfileManager().cache().findById(uuid).isPresent();
        if (isUser) {
            return TranslateCore.getServer().getOfflineUser(uuid).thenApply(op -> op.map(t -> t));
        }
        return CompletableFuture.completedFuture(
                this.getService().flatMap(service -> service.findOrCreateAccount(uuid)).map(SEcoUniqueAccount::new));
    }

    private Optional<EconomyService> getService() {
        return Sponge.server().serviceProvider().economyService();
    }
}

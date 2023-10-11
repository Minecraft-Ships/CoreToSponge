package org.core.implementation.sponge.currency.account;

import org.core.eco.Currency;
import org.core.eco.account.Account;
import org.core.eco.transaction.Transaction;
import org.core.eco.transaction.pending.PendingTransaction;
import org.core.implementation.sponge.currency.SCurrency;
import org.core.utils.ComponentUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

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
    public @NotNull String getName() {
        return ComponentUtils.toPlain(this.account.displayName());
    }

    @Override
    public @NotNull PendingTransaction transact(@NotNull Transaction transaction) {
        if (!(transaction.getCurrency() instanceof SCurrency cur)) {
            throw new RuntimeException("Unknown type of currency: " + transaction.getCurrency().getClass().getName());
        }
        BigDecimal originalAmount =  this.account.balance(cur.getSponge());
        var result = switch (transaction.getType()){
            case DEPOSIT -> account.deposit(cur.getSponge(), transaction.getAmount());
            case WITHDRAW -> account.withdraw(cur.getSponge(), transaction.getAmount());
            case SET -> account.setBalance(cur.getSponge(), transaction.getAmount());
        };


        return null;
    }
}

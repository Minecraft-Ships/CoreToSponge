package org.core.implementation.sponge.currency.account;

import org.core.eco.Currency;
import org.core.eco.account.Account;
import org.core.eco.transaction.Transaction;
import org.core.eco.transaction.pending.PendingSingleTransactionImpl;
import org.core.eco.transaction.pending.PendingTransaction;
import org.core.eco.transaction.result.TransactionResult;
import org.core.implementation.sponge.currency.SCurrency;
import org.core.utils.ComponentUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class SEcoAccount<SAccount extends org.spongepowered.api.service.economy.account.Account> implements Account {

    private final SAccount account;

    public SEcoAccount(SAccount account) {
        this.account = account;
    }

    public SAccount getAccount() {
        return this.account;
    }

    @Override
    public @NotNull String getName() {
        return ComponentUtils.toPlain(this.account.displayName());
    }

    @Override
    public @NotNull PendingTransaction transact(@NotNull Transaction transaction) {
        if (!(transaction.getCurrency() instanceof SCurrency)) {
            throw new RuntimeException("Unknown type of currency: " + transaction.getCurrency().getClass().getName());
        }
        SCurrency cur = (SCurrency) transaction.getCurrency();
        BigDecimal originalAmount = this.account.balance(cur.getSponge());
        org.spongepowered.api.service.economy.transaction.TransactionResult result = null;

        switch (transaction.getType()) {
            case DEPOSIT:
                result = this.account.deposit(cur.getSponge(), transaction.getAmount());
                break;
            case WITHDRAW:
                result = this.account.withdraw(cur.getSponge(), transaction.getAmount());
                break;
            case SET:
                result = this.account.setBalance(cur.getSponge(), transaction.getAmount());
                break;
        }

        TransactionResult coreResult = new STransactionResult(transaction, originalAmount, result);
        return new PendingSingleTransactionImpl(this, transaction, CompletableFuture.completedFuture(coreResult));
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull Currency currency) {
        if (!(currency instanceof SCurrency)) {
            throw new RuntimeException("Unknown type of currency: " + currency.getClass().getName());
        }
        return this.account.balance(((SCurrency) currency).getSponge());
    }
}

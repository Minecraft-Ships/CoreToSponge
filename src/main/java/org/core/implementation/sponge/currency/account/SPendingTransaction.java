package org.core.implementation.sponge.currency.account;

import org.core.eco.account.Account;
import org.core.eco.transaction.Transaction;
import org.core.eco.transaction.pending.PendingSingleTransaction;
import org.core.eco.transaction.result.TransactionResult;
import org.core.eco.transaction.result.TransactionsResult;
import org.core.eco.transaction.result.impl.TransactionsResultImpl;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SPendingTransaction implements PendingSingleTransaction {

    private final org.spongepowered.api.service.economy.transaction.TransactionResult result;
    private final Transaction originalTransaction;
    private final Account account;
    private final BigDecimal originalAmount;

    public SPendingTransaction(@NotNull Transaction transaction,
                               @NotNull Account account,
                               org.spongepowered.api.service.economy.transaction.TransactionResult result,
                               BigDecimal originalAmount) {
        this.result = result;
        this.account = account;
        this.originalTransaction = transaction;
        this.originalAmount = originalAmount;
    }


    @Override
    public @NotNull Transaction getTransaction() {
        return this.originalTransaction;
    }

    @Override
    public @NotNull Account getTarget() {
        return this.account;
    }

    private TransactionResult getResult() {
        return new STransactionResult(this.originalTransaction, this.originalAmount, this.result);
    }

    @Override
    public @NotNull TransactionsResult getCurrentResult() {
        return new TransactionsResultImpl(List.of(this.getResult()));
    }

    @Override
    public @NotNull CompletableFuture<TransactionResult> awaitCurrentTransaction() {
        return CompletableFuture.completedFuture(this.getResult());
    }

    @Override
    public @NotNull CompletableFuture<TransactionsResult> awaitComplete() {
        return CompletableFuture.completedFuture(this.getCurrentResult());
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}

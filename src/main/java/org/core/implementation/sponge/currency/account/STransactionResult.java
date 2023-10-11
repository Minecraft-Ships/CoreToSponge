package org.core.implementation.sponge.currency.account;

import org.core.eco.transaction.Transaction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

import java.math.BigDecimal;
import java.util.Optional;

public class STransactionResult implements org.core.eco.transaction.result.TransactionResult {

    private final TransactionResult result;
    private final BigDecimal originalBalance;
    private final Transaction transaction;

    public STransactionResult(Transaction transaction, BigDecimal originalBalance, TransactionResult result) {
        this.transaction = transaction;
        this.originalBalance = originalBalance;
        this.result = result;
    }


    @Override
    public @NotNull Transaction getTransaction() {
        return this.transaction;
    }

    @Override
    public boolean wasSuccessful() {
        return this.result.result() == ResultType.SUCCESS;
    }

    @Override
    public @NotNull BigDecimal getOriginalAmount() {
        return this.originalBalance;
    }

    @Override
    public @NotNull BigDecimal getAfterAmount() {
        return this.result.amount();
    }

    @Override
    public Optional<String> getFailedReason() {
        return switch (this.result.result()) {
            case SUCCESS -> Optional.empty();
            case CONTEXT_MISMATCH -> Optional.of("Context Mismatch");
            case FAILED -> Optional.of("Unknown reason");
            case ACCOUNT_NO_FUNDS -> Optional.of("Account does not have enough money");
            case ACCOUNT_NO_SPACE -> Optional.of("Account is full");
        };
    }
}

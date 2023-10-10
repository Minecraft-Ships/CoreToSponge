package org.core.implementation.sponge.currency.account;

import org.core.source.eco.NamedAccount;
import org.spongepowered.api.service.economy.account.VirtualAccount;

public class SEcoNamedAccount extends SEcoAccount<VirtualAccount> implements NamedAccount {
    public SEcoNamedAccount(VirtualAccount account) {
        super(account);
    }
}

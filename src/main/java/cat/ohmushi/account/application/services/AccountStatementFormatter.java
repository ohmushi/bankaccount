package cat.ohmushi.account.application.services;

import cat.ohmushi.account.domain.account.AccountStatement;

@FunctionalInterface
public interface AccountStatementFormatter {
    String format(AccountStatement statement);
}
package cat.ohmushi.account.application.services;

import cat.ohmushi.account.domain.models.AccountStatement;

@FunctionalInterface
public interface AccountStatementFormatter {
    String format(AccountStatement statement);
}
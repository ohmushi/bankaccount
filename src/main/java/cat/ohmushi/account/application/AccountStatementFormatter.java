package cat.ohmushi.account.application;

import cat.ohmushi.account.domain.AccountStatement;

@FunctionalInterface
public interface AccountStatementFormatter {
    String format(AccountStatement statement);
}
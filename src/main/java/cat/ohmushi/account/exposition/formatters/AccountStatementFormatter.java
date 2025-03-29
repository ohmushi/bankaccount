package cat.ohmushi.account.exposition.formatters;

import cat.ohmushi.account.domain.account.AccountStatement;

@FunctionalInterface
public interface AccountStatementFormatter {
    String format(AccountStatement statement);
}
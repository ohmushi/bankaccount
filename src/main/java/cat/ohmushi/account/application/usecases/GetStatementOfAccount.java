package cat.ohmushi.account.application.usecases;

import cat.ohmushi.account.domain.account.AccountStatement;

@FunctionalInterface
public interface GetStatementOfAccount {
    AccountStatement getStatement(String id);
}

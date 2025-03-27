package cat.ohmushi.account.application.usecases;

import cat.ohmushi.account.domain.models.AccountStatement;

@FunctionalInterface
public interface GetStatementOfAccount {
    AccountStatement getStatement(String id);
}

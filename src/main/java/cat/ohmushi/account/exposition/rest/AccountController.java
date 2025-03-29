package cat.ohmushi.account.exposition.rest;

import java.util.Objects;

import cat.ohmushi.account.application.usecases.DepositMoneyInAccount;
import cat.ohmushi.account.application.usecases.GetStatementOfAccount;
import cat.ohmushi.account.application.usecases.WithdrawMoneyFromAccount;
import cat.ohmushi.account.exposition.formatters.AccountStatementFormatter;
import cat.ohmushi.account.exposition.formatters.DefaultAccountStatementFormatter;

public class AccountController {

    DepositMoneyInAccount deposit;
    WithdrawMoneyFromAccount withdraw;
    GetStatementOfAccount getStatement;
    private final AccountStatementFormatter formatter;

    public AccountController(
            final DepositMoneyInAccount deposit,
            final WithdrawMoneyFromAccount withdraw,
            final GetStatementOfAccount getStatement,
            final AccountStatementFormatter formatter) {
        this.deposit = deposit;
        this.withdraw = withdraw;
        this.getStatement = getStatement;
        this.formatter = Objects.isNull(formatter)
                ? new DefaultAccountStatementFormatter()
                : formatter;
    }

    // TODO
}

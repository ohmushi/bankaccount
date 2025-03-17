package cat.ohmushi.account.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import cat.ohmushi.account.domain.Account;
import cat.ohmushi.account.domain.AccountId;
import cat.ohmushi.account.domain.AccountStatement;
import cat.ohmushi.account.domain.Accounts;
import cat.ohmushi.account.domain.Money;
import cat.ohmushi.account.usecases.AccountApplicationException;
import cat.ohmushi.account.usecases.AccountApplicationException.AccountNotFoundException;
import cat.ohmushi.account.usecases.DepositMoneyInAccount;
import cat.ohmushi.account.usecases.GetStatementOfAccount;
import cat.ohmushi.account.usecases.WithdrawMoneyFromAccount;

public class AccountService implements DepositMoneyInAccount, WithdrawMoneyFromAccount, GetStatementOfAccount {

    private final Accounts accounts;
    private final AccountStatementFormatter formatter;

    public AccountService(Accounts accounts, AccountStatementFormatter formatter) {
        this.accounts = Objects.requireNonNull(accounts);
        this.formatter = Objects.isNull(formatter) ? new DefaultAccountStatementFormatter() : formatter;
    }

    private Account getAccount(String id) throws AccountApplicationException {
        AccountId accountId = AccountId.of(id)
                .orElseThrow(() -> new AccountApplicationException("Invalid id " + id));
        return this.accounts.findAccountByItsId(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    @Override
    public void withdraw(String accountId, BigDecimal amount) throws AccountApplicationException {
        Account account = this.getAccount(accountId);
        Money money = Money.of(amount, account.currency()).orElseThrow(() -> new AccountApplicationException("Invalid money " + String.valueOf(amount)));

        account.withdraw(money, LocalDateTime.now());
        this.accounts.save(account);
    }

    @Override
    public void deposit(String accountId, BigDecimal amount) throws AccountApplicationException {
        Account account = this.getAccount(accountId);
        Money money = Money.of(amount, account.currency()).orElseThrow(() -> new AccountApplicationException("Invalid money " + String.valueOf(amount)));

        account.deposit(money, LocalDateTime.now());
        this.accounts.save(account);
    }

    @Override
    public String getStatement(String id) throws AccountApplicationException {
        Account account = this.getAccount(id);
        return this.formatter.format(AccountStatement.forAccount(account));
    }

}

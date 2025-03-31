package cat.ohmushi.account.application.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import cat.ohmushi.account.application.exceptions.AccountApplicationException;
import cat.ohmushi.account.application.exceptions.AccountApplicationException.AccountNotFoundException;
import cat.ohmushi.account.application.exceptions.AccountApplicationException.AccountTransfertException;
import cat.ohmushi.account.application.usecases.DepositMoneyInAccount;
import cat.ohmushi.account.application.usecases.GetStatementOfAccount;
import cat.ohmushi.account.application.usecases.WithdrawMoneyFromAccount;
import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.account.domain.account.AccountId;
import cat.ohmushi.account.domain.account.AccountStatement;
import cat.ohmushi.account.domain.account.Money;
import cat.ohmushi.account.domain.events.TransfertFailed;
import cat.ohmushi.account.domain.exceptions.AccountDomainException;
import cat.ohmushi.account.domain.repositories.Accounts;

public class AccountService implements
        DepositMoneyInAccount,
        WithdrawMoneyFromAccount,
        GetStatementOfAccount {

    private final Accounts accounts;

    public AccountService(Accounts accounts) {
        this.accounts = Objects.requireNonNull(accounts);
    }

    private Account getAccount(String id) throws AccountApplicationException {
        AccountId accountId = AccountId.of(id)
                .orElseThrow(() -> new AccountApplicationException("Invalid id " + id));
        return this.accounts.findAccountById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    @Override
    public void withdraw(String accountId, BigDecimal amount) throws AccountApplicationException {
        Account account = this.getAccount(accountId);
        Money money = Money.of(amount, account.currency())
                .orElseThrow(() -> new AccountApplicationException("Invalid money " + String.valueOf(amount)));
        var now = LocalDateTime.now();

        try {
            account.withdraw(money, now);
            this.accounts.save(account);
        } catch (AccountDomainException e) {
            account.pushInHistory(new TransfertFailed(e, now, account.balance()));
            this.accounts.save(account);
            throw new AccountTransfertException(e);
        }

    }

    @Override
    public void deposit(String accountId, BigDecimal amount) throws AccountApplicationException {
        Account account = this.getAccount(accountId);
        Money money = Money.of(amount, account.currency())
                .orElseThrow(() -> new AccountApplicationException("Invalid money " + String.valueOf(amount)));
        var now = LocalDateTime.now();

        try {
            account.deposit(money, now);
            this.accounts.save(account);
        } catch (AccountDomainException e) {
            account.pushInHistory(new TransfertFailed(e, now, account.balance()));
            this.accounts.save(account);
            throw new AccountTransfertException(e);
        }
    }

    @Override
    public AccountStatement getStatement(String id) throws AccountApplicationException {
        Account account = this.getAccount(id);
        return AccountStatement.forAccount(account);
    }

}

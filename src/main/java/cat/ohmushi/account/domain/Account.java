package cat.ohmushi.account.domain;

import java.util.Objects;
import java.util.Optional;

import cat.ohmushi.account.domain.AccountException.DepositException;
import cat.ohmushi.account.domain.AccountException.WithdrawException;
import cat.ohmushi.shared.annotations.DomainEntity;

@DomainEntity
public final class Account {

    private final AccountId id;
    private Money balance;
    private final Currency currency;

    private Account(AccountId id, Money balance, Currency currency) throws AccountException {
        try {
            this.id = Objects.requireNonNull(id);
            this.balance = Objects.requireNonNull(balance);
            this.currency = Objects.requireNonNull(currency);
        } catch (NullPointerException e) {
            throw new AccountException("Account cannot have null id or balance.");
        }
    }

    public static Account create(AccountId id, Money balance, Currency currency) throws AccountException {
        if (!balance.isZeroOrPositive()) {
            throw new AccountException("Cannot create an account with a strictly negative balance.");
        }

        return new Account(id, balance, currency);
    }

    public Money balance() {
        return this.balance;
    }

    public boolean currencyIs(Currency currency) {
        return Optional.ofNullable(currency)
                .map(c -> this.currency.equals(c))
                .orElse(false);
    }

    public void deposit(Money amount) throws DepositException {
        if (!this.currencyIs(amount.currency())) {
            String amountCurrency = Objects.isNull(amount) ? null : amount.currency().toString();
            throw AccountException.deposit("Cannot deposit " + amountCurrency + " to " + this.currency + " account.");
        }
        if (!amount.isStrictlyPositive()) {
            throw AccountException.deposit("Money transferred cannot be negative.");
        }

        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) throws WithdrawException {
        if (!this.currencyIs(amount.currency())) {
            String amountCurrency = Objects.isNull(amount) ? null : amount.currency().toString();
            throw AccountException.deposit("Cannot withdraw " + amountCurrency + " to " + this.currency + " account.");
        }
        if (!amount.isStrictlyPositive()) {
            throw AccountException.withdrawal("Money transferred cannot be negative.");
        }

        this.balance = this.balance.minus(amount);
    }
}

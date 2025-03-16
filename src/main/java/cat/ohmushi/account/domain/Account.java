package cat.ohmushi.account.domain;

import java.util.Collections;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import cat.ohmushi.account.domain.AccountEvent.AccountCreated;
import cat.ohmushi.account.domain.AccountEvent.MoneyDepositedInAccount;
import cat.ohmushi.account.domain.AccountEvent.MoneyWithdrawnFromAccount;
import cat.ohmushi.account.domain.AccountException.TransfertException;
import cat.ohmushi.shared.annotations.DomainEntity;
import cat.ohmushi.shared.annotations.DomainEntity.DomainEntityT;

@DomainEntity
public final class Account implements DomainEntityT {

    private final AccountId id;
    private Money balance;
    private final Currency currency;
    private final List<AccountEvent> events;

    private Account(AccountId id, Money balance, Currency currency, List<AccountEvent> events) throws AccountException {
        try {
            this.id = Objects.requireNonNull(id);
            this.balance = Objects.requireNonNull(balance);
            this.currency = Objects.requireNonNull(currency);
            this.events = Objects.isNull(events) ? new ArrayList<AccountEvent>() : events;
        } catch (NullPointerException e) {
            throw new AccountException("Account cannot have null field.");
        }

        if (!balance.currency().equals(currency)) {
            throw new AccountException(
                    "Cannot create account in " + currency + " with " + balance.currency() + " initial balance.");
        }
    }

    public static Account create(AccountId id, Money balance, Currency currency)
            throws AccountException {
        if (!balance.isZeroOrPositive()) {
            throw new AccountException("Cannot create an account with a strictly negative balance.");
        }

        final var account = new Account(id, balance, currency, new ArrayList<>());
        account.addEvent(new AccountCreated(id, balance, currency, LocalDateTime.now()));
        return account;
    }

    public AccountId id() {
        return this.id;
    }

    public Money balance() {
        return this.balance;
    }

    public List<AccountEvent> events() {
        return Collections.unmodifiableList(this.events);
    }

    public boolean currencyIs(Currency currency) {
        return Optional.ofNullable(currency)
                .map(c -> this.currency.equals(c))
                .orElse(false);
    }

    public void deposit(Money amount) throws TransfertException {
        this.ensureValidAmount(amount);
        this.balance = this.balance.add(amount);
        this.addEvent(new MoneyDepositedInAccount(amount, LocalDateTime.now()));
    }

    public void addEvent(AccountEvent e) {
        if (Objects.nonNull(e)) {
            this.events.add(e);
        }
    }

    public void withdraw(Money amount) throws TransfertException {
        this.ensureValidAmount(amount);
        this.balance = this.balance.minus(amount);
        this.addEvent(new MoneyWithdrawnFromAccount(amount, LocalDateTime.now()));
    }

    private void ensureValidAmount(Money amount) throws AccountException {
        if (!this.currencyIs(amount.currency())) {
            String amountCurrency = Objects.isNull(amount) ? null : amount.currency().toString();
            throw AccountException
                    .transfert("Cannot transfert " + amountCurrency + " to " + this.currency + " account.");
        }
        if (!amount.isStrictlyPositive()) {
            throw AccountException.transfert("Money transferred cannot be negative.");
        }
    }

}

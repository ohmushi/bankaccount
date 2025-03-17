package cat.ohmushi.account.domain;

import java.time.LocalDate;
import java.util.Collections;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import cat.ohmushi.account.domain.AccountEvent.AccountCreated;
import cat.ohmushi.account.domain.AccountEvent.MoneyDepositedInAccount;
import cat.ohmushi.account.domain.AccountEvent.MoneyWithdrawnFromAccount;
import cat.ohmushi.account.domain.AccountEvent.TransfertFailed;
import cat.ohmushi.shared.annotations.DomainEntity;
import cat.ohmushi.shared.annotations.DomainEntity.DomainEntityType;

@DomainEntity
public final class Account implements DomainEntityType {

    private final AccountId id;
    private Money balance;
    private final Currency currency;
    private final List<AccountEvent> events;

    private Account(AccountId id, Money balance, Currency currency, List<AccountEvent> events) throws AccountDomainException {
        try {
            this.id = Objects.requireNonNull(id);
            this.balance = Objects.requireNonNull(balance);
            this.currency = Objects.requireNonNull(currency);
            this.events = Objects.isNull(events) ? new ArrayList<AccountEvent>() : events;
        } catch (NullPointerException e) {
            throw new AccountDomainException("Account cannot have null field.");
        }

        if (!balance.currency().equals(currency)) {
            throw new AccountDomainException(
                    "Cannot create account in " + currency + " with " + balance.currency() + " initial balance.");
        }
    }

    public static Account create(AccountId id, Money balance, Currency currency)
            throws AccountDomainException {
        if (!balance.isZeroOrPositive()) {
            throw new AccountDomainException("Cannot create an account with a strictly negative balance.");
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

    public Currency currency() {
        return this.currency;
    }

    public List<AccountEvent> events() {
        return Collections.unmodifiableList(this.events);
    }

    public boolean currencyIs(Currency currency) {
        return Optional.ofNullable(currency)
                .map(c -> this.currency.equals(c))
                .orElse(false);
    }

    public void deposit(Money amount, LocalDateTime date){
        try {
            this.ensureValidAmount(amount);
            this.balance = this.balance.add(amount);
            this.addEvent(new MoneyDepositedInAccount(amount, date, this.balance));
        } catch (AccountDomainException e) {
            this.addEvent(new TransfertFailed(e, date, this.balance));
        }
    }

    public void addEvent(AccountEvent e) {
        if (Objects.nonNull(e)) {
            this.events.add(e);
        }
    }

    public void withdraw(Money amount, LocalDateTime date) {
        try {
            this.ensureValidAmount(amount);
            this.balance = this.balance.minus(amount);
            this.addEvent(new MoneyWithdrawnFromAccount(amount, date, this.balance));
        } catch (AccountDomainException e) {
            this.addEvent(new TransfertFailed(e, date, this.balance));
        }
    }

    private void ensureValidAmount(Money amount) throws AccountDomainException {
        if (!this.currencyIs(amount.currency())) {
            String amountCurrency = Objects.isNull(amount) ? null : amount.currency().toString();
            throw AccountDomainException
                    .transfert("Cannot transfert " + amountCurrency + " to " + this.currency + " account.");
        }
        if (!amount.isStrictlyPositive()) {
            throw AccountDomainException.transfert("Money transferred cannot be negative.");
        }
    }

}

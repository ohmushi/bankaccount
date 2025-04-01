package cat.ohmushi.account.domain.account;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import cat.ohmushi.account.domain.events.AccountCreated;
import cat.ohmushi.account.domain.events.AccountEvent;
import cat.ohmushi.account.domain.events.MoneyDepositedInAccount;
import cat.ohmushi.account.domain.events.MoneyWithdrawnFromAccount;
import cat.ohmushi.account.domain.exceptions.AccountDomainException;
import cat.ohmushi.shared.annotations.DomainEntity;
import cat.ohmushi.shared.annotations.DomainEntity.DomainEntityType;

@DomainEntity
public final class Account implements DomainEntityType {

    private final AccountId id;
    private Money balance;
    private final Currency currency;
    private final List<AccountEvent> history;

    private Account(AccountId id, Money balance, Currency currency, List<AccountEvent> history)
            throws AccountDomainException {
        this.id = id;
        this.balance = balance;
        this.currency = currency;
        this.history = Objects.isNull(history)
                ? new ArrayList<>(List.of(new AccountCreated(id, balance, currency, Instant.now())))
                : history;

        this.validateAccount();
    }

    private void validateAccount() throws AccountDomainException {
        AccountDomainException.requireNonNull(id, "Id cannot be null.");
        AccountDomainException.requireNonNull(balance, "Balance cannot be null.");
        AccountDomainException.requireNonNull(currency, "Currency cannot be null.");

        if (!balance.currency().equals(currency)) {
            throw new AccountDomainException(
                    "Cannot have account in " + currency + " with " + balance.currency() + " balance.");
        }
        if (Objects.requireNonNull(history, "History cannot be null").isEmpty()) {
            throw new AccountDomainException(
                    "Cannot create account with empty history.");
        }
    }

    public static Account create(AccountId id, Money balance, Currency currency, Instant creationDate)
            throws AccountDomainException {
        if (!balance.isZeroOrPositive()) {
            throw new AccountDomainException("Cannot create an account with a strictly negative balance.");
        }

        var creationEvent = new AccountCreated(id, balance, currency,
                Objects.requireNonNull(creationDate, "Cannot create account without the creation date"));
        final var account = new Account(id, balance, currency, new ArrayList<>(List.of(creationEvent)));
        return account;
    }

    public static Account create(AccountId id, Money balance, Currency currency) {
        return create(id, balance, currency, Instant.now());
    }

    public static Account fromHistory(List<AccountEvent> history) {
        if (Objects.isNull(history) || history.isEmpty()) {
            throw new IllegalArgumentException("Cannot replay Account from empty history.");
        }

        return history.stream()
                .reduce(
                        null,
                        (Account played, AccountEvent event) -> event.play(played),
                        (old, updated) -> updated);
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

    public List<AccountEvent> history() {
        return Collections.unmodifiableList(this.history);
    }

    public boolean currencyIs(Currency currency) {
        return currency != null && this.currency.equals(currency);
    }

    public void deposit(Money amount, Instant date) throws AccountDomainException {
        this.ensureValidAmount(amount);
        this.ensureValidDate(date);
        this.balance = this.balance.add(amount);
        this.pushInHistory(new MoneyDepositedInAccount(amount, date));
    }

    public void pushInHistory(AccountEvent e) {
        if (Objects.nonNull(e)) {
            this.history.add(e);
        }
    }

    public void withdraw(Money amount, Instant date) throws AccountDomainException {
        this.ensureValidAmount(amount);
        this.ensureValidDate(date);
        this.balance = this.balance.minus(amount);
        this.pushInHistory(new MoneyWithdrawnFromAccount(amount, date));
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

    private void ensureValidDate(Instant date) throws AccountDomainException {
        final var lastAppendEventDate = this.lastAppendEvent().getDate();
        if (date.isBefore(lastAppendEventDate) || date.equals(lastAppendEventDate)) {
            throw AccountDomainException.transfert("Cannot change Account history.");
        }
    }

    public AccountEvent lastAppendEvent() throws NoSuchElementException {
        return this.history.stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Account history should not be empty but is."));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}

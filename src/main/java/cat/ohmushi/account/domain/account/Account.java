package cat.ohmushi.account.domain.account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

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
        try {
            this.id = Objects.requireNonNull(id);
            this.balance = Objects.requireNonNull(balance);
            this.currency = Objects.requireNonNull(currency);
            this.history = Objects.isNull(history)
                    ? new ArrayList<>(List.of(new AccountCreated(id, balance, currency, LocalDateTime.now())))
                    : history;
        } catch (NullPointerException e) {
            throw new AccountDomainException("Account cannot have null field.");
        }

        if (!balance.currency().equals(currency)) {
            throw new AccountDomainException(
                    "Cannot create account in " + currency + " with " + balance.currency() + " initial balance.");
        }
    }

    public static Account create(AccountId id, Money balance, Currency currency, LocalDateTime creationDate)
            throws AccountDomainException {
        if (!balance.isZeroOrPositive()) {
            throw new AccountDomainException("Cannot create an account with a strictly negative balance.");
        }

        final var account = new Account(id, balance, currency, new ArrayList<>());
        account.pushHistory(new AccountCreated(id, balance, currency, Objects.requireNonNull(creationDate)));

        return account;
    }

    public static Account create(AccountId id, Money balance, Currency currency) {
        return create(id, balance, currency, LocalDateTime.now());
    }

    public static Account fromHistory(List<AccountEvent> history) {
        if (Objects.isNull(history) || history.isEmpty()) {
            throw new IllegalArgumentException("Cannot create Account from empty history.");
        }
        // TODO should add verifications like :
        // - first event must be AcountCreated
        // - only one event of kind AcountCreated
        // - ...
        // for now it is dangerous...

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
        return Optional.ofNullable(currency)
                .map(c -> this.currency.equals(c))
                .orElse(false);
    }

    public void deposit(Money amount, LocalDateTime date) throws AccountDomainException {
        this.ensureValidAmount(amount);
        this.ensureValidDate(date);
        this.balance = this.balance.add(amount);
        this.pushHistory(new MoneyDepositedInAccount(amount, date));
    }

    public void pushHistory(AccountEvent e) {
        if (Objects.nonNull(e)) {
            this.history.add(e);
        }
    }

    public void withdraw(Money amount, LocalDateTime date) throws AccountDomainException {
        this.ensureValidAmount(amount);
        this.ensureValidDate(date);
        this.balance = this.balance.minus(amount);
        this.pushHistory(new MoneyWithdrawnFromAccount(amount, date));
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

    private void ensureValidDate(LocalDateTime date) throws AccountDomainException {
        final var lastAppendEventDate = this.lastAppendEvent().getDate();
        if (date.equals(lastAppendEventDate) || date.isBefore(lastAppendEventDate)) {
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

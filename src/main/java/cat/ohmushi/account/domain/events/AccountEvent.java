package cat.ohmushi.account.domain.events;

import java.time.LocalDateTime;

import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.account.domain.account.AccountId;
import cat.ohmushi.account.domain.account.Currency;
import cat.ohmushi.account.domain.account.Money;
import cat.ohmushi.account.domain.exceptions.AccountDomainException;
import cat.ohmushi.shared.Event;
import cat.ohmushi.shared.annotations.DomainEvent;
import cat.ohmushi.shared.annotations.Value;

@DomainEvent
public interface AccountEvent extends Event<Account> {

    Money newBalance();

    @Value
    record AccountCreated(
            AccountId id,
            Money balance,
            Currency currency,
            LocalDateTime creationDate) implements AccountEvent {

        @Override
        public Account play(Account a) {
            return Account.create(this.id, this.balance, this.currency);
        }

        @Override
        public LocalDateTime getDate() {
            return this.creationDate;
        }

        @Override
        public Money newBalance() {
            return this.balance;
        }
    }

    @Value
    record MoneyDepositedInAccount(
            Money deposited,
            LocalDateTime eventDate,
            Money newBalance) implements AccountEvent {

        @Override
        public Account play(Account a) {
            a.deposit(this.deposited, this.eventDate);
            return a;
        }

        @Override
        public LocalDateTime getDate() {
            return this.eventDate;
        }

    }

    @Value
    record MoneyWithdrawnFromAccount(
            Money withdrawn,
            LocalDateTime eventDate,
            Money newBalance) implements AccountEvent {

        @Override
        public Account play(Account a) {
            a.withdraw(this.withdrawn, this.eventDate);
            return a;
        }

        @Override
        public LocalDateTime getDate() {
            return this.eventDate;
        }
    }

    @Value
    record TransfertFailed(
            AccountDomainException reason,
            LocalDateTime eventDate,
            Money balanceBeforeFail) implements AccountEvent {

        @Override
        public Account play(Account a) {
            return a;
        }

        @Override
        public LocalDateTime getDate() {
            return this.eventDate;
        }

        @Override
        public Money newBalance() {
            return this.balanceBeforeFail;
        }
    }

}

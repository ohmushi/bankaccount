package cat.ohmushi.account.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import cat.ohmushi.shared.Event;
import cat.ohmushi.shared.annotations.DomainEvent;
import cat.ohmushi.shared.annotations.Value;

@DomainEvent
interface AccountEvent extends Event<Account> {

  @Value
  public class AccountCreated implements AccountEvent {
    private final AccountId id;
    private final Money balance;
    private final Currency currency;
    private LocalDateTime creationDate;

    public AccountCreated(AccountId id, Money balance, Currency currency, LocalDateTime creationDate) {
      this.id = Objects.requireNonNull(id);
      this.balance = Objects.requireNonNull(balance);
      this.currency = Objects.requireNonNull(currency);
      this.creationDate = Objects.requireNonNull(creationDate);
    }

    @Override
    public Account play(Account a) {
      return Account.create(this.id, this.balance, this.currency);
    }

    @Override
    public LocalDateTime getDate() {
      return this.creationDate;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      result = prime * result + ((balance == null) ? 0 : balance.hashCode());
      result = prime * result + ((currency == null) ? 0 : currency.hashCode());
      result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
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
      AccountCreated other = (AccountCreated) obj;
      if (id == null) {
        if (other.id != null)
          return false;
      } else if (!id.equals(other.id))
        return false;
      if (balance == null) {
        if (other.balance != null)
          return false;
      } else if (!balance.equals(other.balance))
        return false;
      if (currency != other.currency)
        return false;
      if (creationDate == null) {
        if (other.creationDate != null)
          return false;
      } else if (!creationDate.equals(other.creationDate))
        return false;
      return true;
    }

    @Override
    public String toString() {
      return "AccountCreated [id=" + id + ", balance=" + balance + ", currency=" + currency + ", creationDate="
          + creationDate + "]";
    }

  }

  @Value
  public class MoneyDepositedInAccount implements AccountEvent {
    private final Money deposited;
    private LocalDateTime eventDate;

    public MoneyDepositedInAccount(Money amount, LocalDateTime creationDate) {
      this.deposited = Objects.requireNonNull(amount);
      this.eventDate = Objects.isNull(creationDate) ? LocalDateTime.now() : creationDate;
    }

    @Override
    public Account play(Account a) {
      a.deposit(this.deposited);
      a.addEvent(this);
      return a;
    }

    @Override
    public LocalDateTime getDate() {
      return this.eventDate;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((deposited == null) ? 0 : deposited.hashCode());
      result = prime * result + ((eventDate == null) ? 0 : eventDate.hashCode());
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
      MoneyDepositedInAccount other = (MoneyDepositedInAccount) obj;
      if (deposited == null) {
        if (other.deposited != null)
          return false;
      } else if (!deposited.equals(other.deposited))
        return false;
      if (eventDate == null) {
        if (other.eventDate != null)
          return false;
      } else if (!eventDate.equals(other.eventDate))
        return false;
      return true;
    }

    @Override
    public String toString() {
      return "MoneyDepositedInAccount [deposited=" + deposited + ", eventDate=" + eventDate + "]";
    }

  }

  @Value
  public class MoneyWithdrawnFromAccount implements AccountEvent {
    private final Money withdrawn;
    private LocalDateTime eventDate;

    public MoneyWithdrawnFromAccount(Money amount, LocalDateTime creationDate) {
      this.withdrawn = Objects.requireNonNull(amount);
      this.eventDate = Objects.isNull(creationDate) ? LocalDateTime.now() : creationDate;
    }

    @Override
    public Account play(Account a) {
      a.withdraw(this.withdrawn);
      a.addEvent(this);
      return a;
    }

    @Override
    public LocalDateTime getDate() {
      return this.eventDate;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((withdrawn == null) ? 0 : withdrawn.hashCode());
      result = prime * result + ((eventDate == null) ? 0 : eventDate.hashCode());
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
      MoneyDepositedInAccount other = (MoneyDepositedInAccount) obj;
      if (withdrawn == null) {
        if (other.deposited != null)
          return false;
      } else if (!withdrawn.equals(other.deposited))
        return false;
      if (eventDate == null) {
        if (other.eventDate != null)
          return false;
      } else if (!eventDate.equals(other.eventDate))
        return false;
      return true;
    }

    @Override
    public String toString() {
      return "MoneyWithdrawnFromAccount [withdrawn=" + withdrawn + ", eventDate=" + eventDate + "]";
    }

  }

}

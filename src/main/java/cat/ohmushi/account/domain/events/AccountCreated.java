package cat.ohmushi.account.domain.events;

import java.time.Instant;
import java.time.LocalDateTime;

import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.account.domain.account.AccountId;
import cat.ohmushi.account.domain.account.Currency;
import cat.ohmushi.account.domain.account.Money;
import cat.ohmushi.shared.annotations.Value;

@Value
public record AccountCreated(
    AccountId id,
    Money balance,
    Currency currency,
    Instant creationDate) implements AccountEvent {

  @Override
  public Account play(Account a) {
    return Account.create(this.id, this.balance, this.currency, creationDate);
  }

  @Override
  public Instant getDate() {
    return this.creationDate;
  }
}

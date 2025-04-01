package cat.ohmushi.account.domain.events;

import java.time.Instant;

import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.account.domain.account.Money;
import cat.ohmushi.shared.annotations.Value;

@Value
public record MoneyWithdrawnFromAccount(
    Money withdrawn,
    Instant eventDate) implements AccountEvent {

  @Override
  public Account play(Account a) {
    a.withdraw(this.withdrawn, this.eventDate);
    return a;
  }

  @Override
  public Instant getDate() {
    return this.eventDate;
  }
}
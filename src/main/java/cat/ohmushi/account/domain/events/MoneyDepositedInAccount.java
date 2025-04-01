package cat.ohmushi.account.domain.events;

import java.time.Instant;
import java.time.LocalDateTime;

import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.account.domain.account.Money;
import cat.ohmushi.shared.annotations.Value;

@Value
public record MoneyDepositedInAccount(Money deposited, Instant depositedAt) implements AccountEvent {

  @Override
  public Account play(Account a) {
    a.deposit(this.deposited, this.depositedAt);
    return a;
  }

  @Override
  public Instant getDate() {
    return this.depositedAt;
  }

}
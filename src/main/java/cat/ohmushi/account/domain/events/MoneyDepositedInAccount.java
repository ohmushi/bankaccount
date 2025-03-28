package cat.ohmushi.account.domain.events;

import java.time.LocalDateTime;

import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.account.domain.account.Money;
import cat.ohmushi.shared.annotations.Value;

@Value
public record MoneyDepositedInAccount(
    Money deposited,
    LocalDateTime eventDate) implements AccountEvent {

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
package cat.ohmushi.account.domain.events;

import java.time.LocalDateTime;

import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.account.domain.account.Money;
import cat.ohmushi.account.domain.exceptions.AccountDomainException;
import cat.ohmushi.shared.annotations.Value;

@Value
public record TransfertFailed(
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
}
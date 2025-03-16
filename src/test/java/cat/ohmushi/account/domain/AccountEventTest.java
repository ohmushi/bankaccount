package cat.ohmushi.account.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;

import cat.ohmushi.account.domain.AccountEvent.AccountCreated;
import cat.ohmushi.account.domain.AccountEvent.MoneyDepositedInAccount;
import cat.ohmushi.account.domain.AccountEvent.MoneyWithdrawnFromAccount;

public class AccountEventTest {

  private final static AccountId id = AccountId.of("id").get();
  private final static Money zeroEuro = Money.of(0, Currency.EUR).get();
  private final static Money tenEuro = Money.of(10, Currency.EUR).get();
  private final static RecursiveComparisonConfiguration ignoreDates = RecursiveComparisonConfiguration.builder()
      .withIgnoredFieldsOfTypes(LocalDateTime.class)
      .build();

  @Test
  void createdAccountShouldHaveOneCreatedAccountEventInIt() {
    var account = Account.create(id, zeroEuro, Currency.EUR);

    assertThat(account.events())
        .usingRecursiveFieldByFieldElementComparator(ignoreDates)
        .containsExactly(new AccountCreated(id, zeroEuro, Currency.EUR, LocalDateTime.now()));
  }

  @Test
  void depositedInAccountShouldHaveCreationEventAndDepositedEvent() {
    var account = Account.create(id, zeroEuro, Currency.EUR);
    account.deposit(tenEuro);

    assertThat(account.events())
        .usingRecursiveFieldByFieldElementComparator(ignoreDates)
        .containsExactly(
            new AccountCreated(id, zeroEuro, Currency.EUR, LocalDateTime.now()),
            new MoneyDepositedInAccount(tenEuro, LocalDateTime.now()));
  }

  @Test
  void withdrawFromAccountShouldHaveWithdrawnEvent() {
    var account = Account.create(id, zeroEuro, Currency.EUR);
    account.deposit(tenEuro);
    account.withdraw(tenEuro);

    assertThat(account.events())
        .usingRecursiveFieldByFieldElementComparator(ignoreDates)
        .containsExactly(
            new AccountCreated(id, zeroEuro, Currency.EUR, LocalDateTime.now()),
            new MoneyDepositedInAccount(tenEuro, LocalDateTime.now()),
            new MoneyWithdrawnFromAccount(tenEuro, LocalDateTime.now()));
  }
}

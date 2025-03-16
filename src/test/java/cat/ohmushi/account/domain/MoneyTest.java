package cat.ohmushi.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class MoneyTest {

  private static Money zeroEuro = Money.of(0, Currency.EUR).get();
  private static Money oneEuro = Money.of(1, Currency.EUR).get();
  private static Money minusOneEuro = Money.of(-1, Currency.EUR).get();
  private static Money hundredEuros = Money.of(100, Currency.EUR).get();
  private static Money threeEuros = Money.of(3, Currency.EUR).get();
  private static Money minusOneHundredEuros = Money.of(-100, Currency.EUR).get();
  private static Money oneDollar = Money.of(1, Currency.USD).get();

  @Test
  void createMoneyShouldGivesValue() {
    assertThat(Money.of(BigDecimal.ZERO, Currency.EUR)).isNotEmpty();
  }

  @Test
  void createMoneyWithNullIsEmpty() {
    assertThat(Money.of(null, Currency.EUR)).isEmpty();
  }

  @Test
  void moneyWithSameAmountShouldBeEqual() {
    assertThat(Money.of(10, Currency.EUR)).isEqualTo(Money.of(10, Currency.EUR));
  }

  @Test
  void moneyWithSameAmountButDifferentCurrenciesShouldNotBeEqual() {
    assertThat(Money.of(10, Currency.EUR)).isNotEqualTo(Money.of(10, Currency.USD));
  }

  @Test
  void ensureStrictlyPositiveMoney() {
    assertThat(oneEuro.isStrictlyPositive()).isTrue();
    assertThat(zeroEuro.isStrictlyPositive()).isFalse();
    assertThat(minusOneEuro.isStrictlyPositive()).isFalse();
  }

  @Test
  void ensureZeroOrPositiveMoney() {
    assertThat(oneEuro.isZeroOrPositive()).isTrue();
    assertThat(zeroEuro.isZeroOrPositive()).isTrue();
    assertThat(minusOneEuro.isZeroOrPositive()).isFalse();
  }

  @Test
  void addMoneyToMoneyShouldGivesAnotherMoneyWithRightAmount() {

    assertThat(hundredEuros.add(threeEuros)).isEqualTo(Money.of(103, Currency.EUR).get());
    assertThat(minusOneHundredEuros.add(threeEuros)).isEqualTo(Money.of(-97, Currency.EUR).get());
  }

  @Test
  void subtractMoneyToMoneyShouldGivesAnotherMoneyWithRightAmount() {
    assertThat(hundredEuros.minus(threeEuros)).isEqualTo(Money.of(97, Currency.EUR).get());
    assertThat(minusOneHundredEuros.minus(threeEuros)).isEqualTo(Money.of(-103, Currency.EUR).get());
  }

  @Test
  void addMoneyOfDifferentCurrenciesShouldThrowError() {
    assertThatThrownBy(() -> oneEuro.add(oneDollar))
        .isInstanceOf(MoneyException.class)
        .hasMessage("Cannot add money of different currencies. Tried to add USD to EUR.");
  }

  @Test
  void subtractMoneyOfDifferentCurrenciesShouldThrowError() {
    assertThatThrownBy(() -> oneEuro.minus(oneDollar))
        .isInstanceOf(MoneyException.class)
        .hasMessage("Cannot subtract money of different currencies. Tried to subtract USD to EUR.");
  }

}

package cat.ohmushi;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import cat.ohmushi.account.domain.Money;

public class MoneyTest {

  @Test
  void createMoneyShouldGivesValue() {
    assertThat(Money.of(BigDecimal.ZERO)).isNotEmpty();
  }

  @Test
  void createMoneyWithNullIsEmpty() {
    assertThat(Money.of(null)).isEmpty();
  }

  @Test
  void moneyWithSameAmountShouldBeEqual() {
    assertThat(Money.of(10)).isEqualTo(Money.of(10));
  }

  @Test
  void ensureStrictlyPositiveMoney() {
    assertThat(Money.of(1).get().isStrictlyPositive()).isTrue();
    assertThat(Money.of(0).get().isStrictlyPositive()).isFalse();
    assertThat(Money.of(-1).get().isStrictlyPositive()).isFalse();
  }

  @Test
  void ensureZeroOrPositiveMoney() {
    assertThat(Money.of(1).get().isZeroOrPositive()).isTrue();
    assertThat(Money.of(0).get().isZeroOrPositive()).isTrue();
    assertThat(Money.of(-1).get().isZeroOrPositive()).isFalse();
  }

  @Test
  void addMoneyToMoneyShouldGivesAnotherMoneyWithRightAmount() {
    assertThat(Money.of(100).get().add(Money.of(3).get())).isEqualTo(Money.of(103).get());
    assertThat(Money.of(-100).get().add(Money.of(3).get())).isEqualTo(Money.of(-97).get());
  }

  @Test
  void subtractMoneyToMoneyShouldGivesAnotherMoneyWithRightAmount() {
    assertThat(Money.of(100).get().minus(Money.of(3).get())).isEqualTo(Money.of(97).get());
    assertThat(Money.of(-100).get().minus(Money.of(3).get())).isEqualTo(Money.of(-103).get());
  }
  
}

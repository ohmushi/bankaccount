package cat.ohmushi.account.domain.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import cat.ohmushi.account.domain.exceptions.MoneyException;

public class MoneyTest {

    private static final Money zeroEuro = Money.of(0, Currency.EUR).get();
    private static final Money oneEuro = Money.of(1, Currency.EUR).get();
    private static final Money minusOneEuro = Money.of(-1, Currency.EUR).get();
    private static final Money hundredEuros = Money.of(100, Currency.EUR).get();
    private static final Money threeEuros = Money.of(3, Currency.EUR).get();
    private static final Money minusOneHundredEuros = Money.of(-100, Currency.EUR).get();
    private static final Money oneDollar = Money.of(1, Currency.USD).get();

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
                .hasMessage("Cannot calculate money of different currencies. Tried USD with EUR.");
    }

    @Test
    void subtractMoneyOfDifferentCurrenciesShouldThrowError() {
        assertThatThrownBy(() -> oneEuro.minus(oneDollar))
                .isInstanceOf(MoneyException.class)
                .hasMessage("Cannot calculate money of different currencies. Tried USD with EUR.");
    }

    @Test
    void addNullToMoneyShouldThrowError() {
        assertThatThrownBy(() -> oneEuro.add(null))
                .isInstanceOf(MoneyException.class)
                .hasMessage("Cannot calculate money of different currencies. Tried null with EUR.");
    }

    @Test
    void subtractNullToMoneyShouldThrowError() {
        assertThatThrownBy(() -> oneEuro.minus(null))
                .isInstanceOf(MoneyException.class)
                .hasMessage("Cannot calculate money of different currencies. Tried null with EUR.");
    }

    @Test
    void ensureMoneyHaveSameCurrenciesThanOther() {
        assertThat(oneEuro.hasSameCurrencyThan(hundredEuros)).isTrue();
    }

}

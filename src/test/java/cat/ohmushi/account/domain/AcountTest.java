package cat.ohmushi.account.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cat.ohmushi.account.domain.AccountEvent.TransfertFailed;
import cat.ohmushi.account.domain.AccountException.TransfertException;

public class AcountTest {

    private final static AccountId exampleId = AccountId.of("id").get();
    private final static Money tenEuros = Money.of(10, Currency.EUR).get();
    private final static Money tenDollars = Money.of(10, Currency.USD).get();
    private final static Money zeroEuros = Money.of(0, Currency.EUR).get();
    private static Account accountWithTenEuros = getAccountWithTenEuros();

    private final static RecursiveComparisonConfiguration ignoreDates = RecursiveComparisonConfiguration.builder()
      .withIgnoredFieldsOfTypes(LocalDateTime.class)
      .build();

    private static Account getAccountWithTenEuros() {
        try {
            return Account.create(exampleId, tenEuros, Currency.EUR);
        } catch (AccountException ex) {
            return null;
        }
    }

    @BeforeEach
    void init() {
        accountWithTenEuros = getAccountWithTenEuros();
    }

    @Test
    void shouldCreateAccountWithZeroBalance() {
        assertThatNoException()
                .isThrownBy(() -> Account.create(exampleId, zeroEuros, Currency.EUR));
    }

    @Test
    void shouldNotCreateAccountWithNegativeBalance() {
        final var negativeBalance = Money.of(BigDecimal.valueOf(-1), Currency.EUR).get();
        assertThatThrownBy(() -> Account.create(exampleId, negativeBalance, Currency.EUR))
                .isInstanceOf(AccountException.class)
                .hasMessage("Cannot create an account with a strictly negative balance.");
    }

    @Test
    void shoulDepositMoneyInAccount() {
        accountWithTenEuros.deposit(Money.of(BigDecimal.valueOf(25), Currency.EUR).get(), now());
        assertThat(accountWithTenEuros.balance().amount()).isEqualTo(BigDecimal.valueOf(10 + 25));
    }

    @Test
    void shoulNotDepositZero() {
        accountWithTenEuros.deposit(zeroEuros, now());
        assertThat(accountWithTenEuros.events())
                .usingRecursiveFieldByFieldElementComparator(ignoreDates)
                .hasSize(2)
                .contains(new TransfertFailed(new TransfertException("Money transferred cannot be negative."), now(), tenEuros));
    }

    @Test
    void shoulNotDepositStrictlyNegativeAmount() {
        accountWithTenEuros.deposit(Money.of(BigDecimal.valueOf(-1), Currency.EUR).get(), now());
        assertThat(accountWithTenEuros.events())
                .usingRecursiveFieldByFieldElementComparator(ignoreDates)
                .hasSize(2)
                .contains(new TransfertFailed(new TransfertException("Money transferred cannot be negative."), now(), tenEuros));
    }

    @Test
    void shoulWithdraw() {
        accountWithTenEuros.withdraw(Money.of(BigDecimal.valueOf(25), Currency.EUR).get(), now());
        assertThat(accountWithTenEuros.balance().amount()).isEqualTo(BigDecimal.valueOf(-15));
    }

    @Test
    void shoulNotWithdrawZero() {
        accountWithTenEuros.withdraw(zeroEuros, now());
        assertThat(accountWithTenEuros.events())
                .usingRecursiveFieldByFieldElementComparator(ignoreDates)
                .hasSize(2)
                .contains(new TransfertFailed(new TransfertException("Money transferred cannot be negative."), now(), tenEuros));
    }

    @Test
    void shoulNotWithdrawStrictlyNegativeAmount() {
        accountWithTenEuros.withdraw(Money.of(BigDecimal.valueOf(-1), Currency.EUR).get(), now());
        assertThat(accountWithTenEuros.events())
                .usingRecursiveFieldByFieldElementComparator(ignoreDates)
                .hasSize(2)
                .contains(new TransfertFailed(new TransfertException("Money transferred cannot be negative."), now(), tenEuros));
    }

    @Test
    void createAccountWithNullIdOrBalanceShouldThrowException() {
        assertThatThrownBy(() -> Account.create(null, tenEuros, Currency.EUR))
                .isInstanceOf(AccountException.class)
                .hasMessage("Account cannot have null field.");
    }

    @Test
    void createAccountWithBalanceCurrencyDifferentThantAccountCurrencyShouldThrowException() {
        assertThatThrownBy(() -> Account.create(exampleId, tenEuros, Currency.USD))
                .isInstanceOf(AccountException.class)
                .hasMessage("Cannot create account in USD with EUR initial balance.");
    }

    @Test
    void depositDollarsToEuroAccountShouldThrowExcention() {
        accountWithTenEuros.deposit(tenDollars, now());
        assertThat(accountWithTenEuros.events())
                .usingRecursiveFieldByFieldElementComparator(ignoreDates)
                .hasSize(2)
                .contains(new TransfertFailed(new TransfertException("Cannot transfert USD to EUR account."), now(),tenEuros));
    }

    @Test
    void withdrawDollarsToEuroAccountShouldThrowExcention() {
        accountWithTenEuros.withdraw(tenDollars, now());
        assertThat(accountWithTenEuros.events())
                .usingRecursiveFieldByFieldElementComparator(ignoreDates)
                .hasSize(2)
                .contains(new TransfertFailed(new TransfertException("Cannot transfert USD to EUR account."), now(), tenEuros));
    }
}

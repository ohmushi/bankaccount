package cat.ohmushi.account.domain.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cat.ohmushi.account.domain.exceptions.AccountDomainException;
import cat.ohmushi.account.domain.exceptions.TransfertException;

public class AcountTest {

    private final static AccountId exampleId = AccountId.of("id").get();
    private final static Money tenEuros = Money.of(10, Currency.EUR).get();
    private final static Money tenDollars = Money.of(10, Currency.USD).get();
    private final static Money zeroEuros = Money.of(0, Currency.EUR).get();
    private static Account accountWithTenEuros = getAccountWithTenEuros();

    private static Account getAccountWithTenEuros() {
        try {
            return Account.create(exampleId, tenEuros, Currency.EUR);
        } catch (AccountDomainException ex) {
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
                .isInstanceOf(AccountDomainException.class)
                .hasMessage("Cannot create an account with a strictly negative balance.");
    }

    @Test
    void shoulDepositMoneyInAccount() {
        accountWithTenEuros.deposit(Money.of(BigDecimal.valueOf(25), Currency.EUR).get(), now());
        assertThat(accountWithTenEuros.balance().amount()).isEqualTo(BigDecimal.valueOf(10 + 25));
    }

    @Test
    void shoulNotDepositZero() {
        assertThatThrownBy(() -> accountWithTenEuros.deposit(zeroEuros, now()))
                .isInstanceOf(TransfertException.class)
                .hasMessage("Money transferred cannot be negative.");
    }

    @Test
    void shoulNotDepositStrictlyNegativeAmount() {
        assertThatThrownBy(() -> accountWithTenEuros.deposit(Money.of(BigDecimal.valueOf(-1), Currency.EUR).get(), now()))
                .isInstanceOf(TransfertException.class)
                .hasMessage("Money transferred cannot be negative.");
    }

    @Test
    void shoulWithdraw() {
        accountWithTenEuros.withdraw(Money.of(BigDecimal.valueOf(25), Currency.EUR).get(), now());
        assertThat(accountWithTenEuros.balance().amount()).isEqualTo(BigDecimal.valueOf(-15));
    }

    @Test
    void shoulNotWithdrawZero() {
        assertThatThrownBy(() -> accountWithTenEuros.withdraw(zeroEuros, now()))
                .isInstanceOf(TransfertException.class)
                .hasMessage("Money transferred cannot be negative.");
    }

    @Test
    void shoulNotWithdrawStrictlyNegativeAmount() {
        assertThatThrownBy(() -> accountWithTenEuros.withdraw(Money.of(BigDecimal.valueOf(-1), Currency.EUR).get(), now()))
                .isInstanceOf(TransfertException.class)
                .hasMessage("Money transferred cannot be negative.");
    }

    @Test
    void createAccountWithNullIdOrBalanceShouldThrowException() {
        assertThatThrownBy(() -> Account.create(null, tenEuros, Currency.EUR))
                .isInstanceOf(AccountDomainException.class)
                .hasMessage("Account cannot have null field.");
    }

    @Test
    void createAccountWithBalanceCurrencyDifferentThantAccountCurrencyShouldThrowException() {
        assertThatThrownBy(() -> Account.create(exampleId, tenEuros, Currency.USD))
                .isInstanceOf(AccountDomainException.class)
                .hasMessage("Cannot create account in USD with EUR initial balance.");
    }

    @Test
    void depositDollarsToEuroAccountShouldThrowExcention() {
        assertThatThrownBy(() -> accountWithTenEuros.deposit(tenDollars, now()))
                .isInstanceOf(TransfertException.class)
                .hasMessage("Cannot transfert USD to EUR account.");
    }

    @Test
    void withdrawDollarsToEuroAccountShouldThrowExcention() {
        assertThatThrownBy(() -> accountWithTenEuros.withdraw(tenDollars, now()))
                .isInstanceOf(TransfertException.class)
                .hasMessage("Cannot transfert USD to EUR account.");
    }
}

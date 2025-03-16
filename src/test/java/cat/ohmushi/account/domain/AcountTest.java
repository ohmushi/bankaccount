package cat.ohmushi.account.domain;

import cat.ohmushi.account.domain.AccountException.TransfertException;
import cat.ohmushi.account.domain.Currency.*;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatNoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AcountTest {

    private final static AccountId exampleId = AccountId.of("id").get();
    private final static Money tenEuros = Money.of(10, Currency.EUR).get();
    private final static Money tenDollars = Money.of(10, Currency.USD).get();
    private final static Money zeroEuros = Money.of(0, Currency.EUR).get();
    private static Account accountWithTenEuros = getAccountWithTenEuros();

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
        accountWithTenEuros.deposit(Money.of(BigDecimal.valueOf(25), Currency.EUR).get());
        assertThat(accountWithTenEuros.balance().amount()).isEqualTo(BigDecimal.valueOf(10 + 25));
    }

    @Test
    void shoulNotDepositZero() {
        assertThatThrownBy(() -> accountWithTenEuros.deposit(Money.of(BigDecimal.ZERO, Currency.EUR).get()))
                .isInstanceOf(TransfertException.class)
                .hasMessage("Money transferred cannot be negative.");
    }

    @Test
    void shoulNotDepositStrictlyNegativeAmount() {
        assertThatThrownBy(() -> accountWithTenEuros.deposit(Money.of(BigDecimal.valueOf(-1), Currency.EUR).get()))
                .isInstanceOf(TransfertException.class)
                .hasMessage("Money transferred cannot be negative.");
    }

    @Test
    void shoulWithdraw() {
        accountWithTenEuros.withdraw(Money.of(BigDecimal.valueOf(25), Currency.EUR).get());
        assertThat(accountWithTenEuros.balance().amount()).isEqualTo(BigDecimal.valueOf(-15));
    }

    @Test
    void shoulNotWithdrawZero() {
        assertThatThrownBy(() -> accountWithTenEuros.withdraw(zeroEuros))
                .isInstanceOf(TransfertException.class)
                .hasMessage("Money transferred cannot be negative.");
    }

    @Test
    void shoulNotWithdrawStrictlyNegativeAmount() {
        assertThatThrownBy(() -> accountWithTenEuros.withdraw(Money.of(BigDecimal.valueOf(-1), Currency.EUR).get()))
                .isInstanceOf(TransfertException.class)
                .hasMessage("Money transferred cannot be negative.");
    }

    @Test
    void createAccountWithNullIdOrBalanceShouldThrowException() {
        assertThatThrownBy(() -> Account.create(null, tenEuros, Currency.EUR))
                .isInstanceOf(AccountException.class)
                .hasMessage("Account cannot have null id or balance.");
    }

    @Test
    void depositDollarsToEuroAccountShouldThrowExcention() {
        assertThatThrownBy(() -> accountWithTenEuros.deposit(tenDollars))
                .isInstanceOf(TransfertException.class)
                .hasMessage("Cannot transfert USD to EUR account.");
    }

    @Test
    void withdrawDollarsToEuroAccountShouldThrowExcention() {
        assertThatThrownBy(() -> accountWithTenEuros.withdraw(tenDollars))
                .isInstanceOf(TransfertException.class)
                .hasMessage("Cannot transfert USD to EUR account.");
    }
}

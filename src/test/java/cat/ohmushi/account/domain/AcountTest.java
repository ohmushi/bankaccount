package cat.ohmushi.account.domain;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatNoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AcountTest {

    private final static AccountId exampleId = AccountId.of("id").get();
    private final static Money tenEuros = Money.of(10).get();
    private final static Money zeroEuros = Money.of(0).get();
    private static Account accountWithTenEuros = getAccountWithTenEuros();

    private static Account getAccountWithTenEuros() {
        try {
            return Account.create(exampleId, tenEuros);
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
            .isThrownBy(() -> Account.create(exampleId, zeroEuros));
    }

    @Test
    void shouldNotCreateAccountWithNegativeBalance() {
        final var negativeBalance = Money.of(BigDecimal.valueOf(-1)).get();
        assertThatThrownBy(() -> Account.create(exampleId, negativeBalance))
            .isInstanceOf(AccountException.class)
            .hasMessage("Cannot create an account with a strictly negative balance.");
    }

    @Test
    void shoulDepositMoneyInAccount() {
        accountWithTenEuros.deposit(Money.of(BigDecimal.valueOf(25)).get());
        assertThat(accountWithTenEuros.balance().amount()).isEqualTo(BigDecimal.valueOf(10 + 25));
    }

    @Test
    void shoulNotDepositZero() {
        assertThatThrownBy(() -> accountWithTenEuros.deposit(Money.of(BigDecimal.ZERO).get()))
            .isInstanceOf(AccountException.DepositException.class)
            .hasMessage("Money transferred cannot be negative.");
    }

    @Test
    void shoulNotDepositStrictlyNegativeAmount() {
        assertThatThrownBy(() -> accountWithTenEuros.deposit(Money.of(BigDecimal.valueOf(-1)).get()))
            .isInstanceOf(AccountException.DepositException.class)
            .hasMessage("Money transferred cannot be negative.");
    }

    @Test
    void shoulWithdraw() {
        accountWithTenEuros.withdraw(Money.of(BigDecimal.valueOf(25)).get());
        assertThat(accountWithTenEuros.balance().amount()).isEqualTo(BigDecimal.valueOf(-15));
    }

    @Test
    void shoulNotWithdrawZero() {
        assertThatThrownBy(() -> accountWithTenEuros.withdraw(zeroEuros))
            .isInstanceOf(AccountException.WithdrawException.class)
            .hasMessage("Money transferred cannot be negative.");
    }

    @Test
    void shoulNotWithdrawStrictlyNegativeAmount() {
        assertThatThrownBy(() -> accountWithTenEuros.withdraw(Money.of(BigDecimal.valueOf(-1)).get()))
            .isInstanceOf(AccountException.WithdrawException.class)
            .hasMessage("Money transferred cannot be negative.");
    }

    @Test
    void createAccountWithNullIdOrBalanceShouldThrowException() {
        assertThatThrownBy(() -> Account.create(null, tenEuros))
            .isInstanceOf(AccountException.class)
            .hasMessage("Account cannot have null id or balance.");
    }
    
}

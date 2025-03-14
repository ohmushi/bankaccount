package cat.ohmushi;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cat.ohmushi.account.domain.Account;
import cat.ohmushi.account.domain.AccountException;
import cat.ohmushi.account.domain.AccountId;
import cat.ohmushi.account.domain.Money;

public class AcountTest {

    private final static AccountId exampleId = AccountId.of("id");
    private static Account accountWithTenEuros = getAccountWithTenEuros();

    private static Account getAccountWithTenEuros() {
        try {
            return Account.create(exampleId, Money.of(BigDecimal.valueOf(10)));
        } catch (AccountException ex) {
            return null;
        }
    }

    @BeforeEach
    void init() {
        accountWithTenEuros = getAccountWithTenEuros();
    }

    @Test
    void shouldCreateAccount() {
        assertDoesNotThrow(() -> Account.create(exampleId, Money.of(BigDecimal.valueOf(0))));
    }

    @Test
    void shouldNotCreateAccountWithNegativeBalance() {
        final var negativeBalance = Money.of(BigDecimal.valueOf(-1));
        Exception throwed = assertThrows(AccountException.class, () -> Account.create(exampleId, negativeBalance));
        assertThat(throwed).hasMessage("Cannot create an account with a strictly negative balance.");
    }

    @Test
    void shoulDepositMoneyInAccount() {
        accountWithTenEuros.deposit(Money.of(BigDecimal.valueOf(25)));
        assertThat(accountWithTenEuros.balance().amount()).isEqualTo(BigDecimal.valueOf(10 + 25));
    }
}

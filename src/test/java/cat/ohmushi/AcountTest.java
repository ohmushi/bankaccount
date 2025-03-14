package cat.ohmushi;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

import cat.ohmushi.account.domain.Account;
import cat.ohmushi.account.domain.AccountId;
import cat.ohmushi.account.domain.Money;

public class AcountTest {
    @Test
    void should_create_account() {
        assertDoesNotThrow(() -> Account.create(AccountId.of("id"), Money.of(BigDecimal.valueOf(0))));
    }
}
package cat.ohmushi.account.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import cat.ohmushi.account.domain.account.AccountId;

final class AccountIdTest {
    @Test
    void shouldCreateAccountId() {
        assertThat(AccountId.of("id")).isNotEmpty();
    }

    @Test
    void createAccountIdWithNullValueShouldBeEmpty() {
        assertThat(AccountId.of(null)).isEmpty();
    }

    @Test
    void twoAccountIdWithSameValueShouldBeEqual() {
        assertThat(AccountId.of("id").get()).isEqualTo(AccountId.of("id").get());
    }

    @Test
    void idShouldBeTrimedAtCreation() {
        assertThat(AccountId.of("   id   ").get()).isEqualTo(AccountId.of("id").get());
    }

    @Test
    void idShouldBeTransformedToLowerCaseAtCreation() {
        assertThat(AccountId.of("ID").get()).isEqualTo(AccountId.of("id").get());
    }
}

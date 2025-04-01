package cat.ohmushi.account.application.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cat.ohmushi.account.application.exceptions.AccountApplicationException;
import cat.ohmushi.account.application.exceptions.AccountApplicationException.AccountNotFoundException;
import cat.ohmushi.account.application.exceptions.AccountApplicationException.AccountTransfertException;
import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.account.domain.account.AccountId;
import cat.ohmushi.account.domain.account.AccountStatement;
import static cat.ohmushi.account.domain.account.Currency.EUR;
import cat.ohmushi.account.domain.account.Money;
import cat.ohmushi.account.domain.events.AccountCreated;
import cat.ohmushi.account.domain.events.AccountEvent;
import cat.ohmushi.account.domain.events.MoneyDepositedInAccount;
import cat.ohmushi.account.domain.events.MoneyWithdrawnFromAccount;
import cat.ohmushi.account.domain.events.TransfertFailed;
import cat.ohmushi.account.domain.repositories.Accounts;

public class AccountServiceTest {

    private final static class MockAccountRepository implements Accounts {

        private List<Account> accounts = new ArrayList<>();

        public void setAccounts(List<Account> accounts) {
            this.accounts = accounts;
        }

        @Override
        public Optional<Account> findAccountById(AccountId id) {
            return accounts.stream().filter(a -> a.id().equals(id)).findAny();
        }

        @Override
        public void save(Account account) {
            if (accounts.contains(account)) {
                accounts.remove(account);
            }
            accounts.add(account);
        }
    }

    private MockAccountRepository repository;
    private AccountService service;
    private final AccountId myAccountId = AccountId.of("111").get();
    private final Account accountWithTenEuros = Account.create(
            myAccountId,
            Money.of(10, EUR).get(),
            EUR);

    @BeforeEach
    void init() {
        repository = new MockAccountRepository();
        repository.setAccounts(new ArrayList<>(List.of(accountWithTenEuros)));
        service = new AccountService(repository);
    }

    @Test
    void shouldInstanciateAccountService() {
        assertDoesNotThrow(() -> new AccountService(repository));
    }

    @Test
    void shouldDepositTenEurosInMyAccount() {
        service.deposit(myAccountId.toString(), BigDecimal.TEN);
        assertThat(repository.accounts.getFirst().balance().amount())
                .isEqualTo(BigDecimal.valueOf(20));
    }

    @Test
    void shouldNotBeAbleToDepositNegativeAmount() {
        assertThatThrownBy(() -> service.deposit("111", BigDecimal.valueOf(-1)))
                .isInstanceOf(AccountTransfertException.class);
        assertThat(repository.accounts.getFirst().lastAppendEvent())
                .isInstanceOf(TransfertFailed.class);
    }

    @Test
    void shouldNotBeAbleToDepositNullAmount() {
        assertThatThrownBy(() -> service.deposit("111", null))
                .isInstanceOf(AccountApplicationException.class);
    }

    @Test
    void shouldNotBeAbleToDepositInNullAccount() {
        assertThatThrownBy(() -> service.deposit(null, BigDecimal.TEN))
                .isInstanceOf(AccountApplicationException.class);
    }

    @Test
    void depositInUnkownAccountShouldThrowNotFoundException() {
        assertThatThrownBy(() -> service.deposit("222", BigDecimal.TEN))
                .isInstanceOf(AccountNotFoundException.class);
    }

    //
    @Test
    void shouldWithdrawTenEurosInMyAccount() {
        service.withdraw(myAccountId.toString(), BigDecimal.TWO);
        assertThat(repository.accounts.getFirst().balance().amount())
                .isEqualTo(BigDecimal.valueOf(8));
    }

    @Test
    void shouldNotBeAbleToWithdrawNegativeAmount() {
        assertThatThrownBy(() -> service.withdraw("111", BigDecimal.valueOf(-1)))
                .isInstanceOf(AccountTransfertException.class);
        assertThat(repository.accounts.getFirst().lastAppendEvent())
                .isInstanceOf(TransfertFailed.class);
    }

    @Test
    void shouldNotBeAbleToWithdrawNullAmount() {
        assertThatThrownBy(() -> service.withdraw("111", null))
                .isInstanceOf(AccountApplicationException.class);
    }

    @Test
    void shouldNotBeAbleToWithdrawInNullAccount() {
        assertThatThrownBy(() -> service.withdraw(null, BigDecimal.TEN))
                .isInstanceOf(AccountApplicationException.class);
    }

    @Test
    void withdrawInUnkownAccountShouldThrowNotFoundException() {
        assertThatThrownBy(() -> service.withdraw("222", BigDecimal.TEN))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void shouldGetStatement() {
        List<AccountEvent> history = List.of(
                new AccountCreated(AccountId.of("111").get(), Money.of(0, EUR).get(), EUR, Instant.now()),
                new MoneyDepositedInAccount(Money.of(10, EUR).get(), Instant.now()),
                new MoneyWithdrawnFromAccount(Money.of(5, EUR).get(), Instant.now())
        );
        var account = Account.fromHistory(history);

        repository.setAccounts(List.of(account));

        assertThat(service.getStatement("111").lines())
                .isEqualTo(AccountStatement.forAccount(account).lines());

    }
}

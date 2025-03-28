package cat.ohmushi.account.domain.repositories;

import java.util.Optional;

import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.account.domain.account.AccountId;

public interface Accounts {

    Optional<Account> findAccountById(AccountId id);

    void save(Account account);
}

package cat.ohmushi.account.domain.repositories;

import java.util.Optional;

import cat.ohmushi.account.domain.models.Account;
import cat.ohmushi.account.domain.models.AccountId;

public interface Accounts {

    Optional<Account> findAccountById(AccountId id);

    void save(Account account);
}

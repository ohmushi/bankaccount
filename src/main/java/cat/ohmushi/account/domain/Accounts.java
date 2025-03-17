package cat.ohmushi.account.domain;

import java.util.Optional;

public interface Accounts {
    Optional<Account> findAccountByItsId(AccountId id);

    void save(Account account);
}

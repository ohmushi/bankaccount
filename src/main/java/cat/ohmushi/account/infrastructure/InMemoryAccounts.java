package cat.ohmushi.account.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cat.ohmushi.account.domain.Account;
import cat.ohmushi.account.domain.AccountId;
import cat.ohmushi.account.domain.Accounts;

public class InMemoryAccounts implements Accounts {

    private final List<Account> accounts;

    public InMemoryAccounts(List<Account> accounts) {
        this.accounts = new ArrayList<>(accounts);
    }

    @Override
    public Optional<Account> findAccountByItsId(AccountId id) {
        return this.accounts.stream().filter(a -> a.id().equals(id)).findAny();
    }

    @Override
    public void save(Account account) {
        this.accounts.add(account);
    }
    
}

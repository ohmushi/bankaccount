package cat.ohmushi.account.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.account.domain.account.AccountId;
import cat.ohmushi.account.domain.repositories.Accounts;

public class InMemoryAccounts implements Accounts {

    private final List<Account> accounts;

    public InMemoryAccounts(List<Account> accounts) {
        this.accounts = new ArrayList<>(accounts);
    }

    @Override
    public Optional<Account> findAccountById(AccountId id) {
        return this.accounts.stream().filter(a -> a.id().equals(id)).findAny();
    }

    @Override
    public void save(Account account) {
        var existing = this.findAccountById(account.id());
        if (existing.isPresent()) {
            this.accounts.removeIf(a -> a.id().equals(account.id()));
        }
        this.accounts.add(account);
    }

}

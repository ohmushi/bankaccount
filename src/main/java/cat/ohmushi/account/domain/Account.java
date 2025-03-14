package cat.ohmushi.account.domain;

import javax.security.auth.login.AccountException;

@DomainEntity
public class Account {

    private final AccountId id;
    private final Money balance;

    private Account(AccountId id, Money balance) {
        this.id = id;
        this.balance = balance;
    }

    public static Account create(AccountId id, Money balance) throws AccountException {
        if(!balance.isStrictlyPositive()) {
            throw new AccountException("Cannot create an account with a strictly negative balance.");
        }
        return new Account(id, balance);
    }
}

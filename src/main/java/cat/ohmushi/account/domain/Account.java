package cat.ohmushi.account.domain;

import cat.ohmushi.account.domain.AccountException.DepositException;

@DomainEntity
public class Account {

    private final AccountId id;
    private Money balance;

    private Account(AccountId id, Money balance) {
        this.id = id;
        this.balance = balance;
    }

    public static Account create(AccountId id, Money balance) throws AccountException {
        if(!balance.isZeroOrPositive()) {
            throw new AccountException("Cannot create an account with a strictly negative balance.");
        }
        return new Account(id, balance);
    }

    public Money balance() {
        return this.balance;
    }

    public void deposit(Money amount) throws DepositException {
        if(amount.isZeroOrPositive()) {
            this.balance = this.balance.add(amount);
        } else {
            throw new DepositException("Money transferred cannot be negative.");
        }
    }
}

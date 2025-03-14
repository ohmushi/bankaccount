package cat.ohmushi.account.domain;

@DomainEntity
public class Account {

    private final AccountId id;
    private Money balance;

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

    public Money balance() {
        return this.balance;
    }

    public void deposit(Money amount) {
        this.balance = this.balance.add(amount);
    }
}

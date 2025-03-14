package cat.ohmushi.account.domain;

@DomainEntity
public class Account {

    private final AccountId id;
    private final Money balance;

    private Account(AccountId id, Money balance) {
        this.id = id;
        this.balance = balance;
    }

    public static Account create(AccountId id, Money balance) {
        return new Account(id, balance);
    }
}

package cat.ohmushi.account.domain;

import java.util.Objects;
import java.util.Optional;

import cat.ohmushi.account.domain.AccountException.DepositException;
import cat.ohmushi.account.domain.AccountException.WithdrawException;
import cat.ohmushi.shared.annotations.DomainEntity;

@DomainEntity
public class Account {

    private final AccountId id;
    private Money balance;

    private Account(AccountId id, Money balance) throws AccountException {
        try {
            this.id = Objects.requireNonNull(id);
            this.balance = Objects.requireNonNull(balance);
        } catch(NullPointerException e) {
            throw new AccountException("Account cannot have null id or balance.");
        }
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
        if(!amount.isStrictlyPositive()) {
            throw AccountException.deposit("Money transferred cannot be negative.");
        }

        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) throws WithdrawException {
        if(!amount.isStrictlyPositive()) {
            throw AccountException.withdrawal("Money transferred cannot be negative.");
        }

        this.balance = this.balance.minus(amount);
    }
}

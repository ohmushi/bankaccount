package cat.ohmushi.account.application.usecases;

import java.math.BigDecimal;

import cat.ohmushi.account.application.exceptions.AccountApplicationException;

@FunctionalInterface
public interface WithdrawMoneyFromAccount {
    void withdraw(String accountId, BigDecimal amount) throws AccountApplicationException;
}

package cat.ohmushi.account.usecases;

import java.math.BigDecimal;

@FunctionalInterface
public interface WithdrawMoneyFromAccount {
    void withdraw(String accountId, BigDecimal amount) throws AccountApplicationException;
}

package cat.ohmushi.account.usecases;

import java.math.BigDecimal;

public interface DepositMoneyInAccount {
    void deposit(String accountId, BigDecimal amount) throws AccountApplicationException;
}

package cat.ohmushi.account.domain;

import cat.ohmushi.shared.annotations.DomainException;

@DomainException
public class AccountException extends RuntimeException {
    AccountException(String msg) {
        super(msg);
    }

    public static DepositException deposit(String msg) {
        return new DepositException(msg);
    }

    public static WithdrawException withdrawal(String msg) {
        return new WithdrawException(msg);
    }

    public static class DepositException extends AccountException {
        public DepositException(String msg) {
            super(msg);
        }
    }

    public static class WithdrawException extends AccountException {
        public WithdrawException(String msg) {
            super(msg);
        }
    }
}

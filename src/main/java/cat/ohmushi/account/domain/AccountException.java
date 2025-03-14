package cat.ohmushi.account.domain;

@DomainException
public class AccountException extends RuntimeException {
    AccountException(String msg) {
        super(msg);
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

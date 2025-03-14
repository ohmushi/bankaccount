package cat.ohmushi.account.domain;

@DomainException
public class AccountException extends RuntimeException {
    AccountException(String msg) {
        super(msg);
    }

    static class DepositException extends AccountException {
        public DepositException(String msg) {
            super(msg);
        }
    }
}

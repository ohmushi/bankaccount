package cat.ohmushi.account.domain;

import cat.ohmushi.shared.annotations.DomainException;

@DomainException
public class AccountException extends RuntimeException {
    AccountException(String msg) {
        super(msg);
    }

    public static TransfertException transfert(String msg) {
        return new TransfertException(msg);
    }

    public static class TransfertException extends AccountException {
        public TransfertException(String msg) {
            super(msg);
        }
    }
}

package cat.ohmushi.account.domain;

import cat.ohmushi.shared.annotations.DomainException;

@DomainException
public class AccountDomainException extends RuntimeException {
    AccountDomainException(String msg) {
        super(msg);
    }

    public final static TransfertException transfert(String msg) {
        return new TransfertException(msg);
    }

    public final static class TransfertException extends AccountDomainException {
        public TransfertException(String msg) {
            super(msg);
        }
    }
}

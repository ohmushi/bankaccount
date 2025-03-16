package cat.ohmushi.account.domain;

import cat.ohmushi.shared.annotations.DomainException;

@DomainException
class AccountException extends RuntimeException {
    AccountException(String msg) {
        super(msg);
    }

    public final static TransfertException transfert(String msg) {
        return new TransfertException(msg);
    }

    public final static class TransfertException extends AccountException {
        public TransfertException(String msg) {
            super(msg);
        }
    }
}

package cat.ohmushi.account.domain.exceptions;

import cat.ohmushi.shared.annotations.DomainException;

@DomainException
public class AccountDomainException extends RuntimeException {
    public AccountDomainException(String msg) {
        super(msg);
    }

    public final static TransfertException transfert(String msg) {
        return new TransfertException(msg);
    }
}

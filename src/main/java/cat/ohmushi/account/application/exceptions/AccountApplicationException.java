package cat.ohmushi.account.application.exceptions;

import cat.ohmushi.account.domain.account.AccountId;
import cat.ohmushi.account.domain.exceptions.AccountDomainException;

public class AccountApplicationException extends RuntimeException{
    public AccountApplicationException(String msg) {
        super(msg);
    }

    public static class AccountNotFoundException extends AccountApplicationException {
        public AccountNotFoundException(AccountId id) {
            super("Account '" + id.toString() + "' was not found.");
        }
    }

    public static class AccountTransfertException extends AccountApplicationException {
        public AccountTransfertException(AccountDomainException cause) {
            super("Transfert money for account failed : " + cause.getMessage());
        }
    }
}

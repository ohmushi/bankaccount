package cat.ohmushi.account.application.exceptions;

import cat.ohmushi.account.domain.models.AccountId;

public class AccountApplicationException extends RuntimeException{
    public AccountApplicationException(String msg) {
        super(msg);
    }

    public static class AccountNotFoundException extends AccountApplicationException {
        public AccountNotFoundException(AccountId id) {
            super("Account '" + id.toString() + "' was not found.");
        }
    }
}

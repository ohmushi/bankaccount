package cat.ohmushi.account.domain;

import cat.ohmushi.shared.annotations.DomainException;

@DomainException
public class MoneyException extends RuntimeException {
    public MoneyException(String msg) {
        super(msg);
    }

    public static CreationMoneyException creation(String msg) {
        return new CreationMoneyException(msg);
    }

    public static class CreationMoneyException extends MoneyException {
        public CreationMoneyException(String  msg) {
            super(msg);
        }
    }

}

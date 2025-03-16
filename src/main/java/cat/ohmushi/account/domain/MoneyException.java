package cat.ohmushi.account.domain;

import cat.ohmushi.shared.annotations.DomainException;

@DomainException
class MoneyException extends RuntimeException {
    public MoneyException(String msg) {
        super(msg);
    }

    public final static CreationMoneyException creation(String msg) {
        return new CreationMoneyException(msg);
    }

    public final static class CreationMoneyException extends MoneyException {
        public CreationMoneyException(String msg) {
            super(msg);
        }
    }

}

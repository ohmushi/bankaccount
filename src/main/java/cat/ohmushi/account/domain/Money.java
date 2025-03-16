package cat.ohmushi.account.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import cat.ohmushi.account.domain.MoneyException.CreationMoneyException;
import cat.ohmushi.shared.annotations.Value;

@Value
final class Money {

    private final BigDecimal amount;
    private final Currency currency;

    private Money(BigDecimal amount, Currency currency) throws CreationMoneyException {
        try {
            this.amount = Objects.requireNonNull(amount);
            this.currency = Objects.requireNonNull(currency);
        } catch (NullPointerException e) {
            throw MoneyException.creation("Cannot create Money with null value.");
        }
    }

    public static Optional<Money> of(BigDecimal amount, Currency currency){
        try {
            return Optional.of(new Money(amount, currency));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Money> of(int amount, Currency currency) {
        return Money.of(BigDecimal.valueOf(amount), currency);
    }

    public BigDecimal amount() {
        return this.amount;
    }

    public boolean isZeroOrPositive() {
        return this.amount.intValue() >= 0;
    }

    public boolean isStrictlyPositive() {
        return this.amount.intValue() > 0;
    }

    public Money add(Money added) {
        // TODO check same currency
        return new Money(this.amount.add(added.amount), this.currency);
    }

    public Money minus(Money subtracted) {
        // TODO check same currency
        return new Money(this.amount.subtract(subtracted.amount), this.currency);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Money other = (Money) obj;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (currency != other.currency)
            return false;
        return true;
    }
}

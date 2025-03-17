package cat.ohmushi.account.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import cat.ohmushi.account.domain.MoneyException.CreationMoneyException;
import cat.ohmushi.shared.OptionalUtils;
import cat.ohmushi.shared.annotations.Value;

@Value
public final class Money {

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

    public static Optional<Money> of(BigDecimal amount, Currency currency) {
        return OptionalUtils.ofThrowable(() -> new Money(amount, currency));
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

    public Money add(Money added) throws MoneyException {
        this.ensureHasSameCurrency(added);
        return new Money(this.amount.add(added.amount), this.currency);
    }

    public Money minus(Money subtracted) throws MoneyException {
        this.ensureHasSameCurrency(subtracted);
        return new Money(this.amount.subtract(subtracted.amount), this.currency);
    }

    private void ensureHasSameCurrency(Money other) throws MoneyException {
        if (!this.hasSameCurrencyThan(other)) {
            var addedCurrency = Optional.ofNullable(other).map(a -> a.currency.toString()).orElse(null);
            throw new MoneyException("Cannot calculate money of different currencies. " +
                    "Tried " + addedCurrency + " with " + this.currency + ".");
        }
    }

    public boolean hasSameCurrencyThan(Money other) {
        return Optional.ofNullable(other)
                .map(o -> this.currency.equals(o.currency))
                .orElse(false);
    }

    public Currency currency() {
        return this.currency;
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

    @Override
    public String toString() {
        return amount + " " + currency;
    }

}

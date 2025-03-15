package cat.ohmushi.account.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import cat.ohmushi.account.domain.MoneyException.CreationMoneyException;
import cat.ohmushi.shared.annotations.Value;

@Value
public class Money {

    private final BigDecimal amount;

    private Money(BigDecimal amount) throws CreationMoneyException {
        try {
            this.amount = Objects.requireNonNull(amount);
        } catch (NullPointerException e) {
            throw MoneyException.creation("Cannot create Money with null value.");
        }
    }

    public static Optional<Money> of(BigDecimal amount){
        try {
            return Optional.of(new Money(amount));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Money> of(int amount) {
        return Money.of(BigDecimal.valueOf(amount));
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
        return new Money(this.amount.add(added.amount()));
    }

    public Money minus(Money amount) {
        return new Money(this.amount.subtract(amount.amount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
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
        return true;
    }

    

}

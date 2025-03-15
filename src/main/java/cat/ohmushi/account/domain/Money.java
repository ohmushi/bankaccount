package cat.ohmushi.account.domain;

import java.math.BigDecimal;
import java.util.Objects;

import cat.ohmushi.shared.annotations.Value;

@Value
public class Money {

    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = Objects.requireNonNull(amount);
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money of(int amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public BigDecimal amount() {
        return this.amount;
    }

    boolean isZeroOrPositive() {
        return this.amount.intValue() >= 0;
    }

    boolean isStrictlyPositive() {
        return this.amount.intValue() > 0;
    }

    public Money add(Money added) {
        return Money.of(this.amount.add(added.amount()));
    }

    public Money minus(Money amount) {
        return Money.of(this.amount.subtract(amount.amount()));
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

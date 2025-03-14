package cat.ohmushi.account.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = Objects.requireNonNull(amount);
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public BigDecimal amount() {
        return this.amount;
    }

    boolean isZeroOrPositive() {
        return this.amount.intValue() >= 0;
    }

    public Money add(Money added) {
        return Money.of(this.amount.add(added.amount()));
    }

}

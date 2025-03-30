package cat.ohmushi.account.domain.account;

import java.time.LocalDateTime;
import java.util.Optional;

import cat.ohmushi.account.domain.events.AccountEvent;
import cat.ohmushi.account.domain.events.MoneyDepositedInAccount;
import cat.ohmushi.account.domain.events.MoneyWithdrawnFromAccount;
import cat.ohmushi.account.domain.events.TransfertFailed;
import cat.ohmushi.account.domain.exceptions.AccountDomainException;
import cat.ohmushi.shared.annotations.Value;

@Value
public record AccountStatementLine(
        LocalDateTime date,
        String operation,
        Money amount,
        Money balance,
        AccountDomainException error) {

    public static Optional<AccountStatementLine> fromAccountState(Account account) {
        return fromEventAndBalance(
                account.lastAppendEvent(),
                account.balance());
    }

    private static Optional<AccountStatementLine> fromEventAndBalance(AccountEvent event, Money balance) {
        var zero = Money.of(0, balance.currency()).get();

        var line = new AccountStatementLine(
                event.getDate(),
                getOperationName(event),
                getAmountFromEvent(event).orElse(zero),
                balance,
                getError(event).orElse(null));

        return Optional.of(line);
    }

    private static Optional<AccountDomainException> getError(AccountEvent event) {
        return switch (event) {
            case TransfertFailed failed -> Optional.of(failed.reason());
            default -> Optional.empty();
        };
    }

    private static String getOperationName(AccountEvent event) {
        return switch (event) {
            case MoneyDepositedInAccount d -> "Deposit";
            case MoneyWithdrawnFromAccount w -> "Withdrawal";
            default -> "";
        };
    }

    private static Optional<Money> getAmountFromEvent(AccountEvent event) {
        return switch (event) {
            case MoneyDepositedInAccount d -> Optional.of(d.deposited());
            case MoneyWithdrawnFromAccount w -> Optional.of(w.withdrawn());
            default -> Optional.empty();
        };
    }
}

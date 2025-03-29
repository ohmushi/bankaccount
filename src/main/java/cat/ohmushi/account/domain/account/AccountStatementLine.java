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
        Optional<Money> amount = switch (event) {
            case MoneyDepositedInAccount d -> Optional.of(d.deposited());
            case MoneyWithdrawnFromAccount w -> Optional.of(w.withdrawn());
            default -> Optional.empty();
        };

        AccountDomainException error = switch (event) {
            case TransfertFailed failed ->
                failed.reason();
            default ->
                null;
        };

        String operation = switch (event) {
            case MoneyDepositedInAccount d ->
                "Deposit";
            case MoneyWithdrawnFromAccount w ->
                "Withdrawal";
            default ->
                "";
        };

        var line = new AccountStatementLine(
                event.getDate(),
                operation,
                amount.orElse(Money.of(0, balance.currency()).get()),
                balance,
                error);
        return Optional.of(line);
    }
}

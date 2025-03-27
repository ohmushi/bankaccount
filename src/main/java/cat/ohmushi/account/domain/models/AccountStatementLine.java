package cat.ohmushi.account.domain.models;

import java.time.LocalDateTime;
import java.util.Optional;

import cat.ohmushi.account.domain.events.AccountEvent;
import cat.ohmushi.account.domain.events.AccountEvent.AccountCreated;
import cat.ohmushi.account.domain.events.AccountEvent.MoneyDepositedInAccount;
import cat.ohmushi.account.domain.events.AccountEvent.MoneyWithdrawnFromAccount;
import cat.ohmushi.account.domain.events.AccountEvent.TransfertFailed;
import cat.ohmushi.account.domain.exceptions.AccountDomainException;
import cat.ohmushi.shared.annotations.Value;

@Value
public record AccountStatementLine(
        LocalDateTime date,
        String operation,
        Money amount,
        Money balance,
        AccountDomainException error) {

    public static Optional<AccountStatementLine> fromAccountEvent(AccountEvent e) {
        Money balance = e.newBalance();
        Optional<Money> amount = switch (e) {
            case AccountCreated c ->
                Optional.empty();
            case MoneyDepositedInAccount d ->
                Optional.of(d.deposited());
            case MoneyWithdrawnFromAccount w ->
                Optional.of(w.withdrawn());
            default ->
                Optional.empty();
        };
        if (amount.isEmpty()) {
            return Optional.empty();
        }

        AccountDomainException error = switch (e) {
            case TransfertFailed failed ->
                failed.reason();
            default ->
                null;
        };

        String operation = switch (e) {
            case MoneyDepositedInAccount d ->
                "Deposit";
            case MoneyWithdrawnFromAccount w ->
                "Withdrawal";
            default ->
                "";
        };

        var line = new AccountStatementLine(
                e.getDate(),
                operation,
                amount.orElse(Money.of(0, balance.currency()).get()),
                balance,
                error);
        return Optional.of(line);
    }
}

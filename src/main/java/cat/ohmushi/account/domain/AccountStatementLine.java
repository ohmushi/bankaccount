package cat.ohmushi.account.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import cat.ohmushi.account.domain.AccountEvent.AccountCreated;
import cat.ohmushi.account.domain.AccountEvent.MoneyDepositedInAccount;
import cat.ohmushi.account.domain.AccountEvent.MoneyWithdrawnFromAccount;
import cat.ohmushi.account.domain.AccountEvent.TransfertFailed;
import cat.ohmushi.shared.annotations.Value;

@Value
record AccountStatementLine(
        LocalDateTime date,
        String operation,
        Money amount,
        Money balance,
        AccountException error) {

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

        AccountException error = switch (e) {
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
                error
        );
        return Optional.of(line);
    }

    public static List<String> format(AccountStatementLine l) {
        String date = String.format("%02d/%02d/%4d", l.date.getMonthValue(), l.date().getDayOfMonth(), l.date().getYear());
        String amountSign = switch (l.operation) {
            case "Deposit" ->
                "+";
            case "Withdrawal" ->
                "-";
            default ->
                "";
        };

        String balanceSign = switch (l.balance.amount().signum()) {
            case -1 ->
                "-";
            default ->
                "";
        };
        String amount = String.format("%s€%d", amountSign, l.amount.amount().abs().intValue());
        String balance = String.format("%s€%d", balanceSign, l.balance.amount().abs().intValue());
        return List.of(date, l.operation, amount, balance);
    }
}

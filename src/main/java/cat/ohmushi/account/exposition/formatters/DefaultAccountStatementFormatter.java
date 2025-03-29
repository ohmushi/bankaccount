package cat.ohmushi.account.exposition.formatters;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cat.ohmushi.account.domain.account.AccountStatement;
import cat.ohmushi.account.domain.account.AccountStatementLine;
import cat.ohmushi.account.domain.account.Currency;

public class DefaultAccountStatementFormatter implements AccountStatementFormatter {

    @Override
    public String format(AccountStatement statement) {
        var head = Stream.of(List.of("Date", "Operation", "Amount", "Balance"))
                .map(this::addPaddingBetweenValues)
                .map(this::joinLineValues);

        var lines = statement.lines()
                .stream()
                .filter(line -> !line.operation().isBlank())
                .map(this::formatLineValues)
                .map(this::addPaddingBetweenValues)
                .map(this::joinLineValues);

        return Stream.concat(head, lines).collect(Collectors.joining("\n"));
    }

    private List<String> formatLineValues(AccountStatementLine l) {
        String date = String.format("%02d/%02d/%4d", l.date().getMonthValue(), l.date().getDayOfMonth(),
                l.date().getYear());
        String amountSign = switch (l.operation()) {
            case "Deposit" ->
                "+";
            case "Withdrawal" ->
                "-";
            default ->
                "";
        };

        String balanceSign = switch (l.balance().amount().signum()) {
            case -1 ->
                "-";
            default ->
                "";
        };

        String currency = switch (l.balance().currency()) {
            case Currency.EUR -> "€";
            case Currency.USD -> "$";
            default -> "";
        };

        String amount = amountSign + currency + l.amount().amount().abs().intValue();
        String balance = balanceSign + currency + l.balance().amount().abs().intValue();
        return List.of(date, l.operation(), amount, balance);
    }

    private List<String> addPaddingBetweenValues(List<String> lineValues) {
        return lineValues.stream().map(w -> String.format("%-15s", w)).toList();
    }

    private String joinLineValues(List<String> lineValues) {
        return String.join("", lineValues);
    }

}

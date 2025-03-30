package cat.ohmushi.account.exposition.formatters;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cat.ohmushi.account.domain.account.AccountStatement;
import cat.ohmushi.account.domain.account.AccountStatementLine;
import cat.ohmushi.account.domain.account.Currency;

public class DefaultAccountStatementFormatter implements AccountStatementFormatter {

    private static int columnWidth = 15;

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
        String date = formatDate(l);
        String amountSign = getAmountSign(l);
        String balanceSign = getBalanceSign(l);
        String currency = getCurrency(l);

        String amount = amountSign + currency + l.amount().amount().abs().intValue();
        String balance = balanceSign + currency + l.balance().amount().abs().intValue();

        return List.of(date, l.operation(), amount, balance);
    }

    private String formatDate(AccountStatementLine l) {
        return String.format("%02d/%02d/%4d", l.date().getMonthValue(), l.date().getDayOfMonth(),
                l.date().getYear());
    }

    private String getCurrency(AccountStatementLine l) {
        return switch (l.balance().currency()) {
            case Currency.EUR -> "€";
            case Currency.USD -> "$";
            default -> "";
        };
    }

    private String getBalanceSign(AccountStatementLine l) {
        return switch (l.balance().amount().signum()) {
            case -1 ->
                "-";
            default ->
                "";
        };
    }

    private String getAmountSign(AccountStatementLine l) {
        return switch (l.operation()) {
            case "Deposit" ->
                "+";
            case "Withdrawal" ->
                "-";
            default ->
                "";
        };
    }

    private List<String> addPaddingBetweenValues(List<String> lineValues) {
        return lineValues.stream().map(w -> String.format("%-" + columnWidth + "s", w)).toList();
    }

    private String joinLineValues(List<String> lineValues) {
        return String.join("", lineValues);
    }

}

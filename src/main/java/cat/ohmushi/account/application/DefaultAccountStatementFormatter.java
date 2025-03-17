package cat.ohmushi.account.application;

import cat.ohmushi.account.domain.AccountStatement;

public class DefaultAccountStatementFormatter implements AccountStatementFormatter{

    @Override
    public String format(AccountStatement statement) {
        return statement
            .lines()
            .map(line -> line.stream().map(w -> String.format("%-15s", w)).toList())
            .map(line -> String.join("", line));
    }

    
}

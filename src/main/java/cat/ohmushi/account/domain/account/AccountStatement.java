package cat.ohmushi.account.domain.account;

import java.util.List;
import java.util.Optional;

public class AccountStatement {

    private final Account account;

    private AccountStatement(Account account) {
        this.account = account;
    }

    public static AccountStatement forAccount(Account account) {
        return new AccountStatement(account);
    }

    public List<AccountStatementLine> lines() {
        return this.account.events()
                .stream()
                .map(AccountStatementLine::fromAccountEvent)
                .filter(Optional::isPresent).map(Optional::get)
                .toList();
    }
}

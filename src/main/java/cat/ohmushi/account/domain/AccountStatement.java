package cat.ohmushi.account.domain;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    public List<List<String>> formatted() {
        var head = Stream.of(
                List.of("Date", "Operation", "Amount", "Balance"));
        var body = this.lines().stream().map(AccountStatementLine::format);
        return Stream.concat(head, body).toList();
    }
}

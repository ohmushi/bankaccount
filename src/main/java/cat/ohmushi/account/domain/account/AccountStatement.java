package cat.ohmushi.account.domain.account;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
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
        return this.historyOfAccountStates()
                .map(AccountStatementLine::fromAccountState)
                .flatMap(Optional::stream) // ignore empty values
                .toList();
    }

    private Stream<Account> historyOfAccountStates() {
        return IntStream
                .rangeClosed(1, account.history().size())
                .mapToObj(i -> account.history().subList(0, i)) // [ [created], [created, deposit], ... ]
                .map(Account::fromHistory);
    }
}

package cat.ohmushi;

import java.math.BigDecimal;
import java.util.List;

import cat.ohmushi.account.application.services.AccountService;
import cat.ohmushi.account.application.usecases.DepositMoneyInAccount;
import cat.ohmushi.account.application.usecases.GetStatementOfAccount;
import cat.ohmushi.account.application.usecases.WithdrawMoneyFromAccount;
import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.account.domain.account.AccountId;
import cat.ohmushi.account.domain.account.Currency;
import cat.ohmushi.account.domain.account.Money;
import cat.ohmushi.account.exposition.formatters.AccountStatementFormatter;
import cat.ohmushi.account.exposition.formatters.DefaultAccountStatementFormatter;
import cat.ohmushi.account.infrastructure.persistence.InMemoryAccounts;

public class App {
    public static void main(String[] arg) {
        final var inMemoryAccountRepository = new InMemoryAccounts(
                List.of(Account.create(
                        AccountId.of("myIBAN").get(),
                        Money.of(500, Currency.EUR).get(),
                        Currency.EUR)));

        AccountService accountService = new AccountService(inMemoryAccountRepository);
        DepositMoneyInAccount depositMoneyInAccount = accountService;
        WithdrawMoneyFromAccount withdrawMoneyFromAccount = accountService;
        GetStatementOfAccount getStatementOfAccount = accountService;
        AccountStatementFormatter formatter = new DefaultAccountStatementFormatter();

        depositMoneyInAccount.deposit("myIBAN", BigDecimal.valueOf(1000));
        withdrawMoneyFromAccount.withdraw("myIBAN", BigDecimal.valueOf(20));

        System.out.println(formatter.format(getStatementOfAccount.getStatement("myIBAN")));
    }
}

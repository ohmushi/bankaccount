package cat.ohmushi;

import java.math.BigDecimal;
import java.util.List;

import cat.ohmushi.account.application.services.AccountService;
import cat.ohmushi.account.application.services.AccountStatementFormatter;
import cat.ohmushi.account.application.usecases.DepositMoneyInAccount;
import cat.ohmushi.account.application.usecases.GetStatementOfAccount;
import cat.ohmushi.account.application.usecases.WithdrawMoneyFromAccount;
import cat.ohmushi.account.domain.models.Account;
import cat.ohmushi.account.domain.models.AccountId;
import cat.ohmushi.account.domain.models.Currency;
import cat.ohmushi.account.domain.models.Money;
import cat.ohmushi.account.exposition.formatters.DefaultAccountStatementFormatter;
import cat.ohmushi.account.exposition.rest.AccountController;
import cat.ohmushi.account.infrastructure.persistence.InMemoryAccounts;

public class App {

    public static void main(String[] arg) {
        AccountService accountService = new AccountService(
                new InMemoryAccounts(List.of(
                        Account.create(
                                AccountId.of("myAccount").get(),
                                Money.of(500, Currency.EUR).get(),
                                Currency.EUR
                        )
                )));

        DepositMoneyInAccount depositMoneyInAccount = accountService;
        WithdrawMoneyFromAccount withdrawMoneyFromAccount = accountService;
        GetStatementOfAccount getStatementOfAccount = accountService;
        AccountStatementFormatter formatter = new DefaultAccountStatementFormatter();

        depositMoneyInAccount.deposit("myAccount", BigDecimal.valueOf(1000));
        withdrawMoneyFromAccount.withdraw("myAccount", BigDecimal.valueOf(20));

        AccountController controller = new AccountController(
                depositMoneyInAccount,
                withdrawMoneyFromAccount,
                getStatementOfAccount,
                formatter
        );

        // TODO controller.deposit()
    }
}

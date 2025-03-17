package cat.ohmushi;

import java.math.BigDecimal;
import java.util.List;

import cat.ohmushi.account.application.AccountService;
import cat.ohmushi.account.application.DefaultAccountStatementFormatter;
import cat.ohmushi.account.domain.Account;
import cat.ohmushi.account.domain.AccountId;
import cat.ohmushi.account.domain.Currency;
import cat.ohmushi.account.domain.Money;
import cat.ohmushi.account.infrastructure.InMemoryAccounts;
import cat.ohmushi.account.usecases.DepositMoneyInAccount;
import cat.ohmushi.account.usecases.GetStatementOfAccount;
import cat.ohmushi.account.usecases.WithdrawMoneyFromAccount;

public class App {

    public static void main(String[] arg) {
        AccountService accountService = new AccountService(
                new InMemoryAccounts(List.of(
                        Account.create(
                                AccountId.of("myAccount").get(),
                                Money.of(500, Currency.EUR).get(),
                                Currency.EUR
                        )
                )),
                new DefaultAccountStatementFormatter());
        DepositMoneyInAccount depositMoneyInAccount = accountService;
        WithdrawMoneyFromAccount withdrawMoneyFromAccount = accountService;
        GetStatementOfAccount getStatementOfAccount = accountService;

        depositMoneyInAccount.deposit("myAccount", BigDecimal.valueOf(1000));
        withdrawMoneyFromAccount.withdraw("myAccount", BigDecimal.valueOf(20));

        var statement = getStatementOfAccount.getStatement("myAccount");
        System.out.println(statement);
    }
}

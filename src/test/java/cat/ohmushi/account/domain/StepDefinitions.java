package cat.ohmushi.account.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.account.domain.account.AccountId;
import cat.ohmushi.account.domain.account.AccountStatement;
import cat.ohmushi.account.domain.account.Currency;
import cat.ohmushi.account.domain.account.Money;
import cat.ohmushi.account.domain.exceptions.AccountDomainException;
import cat.ohmushi.account.exposition.formatters.AccountStatementFormatter;
import cat.ohmushi.account.exposition.formatters.DefaultAccountStatementFormatter;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinitions {

    private Account account;
    private Exception exception;
    private AccountStatement statement;
    private AccountStatementFormatter formatter = new DefaultAccountStatementFormatter();
    private final static Instant accountCreationTime = LocalDateTime
            .of(2025, Month.JANUARY, 1, 0, 0, 0, 0)
            .atZone(ZoneId.systemDefault())
            .toInstant();


    @ParameterType("([-+]?)([€$])(\\d+)")
    public Money money(String negative, String c, String a) throws Exception {
        Currency currency = switch (c) {
            case "€" ->
                Currency.EUR;
            case "$" ->
                Currency.USD;
            default ->
                throw new Exception("Unhandled currency '" + c + "'.");
        };
        var amount = new BigDecimal(a).multiply(BigDecimal.valueOf(negative.isEmpty() ? 1 : -1));
        return Money.of(amount, currency).get();
    }

    @ParameterType("deposit|withdraw|withdrawal")
    public String action(String a) throws Exception {
        if (Objects.isNull(a)) {
            throw new Exception("Action is null.");
        }
        return switch (a) {
            case "deposit" ->
                "deposit";
            case "withdraw", "withdrawal" ->
                "withdraw";
            default ->
                throw new Exception("Unhandled action '" + a + "'.");
        };
    }

    @ParameterType("(\\d{2})\\/(\\d{2})\\/(\\d{4})")
    public Instant date(String month, String day, String year) {
        var now = LocalDateTime.now();
        return LocalDateTime.of(
                Integer.parseInt(year),
                Integer.parseInt(month),
                Integer.parseInt(day),
                now.getHour(), now.getMinute(), now.getSecond(), now.getNano())
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }

    @Given("an account with a(n initial) balance of {money}")
    public void an_account_with_a_balance_of(Money balance) {
        this.account = Account.create(
                AccountId.of("id").get(),
                balance,
                balance.currency(),
                accountCreationTime);
    }

    @Given("a {action} of {money} on {date}")
    public void a_deposit_or_withdraw_on_date(String action, Money amount, Instant ondate) {
        switch (action) {
            case "deposit" ->
                this.account.deposit(amount, ondate);
            case "withdraw" ->
                this.account.withdraw(amount, ondate);
        }
    }

    @Given("I attempt to create an account with an initial balance of {money}")
    public void I_attempt_to_create_an_account_with_an_initial_balance_of(Money balance) {
        try {
            this.account = Account.create(
                    AccountId.of("id").get(),
                    balance,
                    balance.currency());
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @When("I {action} {money}")
    public void I_deposit_or_withdraw(String action, Money amount) {
        switch (action) {
            case "deposit" ->
                this.account.deposit(amount, Instant.now());
            case "withdraw" ->
                this.account.withdraw(amount, Instant.now());
        }
    }

    @When("I check my statement")
    public void I_check_my_statement() {
        this.statement = AccountStatement.forAccount(this.account);
    }

    @When("I try to {action} {money}")
    public void I_try_to_deposit(String action, Money amount) {
        try {
            this.I_deposit_or_withdraw(action, amount);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Then("my balance should be {money}")
    public void my_balance_should_be(Money balance) {
        assertThat(account.balance()).isEqualTo(balance);
    }

    @Then("the operation is declined")
    public void the_operation_is_declined() {
        assertThat(this.exception).isInstanceOf(AccountDomainException.class);
    }

    @Then("my balance should remain {money}")
    public void my_balance_should_remain(Money balance) {
        this.my_balance_should_be(balance);
    }

    @Then("the account should not be created")
    public void the_account_should_not_be_created() {
        assertThat(this.account).isNull();
    }

    @Then("my statement should be:")
    public void it_should_display(DataTable displayed) {
        String actualLines = displayed
                .asLists(String.class)
                .stream()
                .map(line -> line.stream().map(w -> String.format("%-15s", w)).toList())
                .map(line -> String.join("", line))
                .collect(Collectors.joining("\n"));
        assertThat(this.formatter.format(this.statement)).isEqualTo(actualLines);
    }

}

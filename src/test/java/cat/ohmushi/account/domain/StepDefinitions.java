package cat.ohmushi.account.domain;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cat.ohmushi.account.domain.AccountEvent.TransfertFailed;
import cat.ohmushi.account.domain.AccountStatement.AccountStatementLine;

import static java.time.LocalDateTime.now;

public class StepDefinitions {
    private Account account;
    private Exception exception;
    private AccountStatement statement;

    @ParameterType("([-+]?)([€$])(\\d+)")
    public Money money(String negative, String c, String a) throws Exception {
        Currency currency = switch (c) {
            case "€" -> Currency.EUR;
            case "$" -> Currency.USD;
            default -> throw new Exception("Unhandled currency '" + c + "'.");
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
            case "deposit" -> "deposit";
            case "withdraw", "withdrawal" -> "withdraw";
            default -> throw new Exception("Unhandled action '" + a + "'.");
        };
    }

    @ParameterType("(\\d{2})\\/(\\d{2})\\/(\\d{4})")
    public LocalDateTime date(String month, String day, String year) {
        return LocalDateTime.now();
    }

    @Given("an account with a(n initial) balance of {money}")
    public void an_account_with_a_balance_of(Money balance) {
        this.account = Account.create(
                AccountId.of("id").get(),
                balance,
                balance.currency());
    }

    @Given("a {action} of {money} on {date}")
    public void a_deposit_or_withdraw_on_date(String action, Money amount, LocalDateTime ondate) {
        switch (action) {
            case "deposit" -> this.account.deposit(amount, ondate);
            case "withdraw" -> this.account.withdraw(amount);
        }
        ;
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
            case "deposit" -> this.account.deposit(amount, now());
            case "withdraw" -> this.account.withdraw(amount);
        }
        ;
    }

    @When("I check my statement")
    public void I_check_my_statement() {
        this.statement = AccountStatement.forAccount(this.account);
    }

    @When("I try to {action} {money}")
    public void I_try_to_deposit(String action, Money amount) {
        this.I_deposit_or_withdraw(action, amount);
    }

    @Then("my balance should be {money}")
    public void my_balance_should_be(Money balance) {
        assertThat(account.balance()).isEqualTo(balance);
    }

    @Then("the operation is declined")
    public void the_operation_is_declined() {
        if (Objects.isNull(this.exception)) {
            var events = account.events().stream().map((AccountEvent e) -> e.getClass().getName()).toList();
            assertThat(events).contains(TransfertFailed.class.getName());
        }
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
        List<List<String>> actualLines = displayed.asLists(String.class);
        List<List<String>> expected = new ArrayList<>(List.of(
                List.of("Date", "Operation", "Amount", "Balance")));
        this.statement.lines()
                .stream()
                .map(l -> List.of(l.date().toString(), l.operation(), l.amount().toString(), l.balance().toString()))
                .forEach(l -> expected.add(l));
        assertThat(expected).isEqualTo(actualLines);
    }

}
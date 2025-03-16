package cat.ohmushi.account.domain;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

public class StepDefinitions {
    private Account account;
    private Exception exception;

    @ParameterType("(-?)([€$])(\\d+)")
    public Money money(String negative, String c, String a) throws Exception {
        Currency currency = switch (c) {
            case "€" -> Currency.EUR;
            case "$" -> Currency.USD;
            default -> throw new Exception("Unhandled currency '" + c + "'.");
        };
        var amount = new BigDecimal(a).multiply(BigDecimal.valueOf(negative.isEmpty() ? 1 : -1)) ;
        return Money.of(amount, currency).get();
    }

    @ParameterType("deposit|withdraw")
    public String action(String a) {
        return a;
    }

    @Given("an account with a balance of {money}")
    public void an_account_with_a_balance_of(Money balance) {
        this.account = Account.create(
            AccountId.of("id").get(), 
            balance, 
            balance.currency());
    }

    @When("I {action} {money}")
    public void I_deposit_or_withdraw(String action, Money amount) {
        switch(action) {
            case "deposit" -> this.account.deposit(amount);
            case "withdraw" -> this.account.withdraw(amount);
        };
    }

    @Then("my balance should be {money}")
    public void my_balance_should_be(Money balance) {
        assertThat(account.balance()).isEqualTo(balance);
    }

    @Then("the operation is declined")
    public void the_operation_is_declined() {
        assertThat(this.exception).isNotNull();
    }

    @Then("my balance should remain {money}")
    public void my_balance_should_remain(Money balance) {
        this.my_balance_should_be(balance);
    }

    @When("I try to {action} {money}")
    public void I_try_to_deposit(String action, Money amount) {
        try {
            this.I_deposit_or_withdraw(action, amount);
        } catch (Exception e) {
            this.exception = e;
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

    @Then("the account should not be created")
    public void the_account_should_not_be_created() {
        assertThat(this.account).isNull();
    }
    
}
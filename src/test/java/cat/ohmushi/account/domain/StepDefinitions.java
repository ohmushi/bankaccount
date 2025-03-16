package cat.ohmushi.account.domain;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cat.ohmushi.account.domain.AccountException.TransfertException;

public class StepDefinitions {
    private Account account;
    private Exception exception;

    @Given("an account with a balance of €{int}")
    public void an_account_with_a_balance_of(int balance) {
        this.account = Account.create(
            AccountId.of("id").get(), 
            Money.of(balance, Currency.EUR).get(), 
            Currency.EUR);
    }

    @When("I deposit €{int}")
    public void I_deposit(int amount) {
        this.account.deposit(Money.of(amount, Currency.EUR).get());
    }

    @Then("my balance should be €{int}")
    public void my_balance_should_be(int balance) {
        assertThat(account.balance()).isEqualTo(Money.of(balance, Currency.EUR).get());
    }

    @Then("the operation is declined")
    public void the_operation_is_declined() {
        assertThat(this.exception)
            .isInstanceOf(TransfertException.class);
    }

    @Then("my balance should remain €{int}")
    public void my_balance_should_remain(int balance) {
        this.my_balance_should_be(balance);
    }

    @When("I try to deposit -€{int}")
    public void I_try_to_deposit_negative(int amount) {
        this.I_try_to_deposit(amount * -1);
    }

    @When("I try to deposit €{int}")
    public void I_try_to_deposit(int amount) {
        try {
            this.account.deposit(Money.of(amount, Currency.EUR).get());
        } catch (Exception e) {
            this.exception = e;
        }
    }
}
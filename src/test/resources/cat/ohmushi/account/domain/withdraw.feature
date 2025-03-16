Feature: Withdraw from a bank account
  As a bank client
  I want to withdraw money from my account
  So that I can use my available balance

  Scenario: Withdraw a valid amount
    Given an account with a balance of €1500
    When I withdraw €500
    Then my balance should be €1000

  Scenario: Withdraw the entire balance
    Given an account with a balance of €500
    When I withdraw €500
    Then my balance should be €0

  Scenario: Withdraw more than the available balance (allowed)
    Given an account with a balance of €300
    When I withdraw €500
    Then my balance should be -€200

  Scenario: Withdraw a negative amount (not allowed)
    Given an account with a balance of €800
    When I try to withdraw -€100
    Then the operation is declined
    And my balance should remain €800

  Scenario: Withdraw in a different currency (not allowed)
    Given an account with a balance of €1000
    When I try to withdraw $200
    Then the operation is declined
    And my balance should remain €1000

  Scenario: Withdraw an extremely large amount (boundary test)
    Given an account with a balance of €2000000000
    When I withdraw €1000000000
    Then my balance should be €1000000000

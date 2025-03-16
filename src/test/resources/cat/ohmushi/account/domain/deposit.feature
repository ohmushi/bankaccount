Feature: Deposit into a bank account
  As a bank client
  I want to deposit money into my account
  So that I can increase my available balance

  Scenario: Deposit a valid amount
    Given an account with a balance of €500
    When I deposit €1000
    Then my balance should be €1500

  Scenario: Deposit zero euros
    Given an account with a balance of €500
    When I try to deposit €0
    Then the operation is declined
    And my balance should remain €500

  Scenario: Deposit a negative amount (not allowed)
    Given an account with a balance of €500
    When I try to deposit -€100
    Then the operation is declined
    And my balance should remain €500

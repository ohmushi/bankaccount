Feature: Bank account statement consultation
  As a bank client
  I want to check my account statement
  So that I can see my transaction history and balance

  Scenario: Display statement after multiple transactions
    Given an account with an initial balance of €1000
    And a deposit of €500 on 01/03/2025
    And a withdraw of €200 on 02/03/2025
    When I check my statement
    Then my statement should be:
      | Date       | Operation  | Amount | Balance |
      | 01/03/2025 | Deposit    | +€500  | €1500   |
      | 02/03/2025 | Withdrawal | -€200  | €1300   |

  Scenario: Display statement with no transactions
    Given an account with an initial balance of €1000
    When I check my statement
    Then my statement should be:
      | Date | Operation       | Amount | Balance |
      | ---- | ---------       | ------ | ------- |
      |      | No transactions | -      | €1000   |

  Scenario: Display statement with only withdrawals
    Given an account with an initial balance of €1000
    And a withdrawal of €300 on 01/03/2025
    And a withdrawal of €200 on 02/03/2025
    When I check my statement
    Then my statement should be:
      | Date       | Operation  | Amount | Balance |
      | 01/03/2025 | Withdrawal | -€300  | €700    |
      | 02/03/2025 | Withdrawal | -€200  | €500    |

  Scenario: Display statement with only deposits
    Given an account with an initial balance of €1000
    And a deposit of €400 on 01/03/2025
    And a deposit of €600 on 02/03/2025
    When I check my statement
    Then my statement should be:
      | Date       | Operation | Amount | Balance |
      | 01/03/2025 | Deposit   | +€400  | €1400   |
      | 02/03/2025 | Deposit   | +€600  | €2000   |
  # Scenario: Display statement with transactions in different currencies (not allowed)
  #   Given an account with an initial balance of €1000
  #   And a deposit of $500 on 01/03/2025
  #   When I check my statement
  #   Then the operation is declined
  #   And my statement should be:
  #     | Date | Operation             | Amount | Balance |
  #     | ---- | ---------             | ------ | ------- |
  #     |      | No valid transactions | -      | €1000   |

  Scenario: Display statement after a balance reaches zero
    Given an account with an initial balance of €500
    And a withdrawal of €500 on 01/03/2025
    When I check my statement
    Then my statement should be:
      | Date       | Operation  | Amount | Balance |
      | 01/03/2025 | Withdrawal | -€500  | €0      |

  Scenario: Display statement after an attempt to overdraw (allowed)
    Given an account with an initial balance of €500
    And a withdrawal of €600 on 01/03/2025
    When I check my statement
    Then my statement should be:
      | Date       | Operation  | Amount | Balance |
      | 01/03/2025 | Withdrawal | -€600  | €500    |

  Scenario: Display statement with multiple transactions on the same day
    Given an account with an initial balance of €1000
    And a deposit of €200 on 01/03/2025
    And a withdrawal of €150 on 01/03/2025
    When I check my statement
    Then my statement should be:
      | Date       | Operation  | Amount | Balance |
      | 01/03/2025 | Deposit    | +€200  | €1200   |
      | 01/03/2025 | Withdrawal | -€150  | €1050   |

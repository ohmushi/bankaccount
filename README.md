# Banck Account

## Start

run
```bash
./mvnw clean install
./mvnw exec:java
```

### Test
```bash
./mvnw test
```

## Problem Description

Think of your personal bank account experience
When in doubt, go for the simplest solution

Requirements
* Deposit and Withdrawal
* Account statement (date, amount, balance)
* Statement printing

User Stories
US 1 : 
In order to save money
As a bank client
I want to make deposit in my account

US 2:
In order to retrieve some or all of my savings
As a bank client
A want to make a withdrawal from my account


## Decisions

I have chosen to implement a Clean Architecture approach with a domain-first design to ensure that business logic remains independent of frameworks and external concerns. The system follow an event-based approach for communication and state changes.

### Use Cases

Deposit in Account

Withdraw from Account

Get Account Statement

Each use case is treated as a contract, and since the operations are relatively simple, a single service handle their implementation.

### Error Handling

Extensive use of custom exceptions to manage domain-specific errors.

Optional types will be used where necessary to prevent null-related issues and ensure safe handling of missing data.

### Testing Strategy

Cucumber for Behavior-Driven Development (BDD)

3 main features: Deposit, Withdraw, Statement (see src/test/resources/cat/ohmushi/account/domain).

Ensures that business rules are well-defined and tested from an end-user perspective.

#### Unit Testing

JUnit & AssertJ are used for unit testing to ensure correctness and reliability of individual components.

### Consequences

#### Benefits

Clear separation of concerns, making the system easier to maintain and extend.

Domain logic remains independent from frameworks and infrastructure, ensuring long-term adaptability.

Event-based approach allows for greater flexibility and potential future scalability.

Robust error handling to prevent silent failures and ensure correctness.

Well-defined test strategy to guarantee system reliability and expected behavior.

#### Trade-offs

Clean Architecture introduces some complexity in structuring the project.

Using custom exceptions and optionals may require extra effort in handling and documentation.

Event-based communication may increase debugging complexity compared to a purely synchronous approach.

## Author

Theo OMNES (@ohmushi)
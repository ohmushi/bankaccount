package cat.ohmushi.account.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cat.ohmushi.shared.annotations.Value;

class AccountStatement {

  private final Account account;
  private final List<AccountStatementLine> lines;

  record AccountStatementLine(
    LocalDateTime date,
    String operation,
    Money amount,
    Money balance
  ) {}

  private AccountStatement(Account account) {
    this.account = account;
    this.lines = new ArrayList<>();
  }

  public static AccountStatement forAccount(Account account) {
    return new AccountStatement(account);
  }

  public List<AccountStatementLine> lines() {
    return Collections.unmodifiableList(this.lines);
  }

}



package cat.ohmushi.account.domain;

class AccountStatement {
  class StatementLine {

  }

  private Account account;

  private AccountStatement(Account account) {
    this.account = account;
  }

  public static AccountStatement forAccount(Account account) {
    return new AccountStatement(account);
  }
}

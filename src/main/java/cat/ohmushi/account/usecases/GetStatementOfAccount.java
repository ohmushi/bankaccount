package cat.ohmushi.account.usecases;

@FunctionalInterface
public interface GetStatementOfAccount {
    String getStatement(String id);
}

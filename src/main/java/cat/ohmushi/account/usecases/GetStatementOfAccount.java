package cat.ohmushi.account.usecases;

import java.util.List;

public interface GetStatementOfAccount {
    List<List<String>> getStatement(String id);
}

package cat.ohmushi.account.domain;

import java.util.Objects;

@DomainEntityId
public class AccountId {

    private final String value;
    
    private AccountId(String value) {
        this.value = Objects.requireNonNull(value).trim().toLowerCase();
    }

    public static AccountId of(String id) {
        return new AccountId(id);
    }

}

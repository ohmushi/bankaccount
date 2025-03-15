package cat.ohmushi.account.domain;

import java.util.Objects;

import cat.ohmushi.shared.annotations.DomainEntityId;

@DomainEntityId
public class AccountId {

    private final String value;
    
    private AccountId(String value) {
        this.value = Objects.requireNonNull(value).trim().toLowerCase();
        if(this.value.isEmpty()) {
            throw new IllegalArgumentException("Tried to create an id with empty value");
        }
    }

    public static AccountId of(String id) {
        return new AccountId(id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccountId other = (AccountId) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    
}

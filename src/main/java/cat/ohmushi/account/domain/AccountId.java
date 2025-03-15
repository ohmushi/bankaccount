package cat.ohmushi.account.domain;

import java.util.Objects;
import java.util.Optional;

import cat.ohmushi.shared.annotations.DomainEntityId;

@DomainEntityId
public class AccountId {

    private final String value;
    
    private AccountId(String value) throws AccountException {
        try {
            this.value = Objects.requireNonNull(value).trim().toLowerCase();
            if(this.value.isEmpty()) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            throw new AccountException("Cannot create an id with null or empty value.");
        }
    }

    public static Optional<AccountId> of(String id) {
        try {
            return Optional.of(new AccountId(id));
        } catch (Exception e) {
            return Optional.empty();
        }
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

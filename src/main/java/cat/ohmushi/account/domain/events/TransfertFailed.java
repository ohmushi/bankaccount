package cat.ohmushi.account.domain.events;

import java.time.Instant;

import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.account.domain.exceptions.AccountDomainException;
import cat.ohmushi.shared.annotations.Value;

@Value
public record TransfertFailed(
        AccountDomainException reason,
        Instant eventDate) implements AccountEvent {

    @Override
    public Account play(Account a) {
        return a;
    }

    @Override
    public Instant getDate() {
        return this.eventDate;
    }
}

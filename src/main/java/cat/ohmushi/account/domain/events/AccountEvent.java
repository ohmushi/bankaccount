package cat.ohmushi.account.domain.events;

import cat.ohmushi.account.domain.account.Account;
import cat.ohmushi.shared.Event;
import cat.ohmushi.shared.annotations.DomainEvent;

@DomainEvent
public interface AccountEvent extends Event<Account> {

}

package cat.ohmushi.shared;


import java.time.Instant;

import cat.ohmushi.shared.annotations.DomainEntity;

public interface Event<T extends DomainEntity.DomainEntityType> {
  T play(T entity);

  Instant getDate();
}

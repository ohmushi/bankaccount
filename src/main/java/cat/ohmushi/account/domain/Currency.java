package cat.ohmushi.account.domain;

import java.util.Objects;
import java.util.Optional;

import cat.ohmushi.shared.annotations.Value;

@Value
public enum Currency {
  EUR,
  USD,
  ;

  public static Optional<Currency> fromString(String s) {
    if(Objects.isNull(s)) {
      return Optional.empty();
    }
    return switch(s.trim().toUpperCase()) {
      case "EUR" -> Optional.of(EUR);
      case "USD" -> Optional.of(USD);
      default -> Optional.empty();
    };
  }
}

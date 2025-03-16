package cat.ohmushi.shared;

import java.util.Optional;
import java.util.function.Function;

public class OptionalUtils {
  public static <T, V> Optional<V> ofThrowable(T it, Function<T, V> func) {
        try {
            return Optional.ofNullable(func.apply(it));
        } catch (final Exception e) {
            return Optional.empty();
        }
    }
}

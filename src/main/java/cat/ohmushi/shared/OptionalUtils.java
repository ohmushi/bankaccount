package cat.ohmushi.shared;

import java.util.Optional;
import java.util.concurrent.Callable;

public class OptionalUtils {
    public static <T> Optional<T> ofThrowable(Callable<T> func) {
        try {
            return Optional.ofNullable(func.call());
        } catch (final Exception e) {
            return Optional.empty();
        }
    }
}

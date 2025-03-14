
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;


public class TestJunit {
  @Test
  void should_assert_true() {
    assertThat(1).isEqualTo(1);
  }
}

package monads;

import java.util.Arrays;
import java.util.Objects;

public class Any
{
  @SafeVarargs
  public static <T> T any(final T... ts)
  {
    return Arrays.stream(ts)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }
}
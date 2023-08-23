package monads;

import java.util.Optional;

public class Either
{
  public static <T> T either(final T t1, final T t2)
  {
    return Optional.ofNullable(t1)
        .orElse(t2);
  }
}
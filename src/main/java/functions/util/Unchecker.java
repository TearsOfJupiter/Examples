package functions.util;

public class Unchecker
{
  public static <E extends Throwable> RuntimeException asUnchecked(final E e)
  {
    return new RuntimeException(e);
  }
}
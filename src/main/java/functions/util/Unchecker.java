package functions.util;

public class Unchecker
{
  public static <E extends Throwable> RuntimeException uncheck(final E e)
  {
    return new RuntimeException(e);
  }

  public static <E extends Throwable> void throwUnchecked(final E e)
  {
    throw new RuntimeException(e);
  }
}

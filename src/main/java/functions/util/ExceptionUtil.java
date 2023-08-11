package functions.util;

/**
 * With help from <a href="https://stackoverflow.com/a/39719455">...</a>
 */
public class ExceptionUtil
{
  @SuppressWarnings("unchecked")
  public static <E extends Throwable> RuntimeException throwAsUnchecked(final Throwable e) throws E
  {
    throw (E) e;
  }
}
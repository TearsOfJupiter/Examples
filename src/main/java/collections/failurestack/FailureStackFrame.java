package collections.failurestack;

import util.tuples.Trio;

public class FailureStackFrame<T, C extends Class<T>, E extends Throwable> extends Trio<T, C, E>
{
  private FailureStackFrame(final T t, final C c, final E e)
  {
    super(t, c, e);
  }

  public static <T, C extends Class<T>, E extends Throwable> FailureStackFrame<T, C, E> of(final T t, final C c, final E e)
  {
    return new FailureStackFrame<>(t, c, e);
  }

  public T getValue()
  {
    return getValueClass().cast(getA());
  }

  public C getValueClass()
  {
    return getB();
  }

  public E getError()
  {
    return getC();
  }
}
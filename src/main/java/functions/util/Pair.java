package functions.util;

public class Pair<T, U>
{
  private T a;
  private U b;

  private Pair(final T a, final U b)
  {
    this.a = a;
    this.b = b;
  }

  public static <T, U> Pair<T, U> of(final T a, final U b)
  {
    return new Pair<>(a, b);
  }

  public T getA()
  {
    return a;
  }
  public Pair<T, U> withA(final T a)
  {
    this.a = a;
    return this;
  }

  public U getB()
  {
    return b;
  }
  public Pair<T, U> withB(final U b)
  {
    this.b = b;
    return this;
  }
}

package functions.util.tuples;

import java.util.Objects;

public class Trio<A, B, C> extends Pair<A, B>
{
  private final C c;

  public Trio(final A a, final B b, final C c)
  {
    super(a, b);
    this.c = c;
  }

  public static <A, B, C> Trio<A, B, C> from(final Pair<A, B> pair, final C c)
  {
    Objects.requireNonNull(pair);
    return new Trio<>(pair.getA(), pair.getB(), c);
  }

  public C getC()
  {
    return c;
  }
}
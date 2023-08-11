package functions.util.tuples;

import java.util.Objects;

public class Pair<A, B>
{
  private final A a;
  private final B b;

  public Pair(final A a, final B b)
  {
    this.a = a;
    this.b = b;
  }

  public A getA()
  {
    return a;
  }

  public B getB()
  {
    return b;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;

    return (obj instanceof Pair<?, ?> p) // Pattern matching, as of 17
        && Objects.equals(a, p.a)
        && Objects.equals(b, p.b);
  }
}
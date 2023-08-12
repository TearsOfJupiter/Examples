package util.tuples;

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

    return (obj instanceof Pair<?, ?> p) // Pattern matching, as of 17 (https://docs.oracle.com/en/java/javase/17/language/pattern-matching-instanceof-operator.html#GUID-843060B5-240C-4F47-A7B0-95C42E5B08A7)
        && Objects.equals(a, p.a)
        && Objects.equals(b, p.b);
  }
}
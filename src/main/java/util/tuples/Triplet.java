package util.tuples;

import java.util.Objects;

public class Triplet<T> extends Trio<T, T, T>
{
  public Triplet(final T t1, final T t2, final T t3)
  {
    super(t1, t2, t3);
  }

  public static <T> Triplet<T> from(final Twin<T> twin, final T c)
  {
    Objects.requireNonNull(twin);
    return new Triplet<>(twin.getA(), twin.getB(), c);
  }
}
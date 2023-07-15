package functions.curry;

import functions.arity.TriFunction;

import java.util.function.*;

public class Currier
{
  public static <T, U, R> Function<? super T, Function<? super U, ? extends R>> curry(
      final BiFunction<? super T, ? super U, ? extends R> biFunction)
  {
    return a -> b -> biFunction.apply(a, b);
  }

  public static IntFunction<IntUnaryOperator> curryInt(
      final IntBinaryOperator biFunction)
  {
    return a -> b -> biFunction.applyAsInt(a, b);
  }

  public static <T, U, V, R> Function<? super T, Function<? super U, Function<? super V, ? extends R>>> curry(
      final TriFunction<? super T, ? super U, ? super V, ? extends R> triFunction)
  {
    return a -> b -> c -> triFunction.apply(a, b, c);
  }
}

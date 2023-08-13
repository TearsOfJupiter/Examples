package functions.recursivelambda;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.IntUnaryOperator;

// @param <I> - Functional Interface Type
public class Recursable<I>
{
  private I func;

  public I getFunc()
  {
    return func;
  }
  public Recursable<I> withFunc(final I func)
  {
    this.func = func;
    return this;
  }

  public static int factorial(final int n)
  {
    final Recursable<IntUnaryOperator> recursable = new Recursable<>();
    recursable.func = x -> (x == 0) ? 1 : x * recursable.func.applyAsInt(x - 1);
    return recursable.func.applyAsInt(n);
  }

  public static long fibonacci(final int n)
  {
    final Recursable<BiFunction<Map<Integer, Long>, Integer, Long>> recursable = new Recursable<>();
    recursable.func = (m, x) -> m.computeIfAbsent(x, y -> recursable.func.apply(m, y - 2) + recursable.func.apply(m, y - 1));
    return recursable.func.apply(new ConcurrentHashMap<>(Map.of(0, 0L, 1, 1L)), n);
  }
}
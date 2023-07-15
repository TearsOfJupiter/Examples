package functions.arity;

@FunctionalInterface
@SuppressWarnings("unchecked")
public interface VariadicFunction<T, R>
{
  R apply(T... ts);

  /**
   * This method merely wraps a VariadicFunction lambda in order to be immediately chained upon
   * Example:
   *    VariadicFunction.<Integer, Integer>of(
   *            array -> Arrays.stream(array).mapToInt(Integer::intValue).sum())
   *        .apply(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
   */
  static <T, R> VariadicFunction<T, R> of(final VariadicFunction<T, R> variadicFunction)
  {
    return variadicFunction;
  }
}

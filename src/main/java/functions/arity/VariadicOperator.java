package functions.arity;

public interface VariadicOperator<T> extends VariadicFunction<T, T>
{
  /**
   * This method merely wraps a VariadicOperator lambda in order to be immediately chained upon
   * Example:
   *    VariadicOperator.<Integer>of(
   *            array -> Arrays.stream(array).mapToInt(Integer::intValue).sum())
   *        .apply(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
   */
  static <T> VariadicOperator<T> of(final VariadicOperator<T> operator)
  {
    return operator;
  }
}

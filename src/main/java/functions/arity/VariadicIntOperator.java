package functions.arity;

import collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;

@FunctionalInterface
public interface VariadicIntOperator extends VariadicOperator<Integer>
{
  /**
   * This method merely wraps a VariadicIntOperator lambda in order to be immediately chained upon
   * Example:
   *    VariadicIntOperator.of(
   *            array -> Arrays.stream(array).mapToInt(Integer::intValue).sum())
   *        .apply(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
   */
  static VariadicIntOperator of(final VariadicIntOperator operator)
  {
    return operator;
  }

  default <C extends Collection<Integer>> Integer apply(final C collection)
  {
    return apply(CollectionUtils.defaultCollection(collection, ArrayList::new).toArray(Integer[]::new));
  }
}

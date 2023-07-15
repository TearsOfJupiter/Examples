package functions.arity;

import collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;

@FunctionalInterface
public interface VariadicIntOperator extends VariadicOperator<Integer>
{
  static VariadicIntOperator of(final VariadicIntOperator operator)
  {
    return operator;
  }

  default <C extends Collection<Integer>> Integer apply(final C collection)
  {
    return apply(CollectionUtils.defaultCollection(collection, ArrayList::new).toArray(Integer[]::new));
  }
}

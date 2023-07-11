package functions.arity;

import collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;

@FunctionalInterface
public interface VariadicIntUnaryOperator extends VariadicFunction<Integer, Integer>
{
  default <C extends Collection<Integer>> Integer apply(final C collection)
  {
    return apply(CollectionUtils.defaultCollection(collection, ArrayList::new).toArray(Integer[]::new));
  }
}

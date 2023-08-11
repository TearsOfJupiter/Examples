package collections;

import functions.util.Visitor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * My own take on common CollectionUtils classes (e.g. apache.collections, Spring, etc.)
 */
public class CollectionUtils
{
  public static <T, C extends Collection<T>> C defaultCollection(final C collection, final Supplier<C> defaultSupplier)
  {
    return collection != null
        ? collection
        : defaultSupplier.get();
  }

  public static <T, C extends Collection<T>> C addPassThru(final C collection, final T t)
  {
    return Visitor.visit(Collection::add, collection, t);
  }

  public static <T> List<T> reverseList(final List<T> list)
  {
    return list == null
        ? Collections.emptyList()
        : IntStream.range(0, list.size())
            .map(i -> list.size() - 1 - i)
            .mapToObj(list::get)
            .collect(Collectors.toList());
  }
}
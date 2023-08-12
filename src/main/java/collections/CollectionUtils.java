package collections;

import functions.util.Visitor;
import functions.util.tuples.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CollectionUtils
{
  private CollectionUtils() {}

  /* *******************************************************************************************************************
   *                                                 DEFAULT METHODS
   * ***************************************************************************************************************** */

  public static <T> List<? extends T> defaultList(final List<? extends T> list)
  {
    return defaultCollection(list, ArrayList::new);
  }

  public static <T, C extends Collection<T>> C defaultCollection(final C collection, final Supplier<C> defaultSupplier)
  {
    return collection != null
        ? collection
        : defaultSupplier.get();
  }

  public static <T, C extends Collection<T>> Stream<T> defaultStream(final C collection)
  {
    return defaultCollection(collection, HashSet::new).stream();
  }

  /* *******************************************************************************************************************
   *                                                 MUTATIVE METHODS
   * ***************************************************************************************************************** */

  public static <T, C extends Collection<T>> C add(final C collection, final T t)
  {
    return Visitor.visit(Collection::add, collection, t);
  }

  /* *******************************************************************************************************************
   *                                              TRANSFORMATIVE METHODS
   * ***************************************************************************************************************** */

  public static <T> List<T> reverseList(final List<T> list)
  {
    return list == null
        ? Collections.emptyList()
        : IntStream.range(0, list.size())
            .map(i -> list.size() - 1 - i)
            .mapToObj(list::get)
            .collect(Collectors.toList());
  }

  @SuppressWarnings("DataFlowIssue")
  public static <T, U> List<U> mapToList(final Collection<T> collection,
                                         final Function<? super T, ? extends U> mapper)
  {
    return defaultStream(collection)
        .map(Objects.requireNonNull(mapper))
        .collect(Collectors.toList());
  }

  /* *******************************************************************************************************************
   *                                            COMBINATORICS METHODS
   * ***************************************************************************************************************** */

  public static <T, U> List<Pair<? extends T, ? extends U>> cartesianProduct(final Collection<? extends T> tCollection,
                                                                             final Collection<? extends U> uCollection)
  {
    return defaultStream(tCollection)
        .flatMap(t -> defaultStream(uCollection)
            .map(u -> new Pair<>(t, u)))
        .collect(Collectors.toList());
  }
}
package collections;

import util.Visitor;
import util.tuples.Pair;

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

  public static <T, C1 extends Collection<T>, C2 extends Collection<T>> List<T> merge(final C1 c1, final C2 c2)
  {
    return Stream.concat(
          defaultStream(c1),
          defaultStream(c2)
        ).collect(Collectors.toCollection(ArrayList::new));
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

  public static <T, R> List<R> mapToList(final Collection<T> collection,
                                         final Function<? super T, ? extends R> mapper)
  {
    return defaultStream(collection)
        .map(Objects.requireNonNull(mapper))
        .collect(Collectors.toList());
  }

  public static <T> T first(final Collection<? extends T> collection)
  {
    return defaultStream(collection)
        .findFirst()
        .orElse(null);
  }

  /* *******************************************************************************************************************
   *                                            COMBINATORICS METHODS
   * ***************************************************************************************************************** */

  public static <T, R> List<Pair<? extends T, ? extends R>> cartesianProduct(final Collection<? extends T> tCollection,
                                                                             final Collection<? extends R> uCollection)
  {
    return defaultStream(tCollection)
        .flatMap(t -> defaultStream(uCollection)
            .map(u -> new Pair<>(t, u)))
        .collect(Collectors.toList());
  }
}
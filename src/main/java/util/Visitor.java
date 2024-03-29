package util;

import collections.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Turns consumers into pass-throughs
 */
public class Visitor<T>
{
  private T value;

  private Visitor() {}

  private Visitor<T> withValue(final T value)
  {
    this.value = value;
    return this;
  }

  /**
   * Instantiates a visitor upon which chained {@link Visitor#visit(BiConsumer, Object)} calls can be made
   */
  public static <T> Visitor<T> of(final T value)
  {
    return new Visitor<T>()
        .withValue(value);
  }

  /**
   * NOTE: Needs to be used in conjuction with {@link Visitor#of(Object)}
   */
  public <R> Visitor<T> visit(final BiConsumer<? super T, ? super R> consumer,
                              final R value)
  {
    consumer.accept(this.value, value);
    return this;
  }

  public T get()
  {
    return value;
  }

  public static <T> T visit(final Consumer<? super T> consumer,
                            final T visited)
  {
    consumer.accept(visited);
    return visited;
  }

  public static <T, R> T visit(final BiConsumer<? super T, ? super R> consumer,
                               final T visited,
                               final R value)
  {
    consumer.accept(visited, value);
    return visited;
  }

  @SuppressWarnings("UnusedReturnValue")
  @SafeVarargs
  public static <T> T visit(final T visited,
                            final Consumer<? super T>... consumers)
  {
    Arrays.asList(consumers).forEach(consumer -> consumer.accept(visited));
    return visited;
  }

  @SafeVarargs
  public static <T, C extends Collection<? extends T>> C visitEach(final C collection,
                                                                   final Consumer<? super T>... consumers)
  {
    CollectionUtils.cartesianProduct(collection, Arrays.asList(consumers))
        .forEach(pair -> pair.getB().accept(pair.getA()));
    return collection;
  }
}
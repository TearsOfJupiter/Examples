package functions;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A silly class, I know, as you could just chain a series of Function::map calls instead of nesting CompositeFunctions,
 *   but this was an interesting exercise
 */
public class CompositeFunction<T, R>
{
  private Function<T, R> function;
  private CompositeFunction<T, R> child;

  private CompositeFunction<T, R> withFunction(final Function<T, R> function)
  {
    this.function = function;
    return this;
  }

  private CompositeFunction<T, R> withChild(final CompositeFunction<T, R> child)
  {
    this.child = child;
    return this;
  }

  public static <T, R> CompositeFunction<T, R> of(final Function<T, R> function)
  {
    return new CompositeFunction<T, R>()
        .withFunction(function);
  }

  @SuppressWarnings("unchecked")
  public static <T, U, R> CompositeFunction<T, R> of(final Function<T, U> function,
                                                     final CompositeFunction<U, R> child)
  {
    return (CompositeFunction<T, R>) of(function)
        .withChild((CompositeFunction<T, U>) child);
  }

  public R apply(final T t)
  {
    final R r = Objects.requireNonNull(function).apply(t);
    //noinspection unchecked
    return Optional.ofNullable(child)
        .map(c -> c.apply((T) r))
        .orElse(r);
  }
}
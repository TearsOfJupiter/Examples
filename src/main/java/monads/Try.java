package monads;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Try<T, E extends Throwable>
{
  private final T val;
  private final E e;

  private Try(final T val, final E e)
  {
    if (val != null && e != null)
      throw new IllegalStateException("val and e cannot both be non-null");

    this.val = val;
    this.e = e;
  }

  public static <T, E extends Throwable> Try<T, E> of(final T val)
  {
    return success(val);
  }
  public static <T, E extends Throwable> Try<T, E> of(final Supplier<? extends T> suppler)
  {
    try
    {
      return success(suppler.get());
    }
    catch (Throwable t)
    {
      //noinspection unchecked
      return failure((E) t);
    }
  }
  public static <T, U, E extends Throwable> Try<U, E> of(final Function<? super T, ? extends U> mapper, final T val)
  {
    try
    {
      return success(mapper.apply(val));
    }
    catch (Throwable t)
    {
      //noinspection unchecked
      return failure((E) t);
    }
  }

  private static <T, E extends Throwable> Try<T, E> success(final T val)
  {
    return new Try<>(val, null);
  }

  private static <T, E extends Throwable> Try<T, E> failure(final E e)
  {
    return new Try<>(null, e);
  }

  public T get() throws E
  {
    if (isSuccessful())
      return val;
    else
      throw e;
  }

  public boolean isSuccessful()
  {
    return val != null;
  }

  public boolean isFailure()
  {
    return e != null;
  }

  public Try<T, E> ifSuccessful(final Consumer<? super T> consumer)
  {
    if (isSuccessful())
      Objects.requireNonNull(consumer).accept(val);

    return this;
  }

  public Try<T, E> ifSuccessfulOrElse(final Consumer<? super T> consumer, final Runnable failureAction)
  {
    if (isSuccessful())
    {
      return ifSuccessful(consumer);
    }
    else
    {
      failureAction.run();
      return this;
    }
  }

  public Try<T, E> ifFailure(final Consumer<? super E> consumer)
  {
    if (isFailure())
      consumer.accept(e);

    return this;
  }

  public Try<T, E> filter(final Predicate<? super T> predicate)
  {
    if (isFailure())
      return this;
    else
      //noinspection unchecked
      return Objects.requireNonNull(predicate).test(val)
          ? this
          : (Try<T, E>) failure(new IllegalArgumentException(val + " failed predicate " + predicate));
  }

  public <U> Try<U, E> map(final Function<? super T, ? extends U> mapper)
  {
    return isSuccessful()
        ? success(Objects.requireNonNull(mapper).apply(val))
        : failure(e);
  }

  public <U> Try<U, E> flatMap(final Function<? super T, ? extends Try<? extends U, ? extends E>> mapper)
  {
    //noinspection unchecked
    return isSuccessful()
        ? (Try<U, E>) Objects.requireNonNull(mapper).apply(val)
        : failure(e);
  }

  public Try<T, E> or(final Supplier<Try<T, E>> supplier)
  {
    return isSuccessful()
        ? this
        : Objects.requireNonNull(supplier).get();
  }

  public Stream<T> stream()
  {
    return isSuccessful()
        ? Stream.of(val)
        : Stream.empty();
  }

  public T orElse(final T alternative)
  {
    return isSuccessful()
        ? val
        : alternative;
  }

  public T orElseGet(final Supplier<? extends T> supplier)
  {
    return isSuccessful()
        ? val
        : supplier.get();
  }

  public T orElseThrow() throws E
  {
    if (isSuccessful())
      return val;
    else
      throw e;
  }

  public T orElseThrow(final Supplier<? extends E> exceptionSupplier) throws E
  {
    if (isSuccessful())
      return val;
    else
      throw Objects.requireNonNull(exceptionSupplier).get();
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
      return true;

    return obj instanceof Try<?, ?> other
        && Objects.equals(val, other.val)
        && Objects.equals(e, other.e);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(val, e);
  }

  @Override
  public String toString()
  {
    return isSuccessful()
        ? ("Try[val=" + val)
        : ("Try[e=" + e);
  }
}

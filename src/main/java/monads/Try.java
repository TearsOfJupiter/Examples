package monads;

import functions.util.Pair;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Try<T>
{
  private final T val;
  protected final Throwable e;

  private Try(final T val, final Throwable e)
  {
    this.val = val;
    this.e = e;
  }

  public static <T> Try<T> of(final T val)
  {
    return success(val);
  }
  public static <T> Try<T> of(final Supplier<? extends T> suppler)
  {
    try
    {
      return success(suppler.get());
    }
    catch (Throwable e)
    {
      return failure(e);
    }
  }
  @SuppressWarnings("unchecked")
  public static <T, U, B extends Try<U>> B of(final Function<? super T, ? extends U> mapper, final T val)
  {
    try
    {
      return (B) success(mapper.apply(val));
    }
    catch (Throwable e)
    {
      return (B) FailureWithOriginalVal.failure(val, e);
    }
  }

  private static <T> Try<T> success(final T val)
  {
    return new Try<>(val, null);
  }

  private static <T> Try<T> failure(final Throwable e)
  {
    return new Try<>(null, e);
  }

  public T get() throws Throwable
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

  public Try<T> ifSuccessful(final Consumer<? super T> consumer)
  {
    if (isSuccessful())
      Objects.requireNonNull(consumer).accept(val);

    return this;
  }

  public Try<T> ifSuccessfulOrElse(final Consumer<? super T> consumer, final Runnable failureAction)
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

  public Try<T> ifFailure(final Consumer<? super Throwable> consumer)
  {
    if (isFailure())
      consumer.accept(e);

    return this;
  }

  public Optional<T> getSuccess()
  {
    return Optional.ofNullable(val);
  }

  public Optional<Throwable> getFailure()
  {
    return isFailure()
        ? Optional.of(e)
        : Optional.empty();
  }

  public <U> Optional<Pair<U, Throwable>> getFailurePair()
  {
    return isFailure()
        ? Optional.of(Pair.of(null, e))
        : Optional.empty();
  }

  public Try<T> filter(final Predicate<? super T> predicate)
  {
    if (isFailure())
      return this;
    else
      return Objects.requireNonNull(predicate).test(val)
          ? this
          : FailureWithOriginalVal.failure(val, new IllegalArgumentException(val + " failed predicate " + predicate));
  }

  public <U> Try<U> map(final Function<? super T, ? extends U> mapper)
  {
    return isSuccessful()
        ? of(Objects.requireNonNull(mapper), val)
        : failure(e);
  }

  public <U> Try<U> flatMap(final Function<? super T, ? extends Try<? extends U>> mapper)
  {
    //noinspection unchecked
    return isSuccessful()
        ? (Try<U>) Objects.requireNonNull(mapper).apply(val)
        : failure(e);
  }

  public Try<T> or(final Supplier<Try<T>> supplier)
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

  public T orElseThrow() throws Throwable
  {
    if (isSuccessful())
      return val;
    else
      throw e;
  }

  public T orElseThrow(final Supplier<? extends Throwable> exceptionSupplier) throws Throwable
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

    return obj instanceof Try<?> other
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

  /**
   * A note about this subclass:
   *  This class will only be instantiated in the following cases:
   *    - a failed call to {@link Try#of(Function, Object)}
   *    - a failed call to {@link Try#filter(Predicate)}
   *    - a failed call to {@link Try#map(Function)}
   *  as only in those cases would there be a non-null {@link FailureWithOriginalVal#originalVal}
   */
  private static class FailureWithOriginalVal<T, U> extends Try<T>
  {
    private final U originalVal;

    private FailureWithOriginalVal(final U originalVal, final Throwable e)
    {
      this(null, originalVal, e);
    }
    private FailureWithOriginalVal(final T val, final U originalVal, final Throwable e)
    {
      super(val, e);
      this.originalVal = originalVal;
    }

    private static <T, U> FailureWithOriginalVal<T, U> failure(final U originalVal, final Throwable e)
    {
      return new FailureWithOriginalVal<>(originalVal, e);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Pair<U, Throwable>> getFailurePair()
    {
      return isFailure()
          ? Optional.of(Pair.of(originalVal, e))
          : Optional.empty();
    }
  }
}

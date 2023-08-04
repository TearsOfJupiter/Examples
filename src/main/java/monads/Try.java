package monads;

import functions.arity.VariadicConsumer;
import functions.arity.VariadicFunction;
import functions.util.Pair;
import functions.util.Unchecker;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

public class Try<T>
{
  protected final T val;
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
      return success(Objects.requireNonNull(suppler).get());
    }
    catch (Throwable e)
    {
      return Failure.failure(null, e);
    }
  }
  @SuppressWarnings("unchecked")
  public static <T, U, V extends Try<U>> V of(final Function<? super T, ? extends U> mapper,
                                              final T val)
  {
    try
    {
      return (V) success(Objects.requireNonNull(mapper).apply(val));
    }
    catch (Throwable e)
    {
      return (V) Failure.failure(val, e);
    }
  }
  public static <T> Try<T> of(final Consumer<? super T> consumer,
                              final T val)
  {
    try
    {
      Objects.requireNonNull(consumer).accept(val);
      return success(val);
    }
    catch (Throwable e)
    {
      return Failure.failure(val, e);
    }
  }
  @SafeVarargs
  public static <T, C extends Collection<T>> Try<C> of(final VariadicConsumer<T> consumer,
                                                       final Function<? super T[], ? extends C> collectionFunction,
                                                       final T... vals)
  {
    try
    {
      Objects.requireNonNull(consumer).accept(vals);
      return success(Objects.requireNonNull(collectionFunction).apply(vals));
    }
    catch (Throwable e)
    {
      return Failure.failure(Objects.requireNonNull(collectionFunction).apply(vals), e);
    }
  }
  @SuppressWarnings("unchecked")
  @SafeVarargs
  public static <T, U, V extends Try<U>> V of(final VariadicFunction<T, U> function,
                                              final T... vals)
  {
    try
    {
      return (V) success(Objects.requireNonNull(function).apply(vals));
    }
    catch (Throwable e)
    {
      return (V) Failure.failure(vals, e);
    }
  }

  private static <T> Try<T> success(final T val)
  {
    return new Try<>(val, null);
  }

  public T get()
  {
    return val;
  }

  public boolean isSuccessful()
  {
    return true;
  }

  public boolean isFailure()
  {
    return false;
  }

  public Try<T> ifSuccessful(final Consumer<? super T> consumer)
  {
    Objects.requireNonNull(consumer).accept(val);
    return this;
  }

  public Try<T> ifSuccessfulOrElse(final Consumer<? super T> consumer,
                                   final Consumer<? super T> failureConsumer)
  {
    return ifSuccessful(consumer);
  }
  public Try<T> ifSuccessfulOrElse(final Consumer<? super T> consumer,
                                   final BiConsumer<? super Throwable, ? super T> failureConsumer)
  {
    return ifSuccessful(consumer);
  }

  public Try<T> ifSuccessfulOrElseDo(final Consumer<? super T> consumer,
                                     final Runnable failureAction)
  {
    return ifSuccessful(consumer);
  }

  public Try<T> ifFailure(final Consumer<? super Throwable> consumer)
  {
    return this;
  }

  public Try<T> ifFailure(final BiConsumer<? super Throwable, ? super T> consumer)
  {
    return this;
  }

  public Optional<T> getSuccess()
  {
    return Optional.ofNullable(val);
  }

  public Optional<Throwable> getFailure()
  {
    return Optional.empty();
  }

  public Optional<Pair<T, Throwable>> getFailurePair()
  {
    return Optional.empty();
  }

  public Try<T> filter(final Predicate<? super T> predicate)
  {
    return Objects.requireNonNull(predicate).test(val)
        ? this
        : Failure.failure(val, new IllegalArgumentException(val + " failed predicate " + predicate));
  }

  @SuppressWarnings("unchecked")
  public <U, V extends Try<U>> V map(final Function<? super T, ? extends U> mapper)
  {
    return of(Objects.requireNonNull(mapper), val);
  }

  @SuppressWarnings("unchecked")
  public <U, V extends Try<U>> V flatMap(final Function<? super T, ? extends Try<? extends U>> mapper)
  {
    return (V) Objects.requireNonNull(mapper).apply(val);
  }

  public Try<T> or(final Supplier<Try<T>> supplier)
  {
    return this;
  }

  public Stream<T> stream()
  {
    return Stream.of(val);
  }

  public T orElse(final T alternative)
  {
    return val;
  }

  public T orElseGet(final Supplier<? extends T> supplier)
  {
    return val;
  }

  public T orElseThrow()
  {
    return val;
  }
  public T orElseThrow(final Supplier<? extends Throwable> exceptionSupplier)
  {
    return val;
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
   *    - a failed call to {@link Try#of(Consumer, Object)}
   *    - a failed call to {@link Try#filter(Predicate)}
   *    - a failed call to {@link Try#map(Function)}
   *  as only in those cases would there be a non-null {@link Failure#val}
   */
  private static class Failure<T> extends Try<T>
  {
    private Failure(final T val, final Throwable e)
    {
      super(val, e);
    }

    private static <T> Failure<T> failure(final T val, final Throwable e)
    {
      return new Failure<>(val, e);
    }

    @Override
    public T get()
    {
      throw Unchecker.uncheck(e);
    }

    @Override
    public boolean isSuccessful()
    {
      return false;
    }

    @Override
    public boolean isFailure()
    {
      return true;
    }

    @Override
    public Try<T> ifSuccessful(final Consumer<? super T> consumer)
    {
      return this;
    }

    @Override
    public Try<T> ifSuccessfulOrElse(final Consumer<? super T> consumer,
                                     final Consumer<? super T> failureConsumer)
    {
      Objects.requireNonNull(failureConsumer).accept(val);
      return this;
    }
    @Override
    public Try<T> ifSuccessfulOrElse(final Consumer<? super T> consumer,
                                     final BiConsumer<? super Throwable, ? super T> failureConsumer)
    {
      Objects.requireNonNull(failureConsumer).accept(e, val);
      return this;
    }

    @Override
    public Try<T> ifSuccessfulOrElseDo(final Consumer<? super T> consumer,
                                       final Runnable failureAction)
    {
      Objects.requireNonNull(failureAction).run();
      return this;
    }

    @Override
    public Try<T> ifFailure(final Consumer<? super Throwable> consumer)
    {
      Objects.requireNonNull(consumer).accept(e);
      return this;
    }

    @Override
    public Try<T> ifFailure(final BiConsumer<? super Throwable, ? super T> consumer)
    {
      Objects.requireNonNull(consumer).accept(e, val);
      return this;
    }

    @Override
    public Optional<T> getSuccess()
    {
      return Optional.empty();
    }

    @Override
    public Optional<Throwable> getFailure()
    {
      return Optional.of(e);
    }

    @Override
    public Optional<Pair<T, Throwable>> getFailurePair()
    {
      return Optional.of(Pair.of(val, e));
    }

    @Override
    public Try<T> filter(final Predicate<? super T> predicate)
    {
      return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U, V extends Try<U>> V map(final Function<? super T, ? extends U> mapper)
    {
      return (V) Failure.failure(val, e);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U, V extends Try<U>> V flatMap(final Function<? super T, ? extends Try<? extends U>> mapper)
    {
      return (V) Failure.failure(val, e);
    }

    @Override
    public Try<T> or(final Supplier<Try<T>> supplier)
    {
      return Objects.requireNonNull(supplier).get();
    }

    @Override
    public Stream<T> stream()
    {
      return Stream.empty();
    }

    @Override
    public T orElse(final T alternative)
    {
      return alternative;
    }

    @Override
    public T orElseGet(final Supplier<? extends T> supplier)
    {
      return Objects.requireNonNull(supplier).get();
    }

    @Override
    public T orElseThrow()
    {
      throw Unchecker.uncheck(e);
    }

    @Override
    public T orElseThrow(final Supplier<? extends Throwable> exceptionSupplier)
    {
      throw Unchecker.uncheck(Objects.requireNonNull(exceptionSupplier).get());
    }
  }
}

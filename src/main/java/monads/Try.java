package monads;

import functions.arity.VariadicConsumer;
import functions.arity.VariadicFunction;
import functions.util.ExceptionUtil;
import functions.util.tuples.Pair;

import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

public class Try<T>
{
  protected final T val;

  private Try(final T val)
  {
    this.val = val;
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
      return Failure.fail(e);
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
      return (V) Failure.fail(val, e);
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
      return Failure.fail(val, e);
    }
  }
  @SafeVarargs
  public static <T> Try<T[]> of(final VariadicConsumer<T> consumer,
                                final T... vals)
  {
    try
    {
      Objects.requireNonNull(consumer).accept(vals);
      return success(vals);
    }
    catch (Throwable e)
    {
      return Failure.fail(vals, e);
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
      return (V) Failure.fail(vals, e);
    }
  }

  private static <T> Try<T> success(final T val)
  {
    return new Try<>(val);
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
    try
    {
      Objects.requireNonNull(consumer).accept(val);
      return success(val);
    }
    catch (Throwable e)
    {
      return Failure.fail(val, e);
    }
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

  public Optional<Throwable> getError()
  {
    return Optional.empty();
  }

  public <T1> Optional<Pair<T1, Throwable>> getFailure()
  {
    return Optional.empty();
  }

  public Try<T> filter(final Predicate<? super T> predicate)
  {
    return filter(predicate, () -> new IllegalArgumentException(val + " failed predicate"));
  }
  public Try<T> filter(final Predicate<? super T> predicate,
                       final Supplier<? extends Throwable> throwableSupplier)
  {
    try
    {
      return Objects.requireNonNull(predicate).test(val)
          ? success(val)
          : Failure.fail(val, Objects.requireNonNull(throwableSupplier).get());
    }
    catch (Throwable e)
    {
      return Failure.fail(val, e);
    }
  }

  @SuppressWarnings("unchecked")
  public <U, V extends Try<U>> V map(final Function<? super T, ? extends U> mapper)
  {
    try
    {
      return (V) success(Objects.requireNonNull(mapper).apply(val));
    }
    catch (Throwable e)
    {
      return (V) Failure.fail(val, e);
    }
  }

  @SuppressWarnings("unchecked")
  public <U, V extends Try<U>> V flatMap(final Function<? super T, ? extends V> mapper)
  {
    try
    {
      return Objects.requireNonNull(mapper).apply(val);
    }
    catch (Throwable e)
    {
      return (V) Failure.fail(val, e);
    }
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

  /* *******************************************************************************************************************
   *                                                      FAILURE
   * ***************************************************************************************************************** */

  private static class Failure<T, E extends Throwable> extends Try<T>
  {
    protected final T originalVal;
    protected final E e;

    private Failure(final T originalVal, final E e)
    {
      super(null);
      this.originalVal = originalVal;
      this.e = e;
    }

    protected static <T, E extends Throwable> Failure<T, E> fail(final E e)
    {
      return fail(null, e);
    }
    protected static <T, E extends Throwable> Failure<T, E> fail(final T originalVal, final E e)
    {
      return new Failure<>(originalVal, e);
    }

    @Override
    public T get()
    {
      throw ExceptionUtil.throwAsUnchecked(e);
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
      try
      {
        Objects.requireNonNull(failureConsumer).accept(originalVal);
        return this;
      }
      catch (Throwable e)
      {
        return Failure.fail(originalVal, e);
      }
    }
    @Override
    public Try<T> ifSuccessfulOrElse(final Consumer<? super T> consumer,
                                     final BiConsumer<? super Throwable, ? super T> failureConsumer)
    {
      try
      {
        Objects.requireNonNull(failureConsumer).accept(e, originalVal);
        return this;
      }
      catch (Throwable e)
      {
        return Failure.fail(originalVal, e);
      }
    }

    @Override
    public Try<T> ifSuccessfulOrElseDo(final Consumer<? super T> consumer,
                                       final Runnable failureAction)
    {
      try
      {
        Objects.requireNonNull(failureAction).run();
        return this;
      }
      catch (Throwable e)
      {
        return Failure.fail(originalVal, e);
      }
    }

    @Override
    public Try<T> ifFailure(final Consumer<? super Throwable> consumer)
    {
      try
      {
        Objects.requireNonNull(consumer).accept(e);
        return this;
      }
      catch (Throwable e)
      {
        return Failure.fail(originalVal, e);
      }
    }

    @Override
    public Try<T> ifFailure(final BiConsumer<? super Throwable, ? super T> consumer)
    {
      try
      {
        Objects.requireNonNull(consumer).accept(e, originalVal);
        return this;
      }
      catch (Throwable e)
      {
        return Failure.fail(originalVal, e);
      }
    }

    @Override
    public Optional<T> getSuccess()
    {
      return Optional.empty();
    }

    @Override
    public Optional<Throwable> getError()
    {
      return Optional.of(e);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Pair<T, Throwable>> getFailure()
    {
      return Optional.of(new Pair<>(originalVal, e));
    }

    @Override
    public Try<T> filter(final Predicate<? super T> predicate)
    {
      return this;
    }
    @Override
    public Try<T> filter(final Predicate<? super T> predicate,
                         final Supplier<? extends Throwable> throwableSupplier)
    {
      return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U, V extends Try<U>> V map(final Function<? super T, ? extends U> mapper)
    {
      return (V) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U, V extends Try<U>> V flatMap(final Function<? super T, ? extends V> mapper)
    {
      return (V) this;
    }

    @Override
    public Try<T> or(final Supplier<Try<T>> supplier)
    {
      try
      {
        return Objects.requireNonNull(supplier).get();
      }
      catch (Throwable e)
      {
        return Failure.fail(originalVal, e);
      }
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
      try
      {
        return Objects.requireNonNull(supplier).get();
      }
      catch (Throwable e)
      {
        throw ExceptionUtil.throwAsUnchecked(e);
      }
    }

    @Override
    public T orElseThrow()
    {
      throw ExceptionUtil.throwAsUnchecked(e);
    }

    @Override
    public T orElseThrow(final Supplier<? extends Throwable> exceptionSupplier)
    {
      throw ExceptionUtil.throwAsUnchecked(Objects.requireNonNull(exceptionSupplier).get());
    }
  }
}
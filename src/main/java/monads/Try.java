package monads;

import collections.CollectionUtils;
import functions.arity.VariadicConsumer;
import functions.arity.VariadicFunction;
import util.ExceptionUtil;
import util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

public class Try<T>
{
  protected final T val;
  protected List<Object> states = new ArrayList<>();

  private Try(final T val)
  {
    this.val = val;
    states.add(val);
  }

  public static <T> Try<T> of(final T val)
  {
    return success(val);
  }

  public static <T> Try<T> ofSupplier(final Supplier<? extends T> supplier)
  {
    try
    {
      final T val = Objects.requireNonNull(supplier).get();
      return success(val)
          .withStates(new ArrayList<>(List.of(val)));
    }
    catch (Throwable e)
    {
      return Failure.fail(e, new ArrayList<>());
    }
  }

  @SuppressWarnings("unchecked")
  public static <T, R, V extends Try<R>> V ofFunction(final Function<? super T, ? extends R> mapper,
                                                      final T val)
  {
    try
    {
      final R mapped = Objects.requireNonNull(mapper).apply(val);
      return success(mapped)
          .withStates(new ArrayList<>(List.of(val, mapped)));
    }
    catch (Throwable e)
    {
      return (V) Failure.fail(val, e, new ArrayList<>());
    }
  }

  @SuppressWarnings("unchecked")
  @SafeVarargs
  public static <T, R, V extends Try<R>> V ofVarFunction(final VariadicFunction<T, R> function,
                                                         final T... vals)
  {
    try
    {
      final R mapped = Objects.requireNonNull(function).apply(vals);
      return success(mapped)
          .withStates(CollectionUtils.merge(new ArrayList<>(List.of(vals)), List.of(mapped)));
    }
    catch (Throwable e)
    {
      return (V) Failure.fail(vals, e, new ArrayList<>());
    }
  }

  public static <T> Try<T> ofConsumer(final Consumer<? super T> consumer,
                                      final T val)
  {
    try
    {
      Objects.requireNonNull(consumer).accept(val);
      return success(val)
          .withStates(new ArrayList<>(List.of(val)));
    }
    catch (Throwable e)
    {
      return Failure.fail(val, e, new ArrayList<>());
    }
  }

  @SafeVarargs
  public static <T> Try<T[]> ofVarConsumer(final VariadicConsumer<T> consumer,
                                           final T... vals)
  {
    try
    {
      Objects.requireNonNull(consumer).accept(vals);
      return success(vals)
          .withStates(new ArrayList<>(List.of(vals)));
    }
    catch (Throwable e)
    {
      return Failure.fail(vals, e, new ArrayList<>());
    }
  }

  public static Try<Void> ofRunnable(final Runnable runnable)
  {
    try
    {
      Objects.requireNonNull(runnable).run();
      return success(null);
    }
    catch (Throwable e)
    {
      return Failure.fail(e, new ArrayList<>());
    }
  }

  private static <T> Try<T> success(final T val)
  {
    return new Try<>(val);
  }

  public List<Object> getStates()
  {
    return List.copyOf(states);
  }
  @SuppressWarnings("unchecked")
  protected <V extends Try<T>> V withStates(final List<Object> states)
  {
    this.states = states;
    return (V) this;
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
      return this;
    }
    catch (Throwable e)
    {
      return Failure.fail(val, e, states);
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

  @SuppressWarnings("unchecked")
  public <R, V extends Try<R>> V ifSuccessfulGet(final Supplier<? extends R> supplier)
  {
    try
    {
      return (V) success(Objects.requireNonNull(supplier).get());
    }
    catch (Throwable e)
    {
      return (V) Failure.fail(e, states);
    }
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

  public Optional<Pair<Object, Throwable>> getFailure()
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
              .withStates(states)
          : Failure.fail(val, Objects.requireNonNull(throwableSupplier).get(), states);
    }
    catch (Throwable e)
    {
      return Failure.fail(val, e, states);
    }
  }

  @SuppressWarnings("unchecked")
  public <R, V extends Try<R>> V map(final Function<? super T, ? extends R> mapper)
  {
    try
    {
      final R mapped = Objects.requireNonNull(mapper).apply(val);
      return success(mapped)
          .withStates(CollectionUtils.add(states, mapped));
    }
    catch (Throwable e)
    {
      return (V) Failure.fail(e, states);
    }
  }

  @SuppressWarnings("unchecked")
  public <R, V extends Try<R>> V flatMap(final Function<? super T, ? extends V> mapper)
  {
    try
    {
      final V mappedTry = Objects.requireNonNull(mapper).apply(val);
      return mappedTry
          .withStates(CollectionUtils.merge(states, mappedTry.states));
    }
    catch (Throwable e)
    {
      return (V) Failure.fail(val, e, states);
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

    private Failure(final T originalVal, final E e, final List<Object> states)
    {
      super(null);
      this.originalVal = originalVal;
      this.e = e;
      this.states = states;
      this.states.add(new Pair<>(originalVal, e));
    }

    protected static <T, E extends Throwable> Failure<T, E> fail(final E e, final List<Object> states)
    {
      return fail(null, e, states);
    }
    protected static <T, E extends Throwable> Failure<T, E> fail(final T originalVal, final E e, final List<Object> states)
    {
      return new Failure<>(originalVal, e, states);
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
        return Failure.fail(originalVal, e, states);
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
        return Failure.fail(originalVal, e, states);
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
        return Failure.fail(originalVal, e, states);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R, V extends Try<R>> V ifSuccessfulGet(final Supplier<? extends R> supplier)
    {
      return (V) this;
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
        return Failure.fail(originalVal, e, states);
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
        return Failure.fail(originalVal, e, states);
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
    public Optional<Pair<Object, Throwable>> getFailure()
    {
      return Optional.of((Pair<Object, Throwable>) states.get(states.size() - 1));
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
    public <R, V extends Try<R>> V map(final Function<? super T, ? extends R> mapper)
    {
      return (V) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R, V extends Try<R>> V flatMap(final Function<? super T, ? extends V> mapper)
    {
      return (V) this;
    }

    @Override
    public Try<T> or(final Supplier<Try<T>> supplier)
    {
      try
      {
        final Try<T> altTry = Objects.requireNonNull(supplier).get();
        return altTry
            .withStates(CollectionUtils.merge(states, altTry.states));
      }
      catch (Throwable e)
      {
        return Failure.fail(originalVal, e, new ArrayList<>());
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
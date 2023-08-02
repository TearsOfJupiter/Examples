package monads;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Try<T, E extends Throwable>
{
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
    return new Success<>(val);
  }
  private static <T, E extends Throwable> Try<T, E> failure(final E e)
  {
    return new Failure<>(e);
  }

  public abstract boolean isSuccessful();
  public abstract boolean isFailure();
  public abstract Try<T, E> onSuccess(final Consumer<? super T> consumer);
  public abstract Try<T, E> onSuccessOrElse(final Consumer<? super T> consumer, final Runnable failureAction);
  public abstract Try<T, E> onFailure(final Consumer<? super E> consumer);
  public abstract T get() throws E;
  public abstract T orElse(final T alternative);
  public abstract T orElseGet(final Supplier<? extends T> supplier);
  public abstract T orElseThrow() throws E;
  public abstract Try<T, E> filter(final Predicate<? super T> predicate);
  public abstract <U> Try<U, E> map(final Function<? super T, ? extends U> mapper);
  public abstract <U> Try<U, E> flatMap(final Function<? super T, ? extends Try<? extends U, ? extends E>> mapper);

  private static class Success<T, E extends Throwable> extends Try<T, E>
  {
    private final T val;

    private Success(final T val)
    {
      this.val = val;
    }

    @Override
    public boolean isSuccessful()
    {
      return true;
    }

    @Override
    public boolean isFailure()
    {
      return false;
    }

    @Override
    public Try<T, E> onSuccess(final Consumer<? super T> consumer)
    {
      Objects.requireNonNull(consumer).accept(val);
      return this;
    }

    @Override
    public Try<T, E> onSuccessOrElse(final Consumer<? super T> consumer, final Runnable failureAction)
    {
      onSuccess(consumer);
      return this;
    }

    @Override
    public Try<T, E> onFailure(final Consumer<? super E> consumer)
    {
      return this;
    }

    @Override
    public T get()
    {
      return val;
    }

    @Override
    public T orElse(final T alternative)
    {
      return val;
    }

    @Override
    public T orElseGet(final Supplier<? extends T> supplier)
    {
      return val;
    }

    @Override
    public T orElseThrow() throws E
    {
      return val;
    }

    @Override
    public Try<T, E> filter(Predicate<? super T> predicate)
    {
      //noinspection unchecked
      return Objects.requireNonNull(predicate).test(val)
          ? this
          : (Try<T, E>) new Failure<>(new IllegalArgumentException(val + " failed predicate " + predicate));
    }

    @Override
    public <U> Try<U, E> map(final Function<? super T, ? extends U> mapper)
    {
      return of(Objects.requireNonNull(mapper).apply(val));
    }

    @Override
    public <U> Try<U, E> flatMap(Function<? super T, ? extends Try<? extends U, ? extends E>> mapper)
    {
      //noinspection unchecked
      return (Try<U, E>) Objects.requireNonNull(mapper).apply(val);
    }
  }

  private static class Failure<T, E extends Throwable> extends Try<T, E>
  {
    private final E e;

    private Failure(final E e)
    {
      this.e = e;
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
    public Try<T, E> onSuccess(final Consumer<? super T> consumer)
    {
      return this;
    }

    @Override
    public Try<T, E> onSuccessOrElse(Consumer<? super T> consumer, Runnable failureAction)
    {
      failureAction.run();
      return this;
    }

    @Override
    public Try<T, E> onFailure(final Consumer<? super E> consumer)
    {
      Objects.requireNonNull(consumer).accept(e);
      return this;
    }

    @Override
    public T get() throws E
    {
      throw e;
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
    public T orElseThrow() throws E
    {
      throw e;
    }

    @Override
    public Try<T, E> filter(final Predicate<? super T> predicate)
    {
      return this;
    }

    @Override
    public <U> Try<U, E> map(final Function<? super T, ? extends U> mapper)
    {
      return new Failure<>(e);
    }

    @Override
    public <U> Try<U, E> flatMap(Function<? super T, ? extends Try<? extends U, ? extends E>> mapper)
    {
      return new Failure<>(e);
    }
  }
}

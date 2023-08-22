package monads;

import util.ExceptionUtil;
import util.tuples.Pair;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

public class BiOptional<A, B>
{
  private static final BiOptional<?, ?> EMPTY = new BiOptional<>(null, null);

  private final A a;
  private final B b;

  private BiOptional(final A a, final B b)
  {
    this.a = a;
    this.b = b;
  }

  public static <A, B> BiOptional<A, B> empty()
  {
    //noinspection unchecked
    return (BiOptional<A, B>) EMPTY;
  }

  public static <A, B> BiOptional<A, B> of(final A a, final B b)
  {
    return new BiOptional<>(Objects.requireNonNull(a), Objects.requireNonNull(b));
  }

  public static <A, B> BiOptional<A, B> ofNullable(final A a, final B b)
  {
    if (Objects.isNull(a) || Objects.isNull(b))
      return empty();

    return new BiOptional<>(a, b);
  }

  public Pair<A, B> get()
  {
    if (isEmpty())
      throw new NoSuchElementException("Value(s) not present");

    return new Pair<>(a, b);
  }

  public boolean isPresent()
  {
    return isAPresent()
        && isBPresent();
  }

  public boolean isAPresent()
  {
    return Objects.nonNull(a);
  }

  public boolean isBPresent()
  {
    return Objects.nonNull(b);
  }

  public boolean isEmpty()
  {
    return !isPresent();
  }

  public boolean isAEmpty()
  {
    return !isAPresent();
  }

  public boolean isBEmpty()
  {
    return !isBPresent();
  }

  public BiOptional<A, B> ifPresent(final BiConsumer<? super A, ? super B> consumer)
  {
    if (isPresent())
      Objects.requireNonNull(consumer).accept(a, b);

    return this;
  }

  public BiOptional<A, B> ifAPresent(final Consumer<? super A> aConsumer)
  {
    if (isAPresent())
      Objects.requireNonNull(aConsumer).accept(a);

    return this;
  }

  public BiOptional<A, B> ifBPresent(final Consumer<? super B> bConsumer)
  {
    if (isBPresent())
      Objects.requireNonNull(bConsumer).accept(b);

    return this;
  }

  public BiOptional<A, B> filter(final BiPredicate<? super A, ? super B> biPredicate)
  {
    return When.<BiOptional<A, B>, BiOptional<A, B>>
         newWhenThenGet(
            BiOptional::isEmpty,
            () -> this)
        .whenThenGet(
            biop -> Objects.requireNonNull(biPredicate).test(biop.a, biop.b),
            () -> this)
        .elseGet(
            BiOptional::empty)
        .test(this)
        .get();
  }

  public BiOptional<A, B> filterA(final Predicate<? super A> aPredicate)
  {
    return When.<BiOptional<A, B>, BiOptional<A, B>>
         newWhenThenGet(
            BiOptional::isEmpty,
            () -> this)
        .whenThenGet(
            biop -> Objects.requireNonNull(aPredicate).test(biop.a),
            () -> this)
        .elseGet(
            BiOptional::empty)
        .test(this)
        .get();
  }

  public BiOptional<A, B> filterB(final Predicate<? super B> bPredicate)
  {
    return When.<BiOptional<A, B>, BiOptional<A, B>>
         newWhenThenGet(
            BiOptional::isEmpty,
            () -> this)
        .whenThenGet(
            biop -> Objects.requireNonNull(bPredicate).test(biop.b),
            () -> this)
        .elseGet(
            BiOptional::empty)
        .test(this)
        .get();
  }

  public <T, U> BiOptional<T, U> map(final Function<? super A, ? extends T> aMapper,
                                     final Function<? super B, ? extends U> bMapper)
  {
    return isEmpty()
        ? empty()
        : BiOptional.ofNullable(
            Objects.requireNonNull(aMapper).apply(a),
            Objects.requireNonNull(bMapper).apply(b)
          );
  }

  public <R> BiOptional<R, B> mapA(final Function<? super A, ? extends R> mapper)
  {
    return isEmpty()
        ? empty()
        : BiOptional.ofNullable(Objects.requireNonNull(mapper).apply(a), b);
  }

  public <R> BiOptional<A, R> mapB(final Function<? super B, ? extends R> mapper)
  {
    return isEmpty()
        ? empty()
        : BiOptional.ofNullable(a, Objects.requireNonNull(mapper).apply(b));
  }

  public <R> Optional<R> mapToOptional(final BiFunction<? super A, ? super B, ? extends R> mapper)
  {
    return isEmpty()
        ? Optional.empty()
        : Optional.ofNullable(Objects.requireNonNull(mapper).apply(a, b));
  }

  public <U, V> BiOptional<U, V> flatMap(final BiFunction<? super A, ? super B, ? extends BiOptional<U, V>> mapper)
  {
    return isEmpty()
        ? empty()
        : Objects.requireNonNull(Objects.requireNonNull(mapper).apply(a, b));
  }

  public <R> BiOptional<R, B> flatMapA(final Function<? super A, ? extends Optional<R>> aMapper)
  {
    return isEmpty()
        ? empty()
        : Objects.requireNonNull(aMapper).apply(a)
            .map(r -> BiOptional.ofNullable(r, b))
            .orElseGet(BiOptional::empty);
  }

  public <R> BiOptional<A, R> flatMapB(final Function<? super B, ? extends Optional<R>> bMapper)
  {
    return isEmpty()
        ? empty()
        : Objects.requireNonNull(bMapper).apply(b)
            .map(r -> BiOptional.ofNullable(a, r))
            .orElseGet(BiOptional::empty);
  }

  public BiOptional<A, B> or(final Supplier<? extends BiOptional<? extends A, ? extends B>> supplier)
  {
    //noinspection unchecked
    return isPresent()
        ? this
        : (BiOptional<A, B>) Objects.requireNonNull(supplier).get();
  }

  public Stream<Pair<A, B>> stream()
  {
    return isEmpty()
        ? Stream.empty()
        : Stream.of(get());
  }

  public Stream<A> streamA()
  {
    return isAEmpty()
        ? Stream.empty()
        : Stream.of(a);
  }

  public Stream<B> streamB()
  {
    return isBEmpty()
        ? Stream.empty()
        : Stream.of(b);
  }

  public Pair<A, B> orElse(final Pair<A, B> others)
  {
    return isPresent()
        ? get()
        : others;
  }

  public Pair<A, B> orElseGet(final Supplier<Pair<? extends A, ? extends B>> otherSupplier)
  {
    //noinspection unchecked
    return isPresent()
        ? get()
        : (Pair<A, B>) Objects.requireNonNull(otherSupplier).get();
  }

  public Pair<A, B> orElseThrow()
  {
    return orElseThrow(() -> new NoSuchElementException("Value(s) not present"));
  }

  public Pair<A, B> orElseThrow(final Supplier<? extends Throwable> errorSupplier)
  {
    if (isEmpty())
      throw ExceptionUtil.throwAsUnchecked(Objects.requireNonNull(errorSupplier).get());

    return get();
  }

  public A orElseA(final A otherA)
  {
    return isAPresent()
        ? a
        : otherA;
  }

  public A orElseGetA(final Supplier<? extends A> aSupplier)
  {
    return isAPresent()
        ? a
        : Objects.requireNonNull(aSupplier).get();
  }

  public A orElseThrowIfA()
  {
    return orElseThrowIfA(() -> new NoSuchElementException("\"A\" value not present"));
  }

  public A orElseThrowIfA(final Supplier<? extends Throwable> errorSupplier)
  {
    if (isAEmpty())
      throw ExceptionUtil.throwAsUnchecked(Objects.requireNonNull(errorSupplier).get());

    return a;
  }

  public B orElseB(final B otherB)
  {
    return isBPresent()
        ? b
        : otherB;
  }

  public B orElseGetB(final Supplier<? extends B> bSupplier)
  {
    return isPresent()
        ? b
        : Objects.requireNonNull(bSupplier).get();
  }

  public B orElseThrowIfB()
  {
    return orElseThrowIfB(() -> new NoSuchElementException("\"B\" value not present"));
  }

  public B orElseThrowIfB(final Supplier<? extends Throwable> errorSupplier)
  {
    if (isBEmpty())
      throw ExceptionUtil.throwAsUnchecked(Objects.requireNonNull(errorSupplier).get());

    return b;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;

    return obj instanceof BiOptional<?,?> other
        && Objects.equals(a, other.a)
        && Objects.equals(b, other.b);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(a, b);
  }

  @Override
  public String toString()
  {
    return isPresent()
        ? "BiOptional[" + a + ", " + b + "]"
        : "BiOptional.empty";
  }
}
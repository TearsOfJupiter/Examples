package monads;

import collections.CollectionUtils;
import util.Builder;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class When<T, U>
{
  private Collection<When<T, U>> cases;
  private Predicate<? super T> predicate;
  private Runnable action;
  private Supplier<U> supplier;
  private Consumer<U> consumer;
  private Function<T, U> function;

  private When()
  {
    this.cases = new ArrayDeque<>();
  }
  private When(final Predicate<? super T> predicate)
  {
    this.predicate = predicate;
  }

  private Deque<When<T, U>> getCasesDeque()
  {
    return (Deque<When<T, U>>) cases;
  }
  private List<When<T, U>> getCasesList()
  {
    return (List<When<T, U>>) cases;
  }
  private void setCases(final Deque<When<T, U>> cases)
  {
    this.cases = cases;
  }

  private void lockCases()
  {
    cases = cases.stream().toList();
  }

  private When<T, U> getLastCase()
  {
    return getCasesList().get(cases.size() - 1);
  }

  private boolean hasPredicate()
  {
    return Objects.nonNull(predicate);
  }

  private When<T, U> withAction(final Runnable action)
  {
    this.action = action;
    return this;
  }

  private When<T, U> withSupplier(final Supplier<U> supplier)
  {
    this.supplier = supplier;
    return this;
  }

  private When<T, U> withConsumer(final Consumer<U> consumer)
  {
    this.consumer = consumer;
    return this;
  }

  private When<T, U> withFunction(final Function<T, U> function)
  {
    this.function = function;
    return this;
  }

  public static <T, U> When<T, U> newWhen(final Predicate<? super T> predicate)
  {
    final Deque<When<T, U>> queue = new ArrayDeque<>();
    queue.add(new When<>(predicate));
    return Builder.<When<T, U>>of(When::new)
        .with(When::setCases, queue)
        .build();
  }

  public static <T, U> When<T, U> newWhenThenDo(final Predicate<? super T> predicate,
                                                final Runnable action)
  {
    final Deque<When<T, U>> queue = new ArrayDeque<>();
    queue.add(new When<T, U>(predicate).withAction(action));
    return Builder.<When<T, U>>of(When::new)
        .with(When::setCases, queue)
        .build();
  }

  public static <T, U> When<T, U> newWhenThenGet(final Predicate<? super T> predicate,
                                                 final Supplier<U> supplier)
  {
    final Deque<When<T, U>> queue = new ArrayDeque<>();
    queue.add(new When<T, U>(predicate).withSupplier(supplier));
    return Builder.<When<T, U>>of(When::new)
        .with(When::setCases, queue)
        .build();
  }

  public static <T, U> When<T, U> newWhenThenAccept(final Predicate<? super T> predicate,
                                                    final Consumer<U> consumer)
  {
    final Deque<When<T, U>> queue = new ArrayDeque<>();
    queue.add(new When<T, U>(predicate).withConsumer(consumer));
    return Builder.<When<T, U>>of(When::new)
        .with(When::setCases, queue)
        .build();
  }

  public static <T, U> When<T, U> newWhenThenApply(final Predicate<? super T> predicate,
                                                   final Function<T, U> function)
  {
    final Deque<When<T, U>> queue = new ArrayDeque<>();
    queue.add(new When<T, U>(predicate).withFunction(function));
    return Builder.<When<T, U>>of(When::new)
        .with(When::setCases, queue)
        .build();
  }

  public When<T, U> when(final Predicate<? super T> predicate)
  {
    CollectionUtils.add(cases, new When<>(predicate));
    return this;
  }

  public When<T, U> whenThenDo(final Predicate<? super T> predicate,
                               final Runnable action)
  {
    CollectionUtils.add(cases, new When<T, U>(predicate).withAction(action));
    return this;
  }

  public When<T, U> whenThenGet(final Predicate<? super T> predicate,
                                final Supplier<U> supplier)
  {
    CollectionUtils.add(cases, new When<T, U>(predicate).withSupplier(supplier));
    return this;
  }

  public When<T, U> whenThenAccept(final Predicate<? super T> predicate,
                                   final Consumer<U> consumer)
  {
    CollectionUtils.add(cases, new When<T, U>(predicate).withConsumer(consumer));
    return this;
  }

  public When<T, U> whenThenApply(final Predicate<? super T> predicate,
                                  final Function<T, U> function)
  {
    CollectionUtils.add(cases, new When<T, U>(predicate).withFunction(function));
    return this;
  }

  public When<T, U> thenDo(final Runnable runnable)
  {
    getCasesDeque().getLast().action = runnable;
    return this;
  }

  public When<T, U> thenGet(final Supplier<U> supplier)
  {
    getCasesDeque().getLast().supplier = supplier;
    return this;
  }

  public When<T, U> thenAccept(final Consumer<U> consumer)
  {
    getCasesDeque().getLast().consumer = consumer;
    return this;
  }

  public When<T, U> thenApply(final Function<T, U> function)
  {
    getCasesDeque().getLast().function = function;
    return this;
  }

  public When<T, U> elseDo(final Runnable action)
  {
    cases.add(new When<T, U>().withAction(action));
    lockCases();
    return this;
  }

  public When<T, U> elseGet(final Supplier<U> supplier)
  {
    cases.add(new When<T, U>().withSupplier(supplier));
    lockCases();
    return this;
  }

  public When<T, U> elseAccept(final Consumer<U> consumer)
  {
    cases.add(new When<T, U>().withConsumer(consumer));
    lockCases();
    return this;
  }

  public When<T, U> elseApply(final Function<T, U> function)
  {
    cases.add(new When<T, U>().withFunction(function));
    lockCases();
    return this;
  }

  public When<T, U> test(final T subject)
  {
    final Optional<When<T, U>> elseOptional = Optional.of(getLastCase())
        .filter(lastCase -> lastCase.predicate == null);

    return cases.stream()
        .filter(When::hasPredicate)
        .filter(w -> w.predicate.test(subject))
        .findFirst()
        .or(() -> elseOptional)
        .orElseThrow(() -> new IllegalStateException("No cases matched, and no else was defined"));
  }

  public void doAction()
  {
    Objects.requireNonNull(action).run();
  }

  public U get()
  {
    return Objects.requireNonNull(supplier).get();
  }

  public void accept(final U u)
  {
    Objects.requireNonNull(consumer).accept(u);
  }

  public U apply(final T t)
  {
    return Objects.requireNonNull(function).apply(t);
  }
}
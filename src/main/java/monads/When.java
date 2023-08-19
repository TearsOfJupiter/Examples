package monads;

import collections.CollectionUtils;
import util.Builder;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class When<T, R>
{
  private Collection<When<T, R>> cases;
  private Predicate<? super T> predicate;
  private Runnable action;
  private Supplier<R> supplier;
  private Consumer<R> consumer;
  private Function<T, R> function;
  private T switchOn;

  private When()
  {
    this.cases = new ArrayDeque<>();
  }
  private When(final Predicate<? super T> predicate)
  {
    this.predicate = predicate;
  }
  private When(final T switchOn)
  {
    this();
    this.switchOn = switchOn;
  }

  /* *******************************************************************************************************************
   *                                              CASES METHODS
   * ***************************************************************************************************************** */

  private Deque<When<T, R>> getCasesDeque()
  {
    return Optional.of(cases)
        .filter(Deque.class::isInstance)
        .map(c -> (Deque<When<T, R>>) c)
        .orElseGet(() -> new ArrayDeque<>(cases));
  }
  private List<When<T, R>> getCasesList()
  {
    return Optional.of(cases)
        .filter(List.class::isInstance)
        .map(c -> (List<When<T, R>>) c)
        .orElseGet(() -> cases.stream()
            .toList());
  }
  private void setCases(final Deque<When<T, R>> cases)
  {
    this.cases = cases;
  }

  private void lockCases()
  {
    cases = cases.stream().toList();
  }

  private When<T, R> getLastCase()
  {
    return getCasesList().get(cases.size() - 1);
  }

  /* *******************************************************************************************************************
   *                                              FUNCTIONS METHODS
   * ***************************************************************************************************************** */

  private boolean hasPredicate()
  {
    return Objects.nonNull(predicate);
  }

  private boolean hasSwitchOn()
  {
    return Objects.nonNull(switchOn);
  }

  private When<T, R> withAction(final Runnable action)
  {
    this.action = action;
    return this;
  }

  private When<T, R> withSupplier(final Supplier<R> supplier)
  {
    this.supplier = supplier;
    return this;
  }

  private When<T, R> withConsumer(final Consumer<R> consumer)
  {
    this.consumer = consumer;
    return this;
  }

  private When<T, R> withFunction(final Function<T, R> function)
  {
    this.function = function;
    return this;
  }

  /* *******************************************************************************************************************
   *                                              WHEN METHODS
   * ***************************************************************************************************************** */

  public static <T, R> When<T, R> newWhen(final Predicate<? super T> predicate)
  {
    final Deque<When<T, R>> queue = new ArrayDeque<>();
    queue.add(new When<>(predicate));
    return Builder.<When<T, R>>of(When::new)
        .with(When::setCases, queue)
        .build();
  }

  public static <T, R> When<T, R> newWhenThenDo(final Predicate<? super T> predicate,
                                                final Runnable action)
  {
    final Deque<When<T, R>> queue = new ArrayDeque<>();
    queue.add(new When<T, R>(predicate).withAction(action));
    return Builder.<When<T, R>>of(When::new)
        .with(When::setCases, queue)
        .build();
  }

  public static <T, R> When<T, R> newWhenThenGet(final Predicate<? super T> predicate,
                                                 final Supplier<R> supplier)
  {
    final Deque<When<T, R>> queue = new ArrayDeque<>();
    queue.add(new When<T, R>(predicate).withSupplier(supplier));
    return Builder.<When<T, R>>of(When::new)
        .with(When::setCases, queue)
        .build();
  }

  public static <T, R> When<T, R> newWhenThenAccept(final Predicate<? super T> predicate,
                                                    final Consumer<R> consumer)
  {
    final Deque<When<T, R>> queue = new ArrayDeque<>();
    queue.add(new When<T, R>(predicate).withConsumer(consumer));
    return Builder.<When<T, R>>of(When::new)
        .with(When::setCases, queue)
        .build();
  }

  public static <T, R> When<T, R> newWhenThenApply(final Predicate<? super T> predicate,
                                                   final Function<T, R> function)
  {
    final Deque<When<T, R>> queue = new ArrayDeque<>();
    queue.add(new When<T, R>(predicate).withFunction(function));
    return Builder.<When<T, R>>of(When::new)
        .with(When::setCases, queue)
        .build();
  }

  public When<T, R> when(final Predicate<? super T> predicate)
  {
    CollectionUtils.add(cases, new When<>(predicate));
    return this;
  }

  public When<T, R> whenThenDo(final Predicate<? super T> predicate,
                               final Runnable action)
  {
    CollectionUtils.add(cases, new When<T, R>(predicate).withAction(action));
    return this;
  }

  public When<T, R> whenThenGet(final Predicate<? super T> predicate,
                                final Supplier<R> supplier)
  {
    CollectionUtils.add(cases, new When<T, R>(predicate).withSupplier(supplier));
    return this;
  }

  public When<T, R> whenThenAccept(final Predicate<? super T> predicate,
                                   final Consumer<R> consumer)
  {
    CollectionUtils.add(cases, new When<T, R>(predicate).withConsumer(consumer));
    return this;
  }

  public When<T, R> whenThenApply(final Predicate<? super T> predicate,
                                  final Function<T, R> function)
  {
    CollectionUtils.add(cases, new When<T, R>(predicate).withFunction(function));
    return this;
  }

  /* *******************************************************************************************************************
   *                                              THEN METHODS
   * ***************************************************************************************************************** */

  public When<T, R> thenDo(final Runnable runnable)
  {
    getCasesDeque().getLast().action = runnable;
    return this;
  }

  public When<T, R> thenGet(final Supplier<R> supplier)
  {
    getCasesDeque().getLast().supplier = supplier;
    return this;
  }

  public When<T, R> thenAccept(final Consumer<R> consumer)
  {
    getCasesDeque().getLast().consumer = consumer;
    return this;
  }

  public When<T, R> thenApply(final Function<T, R> function)
  {
    getCasesDeque().getLast().function = function;
    return this;
  }

  /* *******************************************************************************************************************
   *                                              ELSE METHODS
   * ***************************************************************************************************************** */

  public When<T, R> elseDo(final Runnable action)
  {
    cases.add(new When<T, R>().withAction(action));
    lockCases();
    return this;
  }

  public When<T, R> elseGet(final Supplier<R> supplier)
  {
    cases.add(new When<T, R>().withSupplier(supplier));
    lockCases();
    return this;
  }

  public When<T, R> elseAccept(final Consumer<R> consumer)
  {
    cases.add(new When<T, R>().withConsumer(consumer));
    lockCases();
    return this;
  }

  public When<T, R> elseApply(final Function<T, R> function)
  {
    cases.add(new When<T, R>().withFunction(function));
    lockCases();
    return this;
  }

  /* *******************************************************************************************************************
   *                                              TEST METHOD
   * ***************************************************************************************************************** */

  public When<T, R> test(final T subject)
  {
    final Optional<When<T, R>> elseOptional = Optional.of(getLastCase())
        .filter(lastCase -> lastCase.predicate == null);

    return cases.stream()
        .filter(When::hasPredicate)
        .filter(w -> w.predicate.test(subject))
        .findFirst()
        .or(() -> elseOptional)
        .orElseThrow(() -> new IllegalStateException("No cases matched, and no else was defined"));
  }

  /* *******************************************************************************************************************
   *                                              TERMINAL METHODS
   * ***************************************************************************************************************** */

  public void doAction()
  {
    Objects.requireNonNull(action).run();
  }

  public R get()
  {
    return Objects.requireNonNull(supplier).get();
  }

  public void accept(final R r)
  {
    Objects.requireNonNull(consumer).accept(r);
  }

  public R apply(final T t)
  {
    return Objects.requireNonNull(function).apply(t);
  }

  /* *******************************************************************************************************************
   *                                              SWITCH METHODS
   * ***************************************************************************************************************** */

  public static <T, R> When<T, R> switchOn(final T switchOn)
  {
    return Builder.<When<T, R>>of(() -> new When<>(switchOn))
        .with(When::setCases, new ArrayDeque<When<T, R>>())
        .build();
  }

  public When<T, R> withCase(final T switchOn)
  {
    final When<T, R> when = new When<>();
    when.switchOn = switchOn;
    cases.add(when);
    return this;
  }
  public When<T, R> withCaseThenGet(final T switchOn,
                                    final Supplier<R> supplier)
  {
    final When<T, R> when = new When<>();
    when.switchOn = switchOn;
    when.supplier = supplier;
    cases.add(when);
    return this;
  }

  //todo: add withCaseThenDo, withCaseThenAccept, withCaseThenApply

  public When<T, R> testSwitch()
  {
    final Optional<When<T, R>> defaultOptional = Optional.of(getLastCase())
        .filter(lastCase -> lastCase.switchOn == null);

    return cases.stream()
        .filter(When::hasSwitchOn)
        .filter(w -> Objects.equals(w.switchOn, this.switchOn))
        .findFirst()
        .or(() -> defaultOptional)
        .orElseThrow(() -> new IllegalStateException("No cases matched, and no default was defined"));
  }
}
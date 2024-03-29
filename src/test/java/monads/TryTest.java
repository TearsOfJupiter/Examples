package monads;

import functions.arity.VariadicConsumer;
import functions.arity.VariadicFunction;
import org.apache.commons.lang3.function.*;
import org.testng.annotations.Test;
import util.tuples.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

@SuppressWarnings({"divzero", "NumericOverflow", "PointlessArithmeticExpression"})
public class TryTest
{
  private static final String KABLOOEY = "kablooey!";
  private static final RuntimeException KABLOOEY_EXCEPTION = new RuntimeException(KABLOOEY);
  private static final FailableConsumer<Integer, Exception> KABLOOEY_CONSUMER = i -> {throw KABLOOEY_EXCEPTION;};
  private static final FailableSupplier<Integer, Exception> KABLOOEY_SUPPLIER = () -> {throw KABLOOEY_EXCEPTION;};
  private static final FailableBiConsumer<Throwable, Integer, Exception> KABLOOEY_BICONSUMER = (e, i) -> {throw KABLOOEY_EXCEPTION;};
  private static final VariadicConsumer<Integer> KABLOOEY_VARCONSUMER = vals -> {throw KABLOOEY_EXCEPTION;};
  private static final FailableSupplier<Integer, Exception> KABLOOEY_INTSUPPLIER = () -> {throw KABLOOEY_EXCEPTION;};
  private static final FailableSupplier<Try<Integer>, Exception> KABLOOEY_TRYSUPPLIER = () -> {throw KABLOOEY_EXCEPTION;};
  private static final FailableFunction<Integer, Integer, Exception> KABLOOEY_FUNCTION = i -> {throw KABLOOEY_EXCEPTION;};
  private static final VariadicFunction<Integer, String> KABLOOEY_VARFUNCTION = (Integer... array) -> {throw KABLOOEY_EXCEPTION;};
  private static final FailablePredicate<Integer, Exception> KABLOOEY_PREDICATE = i -> {throw KABLOOEY_EXCEPTION;};
  private static final FailableRunnable<Exception> KABLOOEY_RUNNABLE = () -> {throw KABLOOEY_EXCEPTION;};
  private static final FailableSupplier<Throwable, Exception> SUPPLY_KABLOOEY = () -> KABLOOEY_EXCEPTION;
  public static final FailableRunnable<Exception> GOOD_RUNNABLE = () -> {};
  private static final FailableFunction<Integer, Try<Integer>, Exception> GOOD_FLATMAP = val -> Try.ofFunction(i -> i / 2, val);
  private static final FailableFunction<Integer, Try<Integer>, Exception> BAD_FLATMAP = val -> Try.ofFunction(i -> i / 0, val);

  /**
   * Demonstrates {@link Try#of(Object)}
   */
  @Test
  public void test_of()
  {
    final int val = 1;
    final Try<Integer> t = Try.of(val);
    assertTrue(t.isSuccessful());
    assertFalse(t.isFailure());
    assertEquals(t.orElseThrow(), val);
    assertEquals(t.getStates(), List.of(1));
  }

  /**
   * Demonstrates {@link Try#ofSupplier(FailableSupplier)}
   */
  @Test
  public void test_ofSupplier()
  {
    final int val = 1;
    final Try<Integer> ts = Try.ofSupplier(() -> val);
    assertTrue(ts.isSuccessful());
    assertFalse(ts.isFailure());
    assertTrue(ts.getFailure().isEmpty());
    assertEquals((int) ts.orElseThrow(), val);
    assertEquals(ts.getStates(), List.of(1));

    final Try<Integer> tf = Try.ofSupplier(KABLOOEY_SUPPLIER);
    assertFalse(tf.isSuccessful());
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> pair = tf.getFailure().orElseThrow();
    assertNull(pair.getA());
    assertEquals(pair.getB(), KABLOOEY_EXCEPTION);
  }

  /**
   * Demonstrates {@link Try#ofFunction(FailableFunction, Object)}
   */
  @Test
  public void test_ofFunction()
  {
    final int val = 1;
    final Try<Integer> ts = Try.ofFunction(i -> i / 1, val);
    assertTrue(ts.isSuccessful());
    assertFalse(ts.isFailure());
    assertTrue(ts.getFailure().isEmpty());
    assertEquals((int) ts.orElseThrow(), val);
    assertEquals(ts.getStates(), List.of(1, 1));

    final Try<Integer> tf = Try.ofFunction(i -> Integer.parseInt(i) / 0, String.valueOf(val));
    assertFalse(tf.isSuccessful());
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> pair = tf.getFailure().orElseThrow();
    assertEquals(String.valueOf(val), pair.getA());
    assertTrue(pair.getB() instanceof ArithmeticException);
  }

  /**
   * Demonstrates {@link Try#ofVarFunction(VariadicFunction, Object[])}
   */
  @Test
  public void test_ofVarFunction()
  {
    final VariadicFunction<Integer, String> goodFunction = (Integer... array) -> Arrays.stream(array)
        .map(String::valueOf)
        .collect(Collectors.joining(","));

    final Try<String> ts = Try.ofVarFunction(goodFunction, 1, 2, 3);
    assertTrue(ts.isSuccessful());
    assertFalse(ts.isFailure());
    assertTrue(ts.getFailure().isEmpty());
    assertEquals(ts.orElseThrow(), "1,2,3");
    assertEquals(ts.getStates(), List.of(1, 2, 3, "1,2,3"));

    final Try<String> tf = Try.ofVarFunction(KABLOOEY_VARFUNCTION, 1, 2, 3);
    assertFalse(tf.isSuccessful());
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> pair = tf.getFailure().orElseThrow();
    final List<Integer> tfVals = Arrays.asList((Integer[]) pair.getA());
    assertEquals(List.of(1, 2, 3), tfVals);
    assertEquals(pair.getB().getMessage(), KABLOOEY);
  }

  /**
   * Demonstrates {@link Try#ofConsumer(FailableConsumer, Object)}
   */
  @Test
  public void test_ofConsumer()
  {
    final Set<Integer> ints = new HashSet<>();

    final Try<Integer> ts = Try.ofConsumer(ints::add, 1);
    assertTrue(ts.isSuccessful());
    assertFalse(ts.isFailure());
    assertTrue(ts.getFailure().isEmpty());
    assertTrue(ints.contains(1));
    assertEquals((int) ts.orElseThrow(), 1);
    assertEquals(ts.getStates(), List.of(1));

    final Try<Integer> tf = Try.ofConsumer(KABLOOEY_CONSUMER, 1);
    assertFalse(tf.isSuccessful());
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> pair = tf.getFailure().orElseThrow();
    assertEquals((int) pair.getA(), 1);
    assertEquals(pair.getB().getMessage(), KABLOOEY);
  }

  /**
   * Demonstrates {@link Try#ofVarConsumer(VariadicConsumer, Object[])}
   */
  @Test
  public void test_ofVarConsumer()
  {
    final List<Integer> ints = new ArrayList<>();
    final VariadicConsumer<Integer> goodConsumer = vals -> ints.addAll(Arrays.asList(vals));

    final Try<Integer[]> ts = Try.ofVarConsumer(goodConsumer, 1, 2, 3);
    assertTrue(ts.isSuccessful());
    assertFalse(ts.isFailure());
    assertTrue(ts.getFailure().isEmpty());
    final List<Integer> tsVals = Arrays.asList(ts.orElseThrow());
    assertEquals(List.of(1, 2, 3), tsVals);
    assertEquals(ints, tsVals);
    assertEquals(ts.getStates(), List.of(1, 2, 3));

    final Try<Integer[]> tf = Try.ofVarConsumer(KABLOOEY_VARCONSUMER, 1, 2, 3);
    assertFalse(tf.isSuccessful());
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> pair = tf.getFailure().orElseThrow();
    final List<Integer> tfVals = Arrays.asList((Integer[]) pair.getA());
    assertEquals(tsVals, tfVals);
    assertEquals(pair.getB().getMessage(), KABLOOEY);
  }

  /**
   * Demonstrates {@link Try#ofRunnable(FailableRunnable)}
   */
  @Test
  public void test_ofRunnable()
  {
    // Good runnable
    final Try<Void> ts = Try.ofRunnable(GOOD_RUNNABLE);
    assertTrue(ts.isSuccessful());

    // Bad runnable
    final Try<Void> tf = Try.ofRunnable(KABLOOEY_RUNNABLE);
    assertTrue(tf.isFailure());
    assertEquals(tf.getError().orElseThrow().getMessage(), KABLOOEY);
  }

  /**
   * Demonstrates {@link Try#get()}
   */
  @Test(expectedExceptions = ArithmeticException.class)
  public void test_get()
  {
    final Try<Integer> ts = Try.ofSupplier(() -> 1 / 1);
    assertEquals((int) ts.get(), 1);
    assertEquals(ts.getStates(), List.of(1));

    final Try<Integer> tf = Try.ofSupplier(() -> 1 / 0);
    tf.get();
  }

  /**
   * Demonstrates {@link Try#ifSuccessful(FailableConsumer)}
   */
  @Test
  public void test_ifSuccessful()
  {
    final Set<Integer> ints = new HashSet<>();

    // Good supplier
    Try.ofSupplier(() -> 1 / 1).ifSuccessful(ints::add);
    assertEquals(ints.size(), 1);
    assertTrue(ints.contains(1));

    // Bad supplier
    ints.clear();
    Try.ofSupplier(() -> 1 / 0).ifSuccessful(ints::add);
    assertTrue(ints.isEmpty());

    // Bad ifSuccessful consumer
    final Try<Integer> tf = Try.ofSupplier(() -> 2 / 1).ifSuccessful(KABLOOEY_CONSUMER);
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertEquals(failure.getA(), 2);
    assertEquals(failure.getB().getMessage(), KABLOOEY);
  }

  /**
   * Demonstrates {@link Try#ifSuccessfulOrElse(FailableConsumer, FailableConsumer)}
   */
  @Test
  public void test_ifSuccessfulOrElse_Consumer()
  {
    final Set<Integer> ints = new HashSet<>();
    final Set<Integer> failedDivisors = new HashSet<>();

    // Good consumer
    Try.ofConsumer(i -> {}, 2).ifSuccessfulOrElse(ints::add, failedDivisors::add);
    assertEquals(ints.size(), 1);
    assertTrue(ints.contains(2));

    ints.clear();

    // Bad consumer
    Try.ofConsumer(KABLOOEY_CONSUMER, 0).ifSuccessfulOrElse(ints::add, failedDivisors::add);
    assertEquals(ints.size(), 0);
    assertEquals(failedDivisors.size(), 1);
    assertEquals((int) failedDivisors.iterator().next(), 0);

    failedDivisors.clear();

    // Bad failure consumer
    final Try<Integer> tf = Try.ofConsumer(KABLOOEY_CONSUMER, 10).ifSuccessfulOrElse(ints::add, KABLOOEY_CONSUMER);
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertEquals(failure.getA(), 10);
    assertEquals(failure.getB().getMessage(), KABLOOEY);
    assertTrue(ints.isEmpty());
    assertTrue(failedDivisors.isEmpty());
  }

  /**
   * Demonstrates {@link Try#ifSuccessfulOrElse(FailableConsumer, FailableBiConsumer)}
   */
  @Test
  public void test_ifSuccessfulOrElse_BiConsumer()
  {
    final Set<Integer> ints = new HashSet<>();
    final Set<Pair<Throwable, Integer>> failedDivisors = new HashSet<>();

    // Good consumer
    Try.ofConsumer(i -> {}, 2).ifSuccessfulOrElse(ints::add, (e, i) -> failedDivisors.add(new Pair<>(e, i)));
    assertEquals(ints.size(), 1);
    assertTrue(ints.contains(2));

    ints.clear();

    // Bad consumer
    Try.ofConsumer(KABLOOEY_CONSUMER, 0).ifSuccessfulOrElse(ints::add, (e, i) -> failedDivisors.add(new Pair<>(e, i)));
    assertEquals(ints.size(), 0);
    assertEquals(failedDivisors.size(), 1);
    final Pair<Throwable, Integer> failedDivisor = failedDivisors.iterator().next();
    assertEquals(failedDivisor.getA().getMessage(), KABLOOEY);
    assertEquals((int) failedDivisor.getB(), 0);

    failedDivisors.clear();

    // Bad failure bi-consumer
    final Try<Integer> tf = Try.ofConsumer(KABLOOEY_CONSUMER, 10).ifSuccessfulOrElse(ints::add, KABLOOEY_BICONSUMER);
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertEquals(failure.getA(), 10);
    assertEquals(failure.getB().getMessage(), KABLOOEY);
    assertTrue(ints.isEmpty());
    assertTrue(failedDivisors.isEmpty());
  }

  /**
   * Demonstrates {@link Try#ifSuccessfulOrElseDo(FailableConsumer, FailableRunnable)}
   */
  @Test
  public void test_ifSuccessfulOrElseDo()
  {
    final Set<Integer> ints = new HashSet<>();

    Try.ofSupplier(() -> 2 / 1).ifSuccessfulOrElseDo(ints::add, () -> ints.add(-1));
    assertEquals(ints.size(), 1);
    assertTrue(ints.contains(2));

    ints.clear();
    Try.ofSupplier(() -> 2 / 0).ifSuccessfulOrElseDo(ints::add, () -> ints.add(-1));
    assertEquals(ints.size(), 1);
    assertTrue(ints.contains(-1));

    // Null runnable
    final Try<Integer> tf = Try.ofFunction(i -> i / 0, 4)
        .ifSuccessfulOrElseDo(ints::add, null);
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertEquals(failure.getA(), 4);
    assertTrue(failure.getB() instanceof NullPointerException);
  }

  /**
   * Demonstrates {@link Try#ifSuccessfulGet(FailableSupplier)}
   */
  @Test
  public void test_ifSuccessfulGet()
  {
    // Good runnable
    final String two = Try.ofRunnable(GOOD_RUNNABLE)
        .ifSuccessfulGet(() -> 2)
        .map(String::valueOf)
        .orElseThrow();
    assertEquals(two, "2");

    // Bad runnable
    final Try<String> tf = Try.ofRunnable(KABLOOEY_RUNNABLE)
        .ifSuccessfulGet(() -> 2)
        .map(String::valueOf);
    assertTrue(tf.isFailure());
    assertEquals(tf.getError().orElseThrow().getMessage(), KABLOOEY);

    // Good runnable, bad supplier
    final Try<String> tf2 = Try.ofRunnable(GOOD_RUNNABLE)
        .ifSuccessfulGet(KABLOOEY_SUPPLIER)
        .map(String::valueOf);
    assertTrue(tf2.isFailure());
    assertEquals(tf2.getError().orElseThrow().getMessage(), KABLOOEY);

    // Silly usage, as the originally supplied value is thrown away; for demonstrative purposes
    final String three = Try.ofSupplier(() -> 2)
        .ifSuccessfulGet(() -> 3)
        .map(String::valueOf)
        .orElseThrow();
    assertEquals(three, "3");
  }

  /**
   * Demonstrates {@link Try#ifFailure(FailableConsumer)}
   */
  @Test
  public void test_ifFailure_Consumer()
  {
    final Set<Throwable> errors = new HashSet<>();

    Try.ofSupplier(() -> 2 / 1).ifFailure(errors::add);
    assertTrue(errors.isEmpty());

    Try.ofSupplier(() -> 2 / 0).ifFailure(errors::add);
    assertFalse(errors.isEmpty());
    assertTrue(errors.iterator().next() instanceof ArithmeticException);

    // Null consumer
    final Try<Integer> tf = Try.ofFunction(i -> i / 0, 4)
        .ifFailure((FailableConsumer<? super Throwable, ? extends Throwable>) null);
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertEquals(failure.getA(), 4);
    assertTrue(failure.getB() instanceof NullPointerException);
  }

  /**
   * Demonstrates {@link Try#ifFailure(FailableBiConsumer)}
   */
  @Test
  public void test_ifFailure_BiConsumer()
  {
    final Set<Throwable> errors = new HashSet<>();
    final Set<Integer> errorDivisors = new HashSet<>();
    final FailableBiConsumer<Throwable, Integer, Exception> biConsumer = (e, i) -> {
      errors.add(e);
      errorDivisors.add(i);
    };

    Try.ofFunction(i -> 2 / i, 1).ifFailure(biConsumer);
    assertTrue(errors.isEmpty());
    assertTrue(errorDivisors.isEmpty());

    Try.ofFunction(i -> 2 / i, 0).ifFailure(biConsumer);
    assertFalse(errors.isEmpty());
    assertTrue(errors.iterator().next() instanceof ArithmeticException);
    assertFalse(errorDivisors.isEmpty());
    assertEquals((int) errorDivisors.iterator().next(), 0);

    // Null consumer for FailedFunction
    final Try<Integer> tf = Try.ofFunction(i -> i / 0, 4)
        .ifFailure((FailableBiConsumer<? super Throwable, ? super Integer, ? extends Throwable>) null);
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertEquals(failure.getA(), 4);
    assertTrue(failure.getB() instanceof NullPointerException);

    // Null consumer for FailedConsumer
    final Try<Integer> tf2 = Try.ofConsumer(KABLOOEY_CONSUMER, 4)
        .ifFailure((FailableBiConsumer<? super Throwable, ? super Integer, ? extends Throwable>) null);
    assertTrue(tf2.isFailure());
    final Pair<Object, Throwable> failure2 = tf2.getFailure().orElseThrow();
    assertEquals(failure2.getA(), 4);
    assertTrue(failure2.getB() instanceof NullPointerException);
  }

  /**
   * Demonstrates {@link Try#getSuccess()}
   */
  @Test
  public void test_getSuccess()
  {
    Optional<Integer> optional = Try.ofSupplier(() -> 1 / 1)
        .getSuccess();
    assertTrue(optional.isPresent());
    assertEquals((int) optional.get(), 1);

    optional = Try.ofSupplier(() -> 1 / 0)
        .getSuccess();
    assertTrue(optional.isEmpty());
  }

  /**
   * Demonstrates {@link Try#getError()}
   */
  @Test
  public void test_getError()
  {
    assertTrue(Try.ofSupplier(() -> 1 / 1)
        .getError().isEmpty());

    final Optional<Throwable> optional = Try.ofFunction(i -> i / 0, 1)
        .getError();
    assertTrue(optional.isPresent());
    assertTrue(optional.get() instanceof ArithmeticException);
  }

  /**
   * Demonstrates {@link Try#getFailure()}
   */
  @Test
  public void test_getFailure()
  {
    assertTrue(Try.ofFunction(i -> i / 3, 3)
        .getFailure().isEmpty());

    final Try<Integer> tf = Try.ofFunction(i -> i / 0, 3);
    assertTrue(tf.getFailure().isPresent());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertEquals(failure.getA(), 3);
    assertTrue(failure.getB() instanceof ArithmeticException);
  }

  /**
   * Demonstrates {@link Try#filter(FailablePredicate)}
   */
  @Test
  public void test_filter()
  {
    // Good function, good predicate, value passes predicate
    final Try<Integer> ts = Try.ofFunction(s -> Integer.parseInt(s) / 3, String.valueOf(9))
        .filter(val -> val % 3 == 0);
    assertTrue(ts.isSuccessful());
    assertEquals((int) ts.orElseThrow(), 3);
    assertEquals(ts.getStates(), List.of("9", 3));

    // Good function, good predicate, value fails predicate
    final Try<Integer> tf1 = Try.ofFunction(s -> Integer.parseInt(s) / 3, String.valueOf(9))
        .filter(val -> val % 2 == 0);
    assertTrue(tf1.isFailure());
    final Pair<Object, Throwable> failure = tf1.getFailure().orElseThrow();
    assertEquals(failure.getA(), 3);
    assertEquals(failure.getB().getMessage(), "3 failed predicate");

    // Good function, bad predicate
    final Try<Integer> tf2 = Try.ofFunction(s -> Integer.parseInt(s) / 3, String.valueOf(9))
        .filter(KABLOOEY_PREDICATE);
    assertTrue(tf2.isFailure());
    final Pair<Object, Throwable> failure2 = tf2.getFailure().orElseThrow();
    assertEquals(failure2.getA(), 3);
    assertEquals(failure2.getB().getMessage(), KABLOOEY);

    // Bad function
    final Try<Integer> tf3 = Try.ofFunction(i -> Integer.parseInt(i) / 0, String.valueOf(3))
        .filter(val -> val % 2 == 0);
    assertTrue(tf3.isFailure());
    final Pair<Object, Throwable> failure3 = tf3.getFailure().orElseThrow();
    assertEquals(failure3.getA(), "3");
    assertTrue(failure3.getB() instanceof ArithmeticException);

    // Null predicate
    final Try<Integer> tf4 = Try.ofFunction(i -> i / 3, 9)
        .filter(null);
    assertTrue(tf4.isFailure());
    final Pair<Object, Throwable> failure4 = tf4.getFailure().orElseThrow();
    assertEquals(failure4.getA(), 3);
    assertTrue(failure4.getB() instanceof NullPointerException);
  }

  @Test
  public void test_filter_withFailureSupplier()
  {
    // Good function, good predicate, value passes predicate
    final Try<Integer> ts = Try.ofFunction(s -> Integer.parseInt(s) / 3, String.valueOf(9))
        .filter(val -> val % 3 == 0, SUPPLY_KABLOOEY);
    assertTrue(ts.isSuccessful());
    assertEquals((int) ts.orElseThrow(), 3);
    assertEquals(ts.getStates(), List.of("9", 3));

    // Good function, good predicate, value fails predicate
    final Try<Integer> tf1 = Try.ofFunction(s -> Integer.parseInt(s) / 3, String.valueOf(9))
        .filter(val -> val % 2 == 0, SUPPLY_KABLOOEY);
    assertTrue(tf1.isFailure());
    final Pair<Object, Throwable> failure = tf1.getFailure().orElseThrow();
    assertEquals(failure.getA(), 3);
    assertEquals(failure.getB().getMessage(), KABLOOEY);

    // Null supplier
    final Try<Integer> tf2 = Try.ofFunction(s -> Integer.parseInt(s) / 3, String.valueOf(9))
        .filter(val -> val % 2 == 0, SUPPLY_KABLOOEY);
    assertTrue(tf2.isFailure());
    final Pair<Object, Throwable> failure2 = tf1.getFailure().orElseThrow();
    assertEquals(failure2.getA(), 3);
    assertEquals(failure2.getB().getMessage(), KABLOOEY);

    // Bad function
    final Try<Integer> tf3 = Try.ofFunction(i -> Integer.parseInt(i) / 0, String.valueOf(3))
        .filter(val -> val % 2 == 0, SUPPLY_KABLOOEY);
    assertTrue(tf3.isFailure());
    final Pair<Object, Throwable> failure3 = tf3.getFailure().orElseThrow();
    assertEquals(failure3.getA(), "3");
    assertTrue(failure3.getB() instanceof ArithmeticException);
  }

  /**
   * Demonstrates {@link Try#map(FailableFunction)}
   */
  @Test
  public void test_map()
  {
    // Good function, good mapper
    final Try<Integer> ts = Try.ofFunction(i -> i / 3, 9)
        .map(i -> i % 3);
    assertTrue(ts.isSuccessful());
    assertEquals(ts.orElseThrow(), 0);
    assertEquals(ts.getStates(), List.of(9, 3, 0));

    // Good function, bad mapper
    final Try<Integer> tf = Try.ofFunction(i -> i / 3, 9)
        .map(KABLOOEY_FUNCTION);
    assertTrue(tf.getFailure().isPresent());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertNull(failure.getA());
    assertEquals(failure.getB().getMessage(), KABLOOEY);
    assertEquals(tf.getStates(), List.of(9, 3, new Pair<>(null, KABLOOEY_EXCEPTION)));

    // Bad function, good mapper (doesn't matter)
    final Try<Integer> tf2 = Try.ofFunction(KABLOOEY_FUNCTION, 4)
        .map(i -> i % 2);
    assertTrue(tf2.isFailure());
    final Pair<Object, Throwable> failure2 = tf2.getFailure().orElseThrow();
    assertEquals(failure2.getA(), 4);
    assertEquals(failure2.getB(), KABLOOEY_EXCEPTION);
    assertEquals(tf2.getStates(), List.of(new Pair<>(4, KABLOOEY_EXCEPTION)));

    // Null mapper
    final Try<Integer> tf3 = Try.ofFunction(i -> i / 2, 4)
        .map(null);
    assertTrue(tf3.isFailure());
    final Pair<Object, Throwable> failure3 = tf3.getFailure().orElseThrow();
    assertNull(failure3.getA());
    assertTrue(failure3.getB() instanceof NullPointerException);
  }

  /**
   * Demonstrates {@link Try#flatMap(FailableFunction)}
   */
  @Test
  public void test_flatMap()
  {
    final Try<Integer> t = Try.ofFunction(i -> i / 4, 8);

    // Good flatMap
    final int result = t
        .flatMap(GOOD_FLATMAP)
        .orElseThrow();
    assertEquals(result, 1);
    assertEquals(t.getStates(), List.of(8, 2));

    // Bad flatMap
    final Try<Integer> tf = t
        .flatMap(BAD_FLATMAP);
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertEquals(failure.getA(), 2);
    assertTrue(failure.getB() instanceof ArithmeticException);

    // Null mapper
    final Try<Integer> tf2 = t
        .flatMap(null);
    assertTrue(tf2.isFailure());
    final Pair<Object, Throwable> failure2 = tf2.getFailure().orElseThrow();
    assertEquals(failure2.getA(), 2);
    assertTrue(failure2.getB() instanceof NullPointerException);

    // Bad function
    final Try<Integer> tf3 = Try.ofFunction(i -> i / 0, 4)
        .flatMap(GOOD_FLATMAP);
    assertTrue(tf3.isFailure());
    final Pair<Object, Throwable> failure3 = tf3.getFailure().orElseThrow();
    assertEquals(failure3.getA(), 4);
    assertTrue(failure3.getB() instanceof ArithmeticException);
  }

  /**
   * Demonstrates {@link Try#or(FailableSupplier)}
   */
  @Test
  public void test_or()
  {
    // Good function, good alternative Try (doesn't matter)
    final Try<Integer> ts = Try.ofFunction(i -> i / 2, 4)
        .or(() -> Try.ofFunction(i -> i / 4, 4));
    assertTrue(ts.isSuccessful());
    assertEquals((int) ts.orElseThrow(), 2);
    assertEquals(ts.getStates(), List.of(4, 2));

    // Good function, bad alternative Try (doesn't matter)
    final Try<Integer> ts2 = Try.ofFunction(i -> i / 2, 4)
        .or(() -> Try.ofFunction(KABLOOEY_FUNCTION, 4));
    assertTrue(ts2.isSuccessful());
    assertEquals((int) ts2.orElseThrow(), 2);
    assertEquals(ts2.getStates(), List.of(4, 2));

    // Bad function, good alternative Try
    final Try<Integer> ts3 = Try.ofFunction(KABLOOEY_FUNCTION, 4)
        .or(() -> Try.ofFunction(i -> i / 2, 4));
    assertTrue(ts3.isSuccessful());
    assertEquals((int) ts3.orElseThrow(), 2);
    assertEquals(ts3.getStates(), List.of(new Pair<>(4, KABLOOEY_EXCEPTION), 4, 2));

    // Bad function, bad alternative Try
    final Try<Integer> tf = Try.ofFunction(KABLOOEY_FUNCTION, 1)
        .or(() -> Try.ofFunction(KABLOOEY_FUNCTION, 2));
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertEquals((int) failure.getA(), 2);
    assertEquals(failure.getB(), KABLOOEY_EXCEPTION);
    assertEquals(tf.getStates(), List.of(new Pair<>(1, KABLOOEY_EXCEPTION), new Pair<>(2, KABLOOEY_EXCEPTION)));

    // Bad function, bad alternative supplier
    final Try<Integer> tf2 = Try.ofFunction(KABLOOEY_FUNCTION, 4)
        .or(KABLOOEY_TRYSUPPLIER);
    assertTrue(tf2.isFailure());
    final Pair<Object, Throwable> failure2 = tf2.getFailure().orElseThrow();
    assertEquals((int) failure2.getA(), 4);
    assertEquals(failure2.getB().getMessage(), KABLOOEY);
    assertEquals(tf2.getStates(), List.of(new Pair<>(4, KABLOOEY_EXCEPTION)));
  }

  /**
   * Demonstrates {@link Try#stream()}
   */
  @Test
  public void test_stream()
  {
    final Integer val = Try.ofFunction(i -> i / 2, 4).stream().findFirst().orElseThrow();
    assertEquals((int) val, 2);

    assertTrue(Try.ofFunction(i -> i / 0, 4).stream().findFirst().isEmpty());
  }

  /**
   * Demonstrates {@link Try#orElse(Object)}
   */
  @Test
  public void test_orElse()
  {
    assertEquals((int) Try.ofFunction(i -> i / 4, 8)
        .orElse(5), 2);

    assertEquals((int) Try.ofFunction(i -> i / 0, 2)
        .orElse(1), 1);
  }

  /**
   * Demonstrates {@link Try#orElseGet(FailableSupplier)}
   */
  @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = KABLOOEY)
  public void test_orElseGet()
  {
    assertEquals((int) Try.ofFunction(i -> i / 2, 4)
        .orElseGet(() -> 5), 2);

    assertEquals((int) Try.ofFunction(i -> i / 0, 4)
        .orElseGet(() -> 5), 5);

    Try.ofFunction(i -> i / 0, 4)
        .orElseGet(KABLOOEY_INTSUPPLIER);
  }

  /**
   * Demonstrates {@link Try#orElseThrow()}
   */
  @Test(expectedExceptions = ArithmeticException.class)
  public void test_orElseThrow()
  {
    assertEquals((int) Try.ofFunction(i -> i / 2, 4)
        .orElseThrow(), 2);

    Try.ofFunction(i -> i / 0, 4)
        .orElseThrow();
  }

  /**
   * Demonstrates {@link Try#orElseThrow(FailableSupplier)}
   */
  @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = KABLOOEY)
  public void test_orElseThrow_Supplier()
  {
    assertEquals((int) Try.ofFunction(i -> i / 2, 4)
        .orElseThrow(SUPPLY_KABLOOEY), 2);

    Try.ofFunction(i -> i / 0, 4)
        .orElseThrow(SUPPLY_KABLOOEY);
  }

  @Test
  public void testComplexChaining()
  {
    final Try<Integer> ts = Try.ofSupplier(() -> 1)
        .map(i -> i * 2)
        .filter(i -> i % 2 == 0)
        .flatMap(i -> Try.ofFunction(Object::toString, i))
        .map(s -> s + "2")
        .map(Integer::parseInt)
        .map(KABLOOEY_FUNCTION)
        .or(() -> Try.ofSupplier(() -> 10));

    assertTrue(ts.isSuccessful());
    List<Object> expectedStates = List.of(1, 2, 2, "2", "22", 22, new Pair<>(null, KABLOOEY_EXCEPTION), 10);
    assertEquals(ts.getStates(), expectedStates);
  }
}
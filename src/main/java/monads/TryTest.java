package monads;

import functions.arity.VariadicConsumer;
import functions.arity.VariadicFunction;
import functions.util.tuples.Pair;
import org.testng.annotations.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

@SuppressWarnings({"divzero", "NumericOverflow", "PointlessArithmeticExpression"})
public class TryTest
{

  private static final String KABLOOEY = "kablooey!";
  private static final RuntimeException KABLOOEY_EXCEPTION = new RuntimeException(KABLOOEY);
  private static final Consumer<Integer> KABLOOEY_CONSUMER = i -> {throw KABLOOEY_EXCEPTION;};
  private static final BiConsumer<Throwable, Integer> KABLOOEY_BICONSUMER = (e, i) -> {throw KABLOOEY_EXCEPTION;};
  private static final VariadicConsumer<Integer> KABLOOEY_VARCONSUMER = vals -> {throw KABLOOEY_EXCEPTION;};
  private static final Supplier<Throwable> KABLOOEY_SUPPLIER = () -> KABLOOEY_EXCEPTION;
  private static final Supplier<Integer> KABLOOEY_INTSUPPLIER = () -> {throw KABLOOEY_EXCEPTION;};
  private static final Supplier<Try<Integer>> KABLOOEY_TRYSUPPLIER = () -> {throw KABLOOEY_EXCEPTION;};
  private static final Function<Integer, Integer> KABLOOEY_FUNCTION = i -> {throw KABLOOEY_EXCEPTION;};
  private static final VariadicFunction<Integer, String> KABLOOEY_VARFUNCTION = (Integer... array) -> {throw KABLOOEY_EXCEPTION;};
  private static final Predicate<Integer> KABLOOEY_PREDICATE = i -> {throw KABLOOEY_EXCEPTION;};
  private static final Function<Integer, Try<Integer>> GOOD_FLATMAP = val -> Try.ofFunction(i -> i / 2, val);
  private static final Function<Integer, Try<Integer>> BAD_FLATMAP = val -> Try.ofFunction(i -> i / 0, val);

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
  }

  /**
   * Demonstrates {@link Try#ofSupplier(Supplier)}
   */
  @Test
  public void test_of_Supplier()
  {
    final int val = 1;
    final Try<Integer> ts = Try.ofSupplier(() -> val);
    assertTrue(ts.isSuccessful());
    assertFalse(ts.isFailure());
    assertTrue(ts.getFailure().isEmpty());
    assertEquals((int) ts.orElseThrow(), val);

    final Try<Integer> tf = Try.ofSupplier(() -> val / 0);
    assertFalse(tf.isSuccessful());
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> pair = tf.getFailure().orElseThrow();
    assertNull(pair.getA());
    assertTrue(pair.getB() instanceof ArithmeticException);
  }

  /**
   * Demonstrates {@link Try#ofFunction(Function, Object)}
   */
  @Test
  public void test_of_Function()
  {
    final int val = 1;
    final Try<Integer> ts = Try.ofFunction(i -> i / 1, val);
    assertTrue(ts.isSuccessful());
    assertFalse(ts.isFailure());
    assertTrue(ts.getFailure().isEmpty());
    assertEquals((int) ts.orElseThrow(), val);

    final Try<Integer> tf = Try.ofFunction(i -> Integer.parseInt(i) / 0, String.valueOf(val));
    assertFalse(tf.isSuccessful());
    assertTrue(tf.isFailure());
    final Pair<String, Throwable> pair = tf.<String>getFailure().orElseThrow();
    assertEquals(String.valueOf(val), pair.getA());
    assertTrue(pair.getB() instanceof ArithmeticException);
  }

  /**
   * Demonstrates {@link Try#ofConsumer(Consumer, Object)}
   */
  @Test
  public void test_of_Consumer()
  {
    final Set<Integer> ints = new HashSet<>();
    final Consumer<Integer> goodConsumer = ints::add;

    final Try<Integer> ts = Try.ofConsumer(goodConsumer, 1);
    assertTrue(ts.isSuccessful());
    assertFalse(ts.isFailure());
    assertTrue(ts.getFailure().isEmpty());
    assertTrue(ints.contains(1));
    assertEquals((int) ts.orElseThrow(), 1);

    final Try<Integer> tf = Try.ofConsumer(KABLOOEY_CONSUMER, 1);
    assertFalse(tf.isSuccessful());
    assertTrue(tf.isFailure());
    final Pair<Integer, Throwable> pair = tf.<Integer>getFailure().orElseThrow();
    assertEquals((int) pair.getA(), 1);
    assertEquals(pair.getB().getMessage(), KABLOOEY);
  }

  /**
   * Demonstrates {@link Try#ofVarConsumer(VariadicConsumer, Object[])}
   */
  @Test
  public void test_of_VariadicConsumer()
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

    final Try<Integer[]> tf = Try.ofVarConsumer(KABLOOEY_VARCONSUMER, 1, 2, 3);
    assertFalse(tf.isSuccessful());
    assertTrue(tf.isFailure());
    final Pair<Integer[], Throwable> pair = tf.<Integer[]>getFailure().orElseThrow();
    final List<Integer> tfVals = Arrays.asList(pair.getA());
    assertEquals(tsVals, tfVals);
    assertEquals(pair.getB().getMessage(), KABLOOEY);
  }

  /**
   * Demonstrates {@link Try#ofVarFunction(VariadicFunction, Object[])}
   */
  @Test
  public void test_of_VariadicFunction()
  {
    final VariadicFunction<Integer, String> goodFunction = (Integer... array) -> Arrays.stream(array)
        .map(String::valueOf)
        .collect(Collectors.joining(","));

    final Try<String> ts = Try.ofVarFunction(goodFunction, 1, 2, 3);
    assertTrue(ts.isSuccessful());
    assertFalse(ts.isFailure());
    assertTrue(ts.getFailure().isEmpty());
    assertEquals(ts.orElseThrow(), "1,2,3");

    final Try<String> tf = Try.ofVarFunction(KABLOOEY_VARFUNCTION, 1, 2, 3);
    assertFalse(tf.isSuccessful());
    assertTrue(tf.isFailure());
    final Pair<Integer[], Throwable> pair = tf.<Integer[]>getFailure().orElseThrow();
    final List<Integer> tfVals = Arrays.asList(pair.getA());
    assertEquals(List.of(1, 2, 3), tfVals);
    assertEquals(pair.getB().getMessage(), KABLOOEY);
  }

  /**
   * Demonstrates {@link Try#get()}
   */
  @Test(expectedExceptions = ArithmeticException.class)
  public void test_get()
  {
    final Try<Integer> ts = Try.ofSupplier(() -> 1 / 1);
    assertEquals((int) ts.get(), 1);

    final Try<Integer> tf = Try.ofSupplier(() -> 1 / 0);
    tf.get();
  }

  /**
   * Demonstrates {@link Try#ifSuccessful(Consumer)}
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
   * Demonstrates {@link Try#ifSuccessfulOrElse(Consumer, Consumer)}
   */
  @Test
  public void test_ifSuccessfulOrElse_Consumer()
  {
    final Consumer<Integer> goodConsumer = i -> {};

    final Set<Integer> ints = new HashSet<>();
    final Set<Integer> failedDivisors = new HashSet<>();

    // Good consumer
    Try.ofConsumer(goodConsumer, 2).ifSuccessfulOrElse(ints::add, failedDivisors::add);
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
    final Pair<Integer, Throwable> failure = tf.<Integer>getFailure().orElseThrow();
    assertEquals(failure.getA(), 10);
    assertEquals(failure.getB().getMessage(), KABLOOEY);
    assertTrue(ints.isEmpty());
    assertTrue(failedDivisors.isEmpty());
  }

  /**
   * Demonstrates {@link Try#ifSuccessfulOrElse(Consumer, BiConsumer)}
   */
  @Test
  public void test_ifSuccessfulOrElse_BiConsumer()
  {
    final Consumer<Integer> goodConsumer = i -> {};

    final Set<Integer> ints = new HashSet<>();
    final Set<Pair<Throwable, Integer>> failedDivisors = new HashSet<>();

    // Good consumer
    Try.ofConsumer(goodConsumer, 2).ifSuccessfulOrElse(ints::add, (e, i) -> failedDivisors.add(new Pair<>(e, i)));
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
    final Pair<Integer, Throwable> failure = tf.<Integer>getFailure().orElseThrow();
    assertEquals(failure.getA(), 10);
    assertEquals(failure.getB().getMessage(), KABLOOEY);
    assertTrue(ints.isEmpty());
    assertTrue(failedDivisors.isEmpty());
  }

  /**
   * Demonstrates {@link Try#ifSuccessfulOrElseDo(Consumer, Runnable)}
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
   * Demonstrates {@link Try#ifFailure(Consumer)}
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
        .ifFailure((Consumer<? super Throwable>) null);
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertEquals(failure.getA(), 4);
    assertTrue(failure.getB() instanceof NullPointerException);
  }

  /**
   * Demonstrates {@link Try#ifFailure(BiConsumer)}
   */
  @Test
  public void test_ifFailure_BiConsumer()
  {
    final Set<Throwable> errors = new HashSet<>();
    final Set<Integer> errorDivisors = new HashSet<>();

    Try.ofFunction(i -> 2 / i, 1).ifFailure((e, i) -> {
      errors.add(e);
      errorDivisors.add(i);
    });
    assertTrue(errors.isEmpty());
    assertTrue(errorDivisors.isEmpty());

    Try.ofFunction(i -> 2 / i, 0).ifFailure((e, i) -> {
      errors.add(e);
      errorDivisors.add(i);
    });
    assertFalse(errors.isEmpty());
    assertTrue(errors.iterator().next() instanceof ArithmeticException);
    assertFalse(errorDivisors.isEmpty());
    assertEquals((int) errorDivisors.iterator().next(), 0);

    // Null consumer for FailedFunction
    final Try<Integer> tf = Try.ofFunction(i -> i / 0, 4)
        .ifFailure((BiConsumer<? super Throwable, ? super Integer>) null);
    assertTrue(tf.isFailure());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertEquals(failure.getA(), 4);
    assertTrue(failure.getB() instanceof NullPointerException);

    // Null consumer for FailedConsumer
    final Try<Integer> tf2 = Try.ofConsumer(KABLOOEY_CONSUMER, 4)
        .ifFailure((BiConsumer<? super Throwable, ? super Integer>) null);
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

    // Demonstrates without explicit generics
    final Try<Integer> tf = Try.ofFunction(i -> i / 0, 3);
    assertTrue(tf.getFailure().isPresent());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertEquals(failure.getA(), 3);
    assertTrue(failure.getB() instanceof ArithmeticException);

    // Demonstrates explicit generics
    assertEquals((int) tf.<Integer>getFailure().orElseThrow().getA(), 3);
  }

  /**
   * Demonstrates {@link Try#filter(Predicate)}
   */
  @Test
  public void test_filter()
  {
    // Good function, good predicate, value passes predicate
    final Try<Integer> ts = Try.ofFunction(s -> Integer.parseInt(s) / 3, String.valueOf(9))
        .filter(val -> val % 3 == 0);
    assertTrue(ts.isSuccessful());
    assertEquals((int) ts.orElseThrow(), 3);

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
  public void test_filter_Supplier()
  {
    // Good function, good predicate, value passes predicate
    final Try<Integer> ts = Try.ofFunction(s -> Integer.parseInt(s) / 3, String.valueOf(9))
        .filter(val -> val % 3 == 0, KABLOOEY_SUPPLIER);
    assertTrue(ts.isSuccessful());
    assertEquals((int) ts.orElseThrow(), 3);

    // Good function, good predicate, value fails predicate
    final Try<Integer> tf1 = Try.ofFunction(s -> Integer.parseInt(s) / 3, String.valueOf(9))
        .filter(val -> val % 2 == 0, KABLOOEY_SUPPLIER);
    assertTrue(tf1.isFailure());
    final Pair<Object, Throwable> failure = tf1.getFailure().orElseThrow();
    assertEquals(failure.getA(), 3);
    assertEquals(failure.getB().getMessage(), KABLOOEY);

    // Null supplier
    final Try<Integer> tf2 = Try.ofFunction(s -> Integer.parseInt(s) / 3, String.valueOf(9))
        .filter(val -> val % 2 == 0, KABLOOEY_SUPPLIER);
    assertTrue(tf2.isFailure());
    final Pair<Object, Throwable> failure2 = tf1.getFailure().orElseThrow();
    assertEquals(failure2.getA(), 3);
    assertEquals(failure2.getB().getMessage(), KABLOOEY);

    // Bad function
    final Try<Integer> tf3 = Try.ofFunction(i -> Integer.parseInt(i) / 0, String.valueOf(3))
        .filter(val -> val % 2 == 0, KABLOOEY_SUPPLIER);
    assertTrue(tf3.isFailure());
    final Pair<Object, Throwable> failure3 = tf3.getFailure().orElseThrow();
    assertEquals(failure3.getA(), "3");
    assertTrue(failure3.getB() instanceof ArithmeticException);
  }

  /**
   * Demonstrates {@link Try#map(Function)}
   */
  @Test
  public void test_map()
  {
    // Good function, good mapper
    final int result = Try.ofFunction(i -> i / 3, 9)
        .map(i -> i % 3)
        .orElseThrow();
    assertEquals(result, 0);

    // Good function, bad mapper
    final Try<Integer> tf = Try.ofFunction(i -> i / 3, 9)
        .map(KABLOOEY_FUNCTION);
    assertTrue(tf.getFailure().isPresent());
    final Pair<Object, Throwable> failure = tf.getFailure().orElseThrow();
    assertEquals(failure.getA(), 3);
    assertEquals(failure.getB().getMessage(), KABLOOEY);

    // Bad function, good mapper (doesn't matter)
    final Try<Integer> tf2 = Try.ofFunction(i -> i / 0, 4)
        .map(i -> i % 2);
    assertTrue(tf2.isFailure());
    final Pair<Object, Throwable> failure2 = tf2.getFailure().orElseThrow();
    assertEquals(failure2.getA(), 4);
    assertTrue(failure2.getB() instanceof ArithmeticException);

    // Null mapper
    final Try<Integer> tf3 = Try.ofFunction(i -> i / 2, 4)
        .map(null);
    assertTrue(tf3.isFailure());
    final Pair<Object, Throwable> failure3 = tf3.getFailure().orElseThrow();
    assertEquals(failure3.getA(), 2);
    assertTrue(failure3.getB() instanceof NullPointerException);
  }

  /**
   * Demonstrates {@link Try#flatMap(Function)}
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
   * Demonstrates {@link Try#or(Supplier)}
   */
  @Test
  public void test_or()
  {
    // Good function, good alternative Try (doesn't matter)
    final Try<Integer> ts = Try.ofFunction(i -> i / 2, 4)
        .or(() -> Try.ofFunction(i -> i / 4, 4));
    assertTrue(ts.isSuccessful());
    assertEquals((int) ts.orElseThrow(), 2);

    // Good function, bad alternative Try (doesn't matter
    final Try<Integer> ts2 = Try.ofFunction(i -> i / 2, 4)
        .or(() -> Try.ofFunction(i -> i / 0, 4));
    assertTrue(ts2.isSuccessful());
    assertEquals((int) ts2.orElseThrow(), 2);

    // Bad function, good alternative Try
    final Try<Integer> ts3 = Try.ofFunction(i -> i / 0, 4)
        .or(() -> Try.ofFunction(i -> i / 2, 4));
    assertTrue(ts3.isSuccessful());
    assertEquals((int) ts3.orElseThrow(), 2);

    // Bad function, bad alternative Try
    final Try<Integer> tf = Try.ofFunction(i -> i / 0, 1)
        .or(() -> Try.ofFunction(i -> i / 0, 2));
    assertTrue(tf.isFailure());
    final Pair<Integer, Throwable> failure = tf.<Integer>getFailure().orElseThrow();
    assertEquals((int) failure.getA(), 2);
    assertTrue(failure.getB() instanceof ArithmeticException);

    // Bad function, bad alternative supplier
    final Try<Integer> tf2 = Try.ofFunction(i -> i / 0, 4)
        .or(KABLOOEY_TRYSUPPLIER);
    assertTrue(tf.isFailure());
    assertTrue(tf2.isFailure());
    final Pair<Integer, Throwable> failure2 = tf2.<Integer>getFailure().orElseThrow();
    assertEquals((int) failure2.getA(), 4);
    assertEquals(failure2.getB().getMessage(), KABLOOEY);
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
   * Demonstrates {@link Try#orElseGet(Supplier)}
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
   * Demonstrates {@link Try#orElseThrow(Supplier)}
   */
  @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = KABLOOEY)
  public void test_orElseThrow_Supplier()
  {
    assertEquals((int) Try.ofFunction(i -> i / 2, 4)
        .orElseThrow(KABLOOEY_SUPPLIER), 2);

    Try.ofFunction(i -> i / 0, 4)
        .orElseThrow(KABLOOEY_SUPPLIER);
  }

  @Test
  public void testComplexChaining()
  {
    final List<Object> steps = new ArrayList<>();

    final Try<Integer> ts = Try.ofSupplier(() -> 1)
        .ifSuccessful(steps::add)
        .map(i -> i * 2)
        .ifSuccessful(steps::add)
        .filter(i -> i % 2 == 0)
        .flatMap(i -> Try.ofFunction(Object::toString, i))
        .ifSuccessful(steps::add)
        .map(s -> s + "2")
        .ifSuccessful(steps::add)
        .map(Integer::parseInt)
        .ifSuccessful(steps::add)
        .map(KABLOOEY_FUNCTION)
        .ifSuccessful(steps::add)
        .ifFailure((e, i) -> steps.add(new Pair<>(e, i)))
        .or(() -> Try.ofSupplier(() -> 10))
        .ifSuccessful(steps::add);

    assertTrue(ts.isSuccessful());
    assertEquals(steps.size(), 7);
    List<Object> expectedStates = List.of(1, 2, "2", "22", 22, new Pair<>(KABLOOEY_EXCEPTION, 22), 10);
    assertEquals(steps, expectedStates);
    assertEquals(ts.getStates(), expectedStates);
  }
}
package monads;

import functions.arity.VariadicConsumer;
import functions.arity.VariadicFunction;
import functions.util.Pair;
import functions.util.Pojo;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings({"divzero", "NumericOverflow", "PointlessArithmeticExpression"})
public class TryDemo
{
  public static void main(String[] args)
  {
    demoOf();
    demoOf_Supplier();
    demoOf_Function();
    demoOf_Consumer();
    demoOf_VariadicConsumer();
    demoOf_VariadicFunction();
    demoGet();
    demoIfSuccessful();
    demoIfSuccessfulOrElse_consumer();
    demoIfSuccessfulOrElse_biConsumer();
    demoIfSuccessfulOrElseDo();
    demoIfFailure_consumer();
    demoIfFailure_biConsumer();
    demoGetSuccess();
    demoGetFailure();
    demoGetFailurePair();
  }

  /**
   * Demonstrates {@link Try#of(Object)}
   */
  private static void demoOf()
  {
    final Try<Integer> t = Try.of(1);
    assert t.isSuccessful();
    assert !t.isFailure();
  }

  /**
   * Demonstrates {@link Try#of(Supplier)}
   */
  private static void demoOf_Supplier()
  {
    final Try<Integer> ts = Try.of(() -> 1);
    assert ts.isSuccessful();
    assert !ts.isFailure();

    final Try<Integer> tf = Try.of(() -> 1 / 0);
    assert !tf.isSuccessful();
    assert tf.isFailure();
  }

  /**
   * Demonstrates {@link Try#of(Function, Object)}
   */
  private static void demoOf_Function()
  {
    final Try<Integer> ts = Try.of(i -> i / 1, 1);
    assert ts.isSuccessful();
    assert !ts.isFailure();

    final Try<Double> tf = Try.of(Pojo::getBoobyTrap, new Pojo());
    assert !tf.isSuccessful();
    assert tf.isFailure();
  }

  /**
   * Demonstrates {@link Try#of(Consumer, Object)}
   */
  private static void demoOf_Consumer()
  {
    final Consumer<Integer> goodConsumer = i -> {};
    final Consumer<Integer> badConsumer = i -> {throw new RuntimeException("kablooey!");};

    final Try<Integer> ts = Try.of(goodConsumer, 1);
    assert ts.isSuccessful();
    assert !ts.isFailure();

    final Try<Integer> tf = Try.of(badConsumer, 1);
    assert !tf.isSuccessful();
    assert tf.isFailure();
  }

  /**
   * Demonstrates {@link Try#of(VariadicConsumer, Function, Object[])}
   */
  private static void demoOf_VariadicConsumer()
  {
    final VariadicConsumer<Integer> goodConsumer = vals -> {};
    final VariadicConsumer<Integer> badConsumer = vals -> {throw new RuntimeException("kablooey!");};

    final Try<List<Integer>> ts = Try.of(goodConsumer, List::of, 1, 2, 3);
    assert ts.isSuccessful();
    assert !ts.isFailure();

    final Try<Set<Integer>> tf = Try.of(badConsumer, Set::of, 1, 2, 3);
    assert !tf.isSuccessful();
    assert tf.isFailure();
  }

  /**
   * Demonstrates {@link Try#of(VariadicFunction, Object[])}
   */
  private static void demoOf_VariadicFunction()
  {
    final VariadicFunction<Integer, String> goodFunction = (Integer... array) -> Arrays.stream(array)
        .map(String::valueOf)
        .collect(Collectors.joining(","));
    final VariadicFunction<Integer, String> badFunction = (Integer... array) -> {throw new RuntimeException("kablooey!");};

    final Try<String> ts = Try.of(goodFunction, 1, 2, 3);
    assert ts.isSuccessful();
    assert !ts.isFailure();
    assert "1,2,3".equals(ts.orElseThrow());

    final Try<String> tf = Try.of(badFunction, 1, 2, 3);
    assert !tf.isSuccessful();
    assert tf.isFailure();
  }

  /**
   * Demonstrates {@link Try#get()}
   */
  private static void demoGet()
  {
    try
    {
      final Try<Integer> ts = Try.of(() -> 1 / 1);
      assert ts.get() == 1;
    }
    catch (Throwable e)
    {
      throw new RuntimeException(e);
    }

    try
    {
      final Try<Integer> tf = Try.of(() -> 1 / 0);
      tf.get();
    }
    catch (Throwable e)
    {
      assert e instanceof RuntimeException;
      assert e.getCause() instanceof ArithmeticException;
    }
  }

  /**
   * Demonstrates {@link Try#ifSuccessful(Consumer)}
   */
  private static void demoIfSuccessful()
  {
    final Set<Integer> ints = new HashSet<>();

    Try.of(() -> 1 / 1).ifSuccessful(ints::add);
    assert ints.size() == 1;
    assert ints.contains(1);

    ints.clear();
    Try.of(() -> 1 / 0).ifSuccessful(ints::add);
    assert ints.isEmpty();
  }

  /**
   * Demonstrates {@link Try#ifSuccessfulOrElse(Consumer, Consumer)}
   */
  private static void demoIfSuccessfulOrElse_consumer()
  {
    final Consumer<Integer> goodConsumer = i -> {};
    final Consumer<Integer> badConsumer = i -> {throw new RuntimeException();};

    final Set<Integer> ints = new HashSet<>();
    final Set<Integer> failedDivisors = new HashSet<>();

    Try.of(goodConsumer, 2).ifSuccessfulOrElse(ints::add, failedDivisors::add);
    assert ints.size() == 1;
    assert ints.contains(2);

    ints.clear();
    Try.of(badConsumer, 0).ifSuccessfulOrElse(ints::add, failedDivisors::add);
    assert ints.size() == 0;
    assert failedDivisors.size() == 1;
    assert failedDivisors.iterator().next() == 0;
  }

  /**
   * Demonstrates {@link Try#ifSuccessfulOrElse(Consumer, BiConsumer)}
   */
  private static void demoIfSuccessfulOrElse_biConsumer()
  {
    final Consumer<Integer> goodConsumer = i -> {};
    final Consumer<Integer> badConsumer = i -> {throw new RuntimeException("kablooey!");};

    final Set<Integer> ints = new HashSet<>();
    final Set<Pair<Throwable, Integer>> failedDivisors = new HashSet<>();

    Try.of(goodConsumer, 2).ifSuccessfulOrElse(ints::add, (e, i) -> failedDivisors.add(Pair.of(e, i)));
    assert ints.size() == 1;
    assert ints.contains(2);

    ints.clear();
    Try.of(badConsumer, 0).ifSuccessfulOrElse(ints::add, (e, i) -> failedDivisors.add(Pair.of(e, i)));
    assert ints.size() == 0;
    assert failedDivisors.size() == 1;
    final Pair<Throwable, Integer> failedDivisor = failedDivisors.iterator().next();
    assert "kablooey!".equals(failedDivisor.getA().getMessage());
    assert failedDivisor.getB() == 0;
  }

  /**
   * Demonstrates {@link Try#ifSuccessfulOrElseDo(Consumer, Runnable)}
   */
  private static void demoIfSuccessfulOrElseDo()
  {
    final Set<Integer> ints = new HashSet<>();

    Try.of(() -> 2 / 1).ifSuccessfulOrElseDo(ints::add, () -> ints.add(-1));
    assert ints.size() == 1;
    assert ints.contains(2);

    ints.clear();
    Try.of(() -> 2 / 0).ifSuccessfulOrElseDo(ints::add, () -> ints.add(-1));
    assert ints.size() == 1;
    assert ints.contains(-1);
  }

  /**
   * Demonstrates {@link Try#ifFailure(Consumer)}
   */
  private static void demoIfFailure_consumer()
  {
    final Set<Throwable> errors = new HashSet<>();

    Try.of(() -> 2 / 1).ifFailure(errors::add);
    assert errors.isEmpty();

    Try.of(() -> 2 / 0).ifFailure(errors::add);
    assert !errors.isEmpty();
    assert errors.iterator().next() instanceof ArithmeticException;
  }

  /**
   * Demonstrates {@link Try#ifFailure(BiConsumer)}
   */
  private static void demoIfFailure_biConsumer()
  {
    final Set<Throwable> errors = new HashSet<>();
    final Set<Integer> errorDivisors = new HashSet<>();

    Try.of(i -> 2 / i, 1).ifFailure((e, i) -> {
      errors.add(e);
      errorDivisors.add(i);
    });
    assert errors.isEmpty();
    assert errorDivisors.isEmpty();

    Try.of(i -> 2 / i, 0).ifFailure((e, i) -> {
      errors.add(e);
      errorDivisors.add(i);
    });
    assert !errors.isEmpty();
    assert errors.iterator().next() instanceof ArithmeticException;
    assert !errorDivisors.isEmpty();
    assert errorDivisors.iterator().next() == 0;
  }

  /**
   * Demonstrates {@link Try#getSuccess()}
   */
  private static void demoGetSuccess()
  {
    Optional<Integer> optional = Try.of(() -> 1 / 1)
        .getSuccess();
    assert optional.isPresent();
    assert optional.get() == 1;

    optional = Try.of(() -> 1 / 0)
        .getSuccess();
    assert optional.isEmpty();
  }

  /**
   * Demonstrates {@link Try#getFailure()}
   */
  private static void demoGetFailure()
  {
    assert Try.of(() -> 1 / 1)
        .getFailure().isEmpty();

    final Optional<Throwable> optional = Try.of(i -> i / 0, 1)
        .getFailure();
    assert optional.isPresent();
    assert optional.get() instanceof ArithmeticException;
  }

  /**
   * Demonstrates {@link Try#getFailurePair()}
   */
  private static void demoGetFailurePair()
  {
    assert Try.of(() -> 1 / 1)
        .getFailurePair().isEmpty();

    Try<Integer> tf = Try.of(() -> 1 / 0);
    Optional<Pair<Integer, Throwable>> optional = tf.getFailurePair();
    assert optional.isPresent();
    Pair<Integer, Throwable> pair = optional.get();
    assert pair.getA() == null;
    assert pair.getB() instanceof ArithmeticException;

    tf = Try.of(i -> i / 0, 1);
    optional = tf.getFailurePair();
    assert optional.isPresent();
    pair = optional.get();
    assert pair.getA() == 1;
    assert pair.getB() instanceof ArithmeticException;
  }
}

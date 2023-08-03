package monads;

import functions.util.Pair;
import functions.util.Pojo;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"divzero", "NumericOverflow", "PointlessArithmeticExpression"})
public class TryDemo
{
  public static void main(String[] args)
  {
    demoOf();
    demoOfSupplier();
    demoOfFunction();
    demoGet();
    demoIfSuccessful();
    demoIfSuccessfulOrElse();
    demoIfFailure();
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
  private static void demoOfSupplier()
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
  private static void demoOfFunction()
  {
    final Try<Integer> ts = Try.of(i -> i / 1, 1);
    assert ts.isSuccessful();
    assert !ts.isFailure();

    final Try<Double> tf = Try.of(Pojo::getBoobyTrap, new Pojo());
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
      assert e instanceof ArithmeticException;
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
   * Demonstrates {@link Try#ifSuccessfulOrElse(Consumer, Runnable)}
   */
  private static void demoIfSuccessfulOrElse()
  {
    final Set<Integer> ints = new HashSet<>();

    Try.of(() -> 2 / 1).ifSuccessfulOrElse(ints::add, () -> ints.add(-1));
    assert ints.size() == 1;
    assert ints.contains(2);

    ints.clear();
    Try.of(() -> 2 / 0).ifSuccessfulOrElse(ints::add, () -> ints.add(-1));
    assert ints.size() == 1;
    assert ints.contains(-1);
  }

  /**
   * Demonstrates {@link Try#ifFailure(Consumer)}
   */
  private static void demoIfFailure()
  {
    final Set<Throwable> errors = new HashSet<>();

    Try.of(() -> 2 / 1).ifFailure(errors::add);
    assert errors.isEmpty();

    Try.of(() -> 2 / 0).ifFailure(errors::add);
    assert !errors.isEmpty();
    assert errors.iterator().next() instanceof ArithmeticException;
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

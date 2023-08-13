package functions.recursivelambda;

import org.testng.annotations.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.IntUnaryOperator;

import static org.testng.Assert.assertEquals;

public class RecursableTest
{
  @Test
  public void testFactorial()
  {
    final int factorialOf5 = 120;
    assertEquals(Recursable.factorial(5), factorialOf5);

    final int factorialOf10 = 3628800;
    assertEquals(Recursable.factorial(10), factorialOf10);

    // Demonstrates raw Recursable (Factorial)
    final Recursable<IntUnaryOperator> recFact = new Recursable<>();
    final IntUnaryOperator fact = recFact.withFunc(
            x -> (x == 0) ? 1 : x * recFact.getFunc().applyAsInt(x - 1))
        .getFunc();
    assertEquals(fact.applyAsInt(5), factorialOf5);
    assertEquals(fact.applyAsInt(10), factorialOf10);
  }

  @Test
  public void testFibonacci()
  {
    final long fibonacci5 = 5;
    assertEquals(Recursable.fibonacci(5), fibonacci5);

    final long fibonacci10 = 55;
    assertEquals(Recursable.fibonacci(10), fibonacci10);

    // Demonstrates raw Recursable (Fibonacci)
    final Recursable<BiFunction<Map<Integer, Integer>, Integer, Integer>> recFib = new Recursable<>();
    final BiFunction<Map<Integer, Integer>, Integer, Integer> fib = recFib.withFunc(
            (m, x) -> m.computeIfAbsent(x, y -> recFib.getFunc().apply(m, y - 2) + recFib.getFunc().apply(m, y - 1)))
        .getFunc();
    final Map<Integer, Integer> map = new ConcurrentHashMap<>(Map.of(0, 0, 1, 1));
    assertEquals((int) fib.apply(map, 5), fibonacci5);
    assertEquals((int) fib.apply(map, 10), fibonacci10);
  }
}
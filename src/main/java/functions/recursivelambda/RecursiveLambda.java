package functions.recursivelambda;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.IntUnaryOperator;

class RecursiveLambda
{
  public static void main(String[] args)
  {
    System.out.println("**************** Factorial ****************");
    System.out.println("Factorial of 5 = " + Recursable.factorial(5));
    System.out.println("Factorial of 10 = " + Recursable.factorial(10));

    System.out.println("**************** Raw Recursable (Factorial) ***********");
    final Recursable<IntUnaryOperator> recFact = new Recursable<>();
    final IntUnaryOperator fact = recFact.withFunc(
            x -> (x == 0) ? 1 : x * recFact.getFunc().applyAsInt(x - 1))
        .getFunc();
    System.out.println("Raw factorial of 5 = " + fact.applyAsInt(5));
    System.out.println("Raw factorial of 10 = " + fact.applyAsInt(10));
    System.out.println();

    System.out.println("**************** Fibonacci ****************");
    System.out.println("5th Fibonacci is " + Recursable.fibonacci(5));
    System.out.println("10th Fibonacci is " + Recursable.fibonacci(10));

    System.out.println("**************** Raw Recursable (Fibonacci) ***********");
    final Recursable<BiFunction<Map<Integer, Integer>, Integer, Integer>> recFib = new Recursable<>();
    final BiFunction<Map<Integer, Integer>, Integer, Integer> fib = recFib.withFunc(
            (m, x) -> m.computeIfAbsent(x, y -> recFib.getFunc().apply(m, y - 2) + recFib.getFunc().apply(m, y - 1)))
        .getFunc();
    final Map<Integer, Integer> map = new ConcurrentHashMap<>(Map.of(0, 0, 1, 1));
    System.out.println("5th Fibonacci (raw) is " + fib.apply(map, 5));
    System.out.println("10th Fibonacci (raw) is " + fib.apply(map, 10));
  }
  

}

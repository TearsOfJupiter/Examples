import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.IntToDoubleFunction;

class RecursiveLambda
{
  public static void main(String[] args)
  {
    System.out.println("**************** Factorial ****************");
    System.out.println("Factorial of 5 = " + Recursable.factorial(5));
    System.out.println("Factorial of 10 = " + Recursable.factorial(10));
    System.out.println("**************** Fibonacci ****************");
    System.out.println("Fibonacci of 5 = " + Recursable.fibonacci(5));
    System.out.println("Fibonacci of 10 = " + Recursable.fibonacci(10));
  }
  
  // @param <I> - Functional Interface Type
  private static class Recursable<I>
  {
    private I func;

    private static double factorial(final int n)
    {
      Recursable<IntToDoubleFunction> recursable = new Recursable<>();
      recursable.func = x -> (x == 0) ? 1 : x * recursable.func.applyAsDouble(x - 1);
      return recursable.func.applyAsDouble(n);
    }
    
    private static long fibonacci(final int n)
    {
      Map<Integer, Long> map = new HashMap<>();
      map.put(0, 0L);
      map.put(1, 1L);
      Recursable<BiFunction<Map<Integer, Long>, Integer, Long>> recursable = new Recursable<>();
      recursable.func = (m, x) -> m.computeIfAbsent(x, y -> recursable.func.apply(m, y - 2) + recursable.func.apply(m, y - 1));
      return recursable.func.apply(map, n);
    }
  }
}

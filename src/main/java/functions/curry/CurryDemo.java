package functions.curry;

import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;

public class CurryDemo
{
  public static void main(String[] args)
  {
    // Demonstrates Currier::curry(BiFunction)
    System.out.println("Demonstrates Currier::curry(BiFunction):");
    final Integer biCurried = Currier.curry(Integer::sum)
        .apply(1)
        .apply(2);
    System.out.println(biCurried + " == " + 3);
    System.out.println();

    // Demonstrates a quad-curried function, first instantiated, then applied and stored off as a BiFunction, then later finally applied
    System.out.println("Demonstrates a quad-curried function, first instantiated, then applied and stored off as a BiFunction, then later finally applied:");
    final QuadCurriedIntBinaryOperator quadFunction =
        (a, b) -> (c, d) -> a + b + c + d;
    final IntBinaryOperator curried = quadFunction.apply(1, 2);
    //...
    final Integer quadCurriedResult = curried.applyAsInt(3, 4);
    System.out.println(quadCurriedResult + " == " + 10);
    System.out.println();
    Integer curriedResult = Currier.curryInt(quadFunction.apply(1, 2))
        .apply(3)
        .applyAsInt(4);
    System.out.println(curriedResult + " == " + 10);
  }
}

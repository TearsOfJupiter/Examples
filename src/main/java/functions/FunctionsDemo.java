package functions;

import functions.arity.TriFunction;
import functions.arity.VariadicFunction;
import functions.curry.Currier;
import functions.curry.QuadCurriedFunction;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class FunctionsDemo
{
  public static void main(String[] args)
  {
    /* ****************** VARIADIC ***************** */

    // Demonstrates TriFunction
    System.out.println("Demonstrates TriFunction:");
    final TriFunction<Integer, Integer, Integer, Integer> triFunction =
        (a, b, c) -> a + b + c;
    System.out.println(triFunction.apply(1, 2, 3) + " == " + 6);
    System.out.println();

    // Demonstrates VariadicFunction::apply that takes var-args
    System.out.println("Demonstrates VariadicFunction::apply that takes var-args:");
    final VariadicFunction<Integer, Integer> vf = array -> Arrays.stream(array)
        .mapToInt(Integer::intValue)
        .sum();
    System.out.println(vf.apply(1, 2, 3, 4, 5, 6, 7, 8, 9, 10) + " == " + 55);
    System.out.println(vf.apply(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).toArray(new Integer[0])) + " == " + 55);
    System.out.println(vf.apply() + " == " + 0);
    System.out.println();

    // Demonstrates VariadicFunction::of(VariadicFunction) in order to immediately call .apply(...)
    System.out.println("Demonstrates VariadicFunction::of(VariadicFunction) in order to immediately call .apply(...):");
    final Integer sum = VariadicFunction.<Integer, Integer>of(
            array -> Arrays.stream(array).mapToInt(Integer::intValue).sum())
        .apply(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    System.out.println(sum + " == " + 55);
    System.out.println();

    /* ****************** CURRYING ***************** */

    // Demonstrates Currier::curry(BiFunction)
    System.out.println("Demonstrates Currier::curry(BiFunction):");
    final Integer biCurried = Currier.curry(Integer::sum)
        .apply(1)
        .apply(2);
    System.out.println(biCurried + " == " + 3);
    System.out.println();

    // Demonstrates a quad-curried function, first stored applied and stored off as a BiFunction, then later finally applied
    System.out.println("Demonstrates a quad-curried function, first stored applied and stored off as a BiFunction, then later finally applied:");
    final QuadCurriedFunction<Integer, Integer, Integer, Integer, Integer> quadFunction =
        (a, b) -> (c, d) -> a + b + c + d;
    BiFunction<Integer, Integer, Integer> curried = quadFunction.apply(1, 2);
    //...
    Integer quadCurriedResult = curried.apply(3, 4);
    System.out.println(quadCurriedResult + " == " + 10);
    System.out.println();
    Integer curriedResult = Currier.curry(quadFunction.apply(1, 2))
        .apply(3)
        .apply(4);
    System.out.println(curriedResult + " == " + 10);
  }
}

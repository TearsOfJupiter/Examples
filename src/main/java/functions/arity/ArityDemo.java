package functions.arity;

import java.util.Arrays;
import java.util.List;

public class ArityDemo
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

    // Demonstrates VariadicIntOperator::apply(T...)
    System.out.println("Demonstrates VariadicIntOperator::apply(T...):");
    final VariadicIntOperator variadicIntOperator = (Integer... array) -> Arrays.stream(array)
        .mapToInt(Integer::intValue)
        .sum();
    System.out.println(variadicIntOperator.apply(1, 2, 3, 4, 5, 6, 7, 8, 9, 10) + " == " + 55);
    System.out.println("Demonstrates VariadicIntOperator::apply():");
    System.out.println(variadicIntOperator.apply() + " == " + 0);
    System.out.println();

    // Demonstrates VariadicIntOperator::apply(Collection<Integer>)
    System.out.println("Demonstrates VariadicIntOperator::apply(Collection<Integer>):");
    System.out.println(variadicIntOperator.apply(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)) + " == " + 55);
    System.out.println();

    // Demonstrates VariadicIntOperator::of(VariadicIntOperator) in order to immediately call .apply(...)
    System.out.println("Demonstrates VariadicIntOperator::of(VariadicIntOperator) in order to immediately call .apply(...):");
    final Integer sum = VariadicIntOperator.of(
            array -> Arrays.stream(array).mapToInt(Integer::intValue).sum())
        .apply(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    System.out.println(sum + " == " + 55);
    System.out.println();

    // Demonstrates VariadicConsumer::accept(T...)
    System.out.println("Demonstrates VariadicConsumer::accept(T...):");
    final VariadicConsumer<String> printer = (String... x) -> System.out.println(String.join(" ", x));
    printer.accept("one", "two", "three", "four");
    System.out.println();
  }
}

package functions.arity;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class VariadicIntOperatorTest
{
  @Test
  public void testVariadicIntOperator()
  {
    // Demonstrates VariadicIntOperator::apply(Integer...)
    final VariadicIntOperator variadicIntOperator = (Integer... array) ->
        Arrays.stream(array)
            .mapToInt(Integer::intValue)
            .sum();
    assertEquals(variadicIntOperator.apply(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 55);

    // Demonstrates passing no arguments
    assertEquals(variadicIntOperator.apply(), 0);

    // Demonstrates VariadicIntOperator::apply(Collection<Integer>)
    assertEquals((int) variadicIntOperator.apply(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)), 55);

    // Demonstrates VariadicIntOperator::of(Integer...)
    final int product = VariadicIntOperator.of((Integer... array) ->
        Arrays.stream(array)
            .mapToInt(Integer::intValue)
            .reduce((a, b) -> a * b)
            .orElseThrow()
        ).apply(1, 2, 3, 4);
    assertEquals(product, 24);
  }
}
package functions.arity;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class VariadicFunctionTest
{
  @Test
  public void testVariadicFunction()
  {
    final VariadicFunction<Integer, String> variadicFunction = (Integer... array) ->
        Arrays.stream(array)
            .map(String::valueOf)
            .collect(Collectors.joining(","));
    final String result = variadicFunction.apply(1, 2, 3, 4);
    assertEquals(result, "1,2,3,4");

    // Demonstrates passing no arguments
    assertTrue(variadicFunction.apply().isEmpty());

    // Demonstrates FunctionUtils::of(VariadicFunction)
    final int sum = VariadicFunction.of((Integer... array) ->
        Arrays.stream(array)
            .mapToInt(Integer::intValue)
            .sum()
        ).apply(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    assertEquals(sum, 55);
  }
}
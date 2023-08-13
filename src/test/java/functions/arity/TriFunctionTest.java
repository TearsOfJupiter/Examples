package functions.arity;

import functions.FunctionUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TriFunctionTest
{
  @Test
  public void testTriFunction()
  {
    final TriFunction<Integer, Integer, Integer, Integer> triFunction =
        (a, b, c) -> a + b + c;
    assertEquals((int) triFunction.apply(1, 2, 3), 6);

    assertEquals(FunctionUtils.<Integer, Integer, Integer, Integer>of((a, b, c) -> a * b * c)
        .apply(1, 2, 3), 6);
  }
}
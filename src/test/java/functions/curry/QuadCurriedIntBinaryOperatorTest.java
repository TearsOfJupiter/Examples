package functions.curry;

import org.testng.annotations.Test;

import java.util.function.IntBinaryOperator;

import static org.testng.Assert.assertEquals;

public class QuadCurriedIntBinaryOperatorTest
{
  @Test
  public void testQuadCurriedIntBinaryOperator()
  {
    // Demonstrates a quad-curried function, first instantiated, then applied and stored off as a BiFunction, then later finally applied
    final QuadCurriedIntBinaryOperator quadFunction =
        (a, b) -> (c, d) -> a + b + c + d;
    final IntBinaryOperator curried = quadFunction.apply(1, 2);

    //...
    final int quadCurriedResult = curried.applyAsInt(3, 4);
    assertEquals(quadCurriedResult, 10);

    final int curriedResult = Currier.curryInt(quadFunction.apply(1, 2))
        .apply(3)
        .applyAsInt(4);
    assertEquals(curriedResult, 10);
  }
}
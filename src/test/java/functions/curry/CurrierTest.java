package functions.curry;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CurrierTest
{
  @Test
  public void testCurryBiFunction()
  {
    // Demonstrates Currier::curry(BiFunction)
    final Integer biCurried = Currier.curry(Integer::sum)
        .apply(1)
        .apply(2);
    assertEquals((int) biCurried, 3);
  }

  @Test
  public void testCurryTriFunction()
  {
    // Demonstrates Currier::curry(TriFunction)
    final Integer triCurried = Currier.<Integer, Integer, Integer, Integer>curry((a, b, c) -> a + b + c)
        .apply(1)
        .apply(2)
        .apply(3);
    assertEquals((int) triCurried, 6);
  }

  @Test
  public void testCurryInt()
  {
    final int sum = Currier.curryInt(Integer::sum)
        .apply(1)
        .applyAsInt(2);
    assertEquals(sum, 3);
  }
}
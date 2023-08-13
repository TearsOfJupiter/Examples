package functions;

import org.testng.annotations.Test;
import util.Pojo;

import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;

public class FunctionUtilsTest
{

  @Test
  public void testOfFunction()
  {
    final int val = FunctionUtils.<Integer, Integer>of(i -> i / 2).apply(4);
    assertEquals(val, 2);
  }

  @Test
  public void testOfBiFunction()
  {
    final int greater = FunctionUtils.<Integer, Integer, Integer>of((a, b) -> a > b ? a : b).apply(1, 2);
    assertEquals(greater, 2);
  }

  @Test
  public void testOfTriFunction()
  {
    final int least = FunctionUtils.<Integer, Integer, Integer, Integer>of((a, b, c) ->
        Stream.of(a, b, c)
            .mapToInt(Integer::intValue)
            .min()
            .orElseThrow()
        ).apply(3, 2, 1);
    assertEquals(least, 1);
  }

  @Test
  public void testOfConsumer()
  {
    final Pojo pojo = new Pojo();
    FunctionUtils.<Integer>of(pojo::setIntProp)
        .accept(1);
    assertEquals(pojo.getIntProp(), 1);
  }

  @Test
  public void testOfBiConsumer()
  {
    final Pojo hungry = new Pojo();
    FunctionUtils.<Integer, Integer>of((a, b) -> hungry.setIntProp(a > b ? a : b))
        .accept(1, 2);
    assertEquals(hungry.getIntProp(), 2);
  }

  @Test
  public void testOfTriConsumer()
  {
    final Pojo pojo = new Pojo();
    FunctionUtils.<String, String, String>of((a, b, c) -> pojo.setStringProp(String.join(" ", a, b, c)))
        .accept("one", "two", "three");
    assertEquals(pojo.getStringProp(), "one two three");
  }

  @Test
  public void testTestOf5()
  {
  }
}
package functions.arity;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class VariadicConsumerTest
{
  @Test
  public void testVariadicConsumer()
  {
    final List<Integer> ints = new ArrayList<>();
    final VariadicConsumer<Integer> consumer = (Integer... array) ->
        ints.addAll(Arrays.asList(array));
    consumer.accept(1, 2, 3, 4, 5);
    assertEquals(ints, List.of(1, 2, 3, 4, 5));
  }
}
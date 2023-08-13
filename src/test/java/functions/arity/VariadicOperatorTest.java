package functions.arity;

import org.testng.annotations.Test;
import util.Builder;
import util.Pojo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.testng.Assert.assertEquals;

public class VariadicOperatorTest
{
  @Test
  public void testVariadicOperator()
  {
    final VariadicOperator<String> operator = (String... array) ->
        Arrays.stream(array)
            .reduce((a, b) -> a + b)
            .orElseThrow();
    assertEquals(operator.apply("1", "2", "3", "4", "5"), "12345");

    // Demonstrates VariadicOperator::of(T...)
    final List<Pojo> pojos = IntStream.range(1, 6)
        .mapToObj(i -> Builder.of(Pojo::new).with(Pojo::setIntProp, i).build())
        .toList();

    final Pojo superPojo = VariadicOperator.of((Pojo... array) ->
        Builder.of(Pojo::new)
            .with(Pojo::setIntProp,
                Arrays.stream(array)
                    .map(Pojo::getIntProp)
                    .mapToInt(Integer::intValue)
                    .sum())
            .build()
        ).apply(pojos.toArray(Pojo[]::new));

    assertEquals(superPojo.getIntProp(), 15);
  }
}
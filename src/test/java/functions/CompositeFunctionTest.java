package functions;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;
import util.Builder;
import util.Pojo;

import static org.testng.Assert.*;

public class CompositeFunctionTest
{
  @Test
  public void testOf()
  {
    final Pojo pojo = Builder.of(Pojo::new)
        .with(Pojo::setIntProp, 5)
        .build();
    final int i = CompositeFunction.of(Pojo::getIntProp)
        .apply(pojo);
    assertEquals(i, 5);
  }

  @Test
  public void testOfWithChild()
  {
    final Pojo pojo = Builder.of(Pojo::new)
        .with(Pojo::setIntProp, 5)
        .build();
    final boolean isFive = CompositeFunction.of(
            Pojo::getIntProp,
            CompositeFunction.of(
                String::valueOf,
                CompositeFunction.of(
                    sVal -> Builder.of(Pojo::new)
                        .with(Pojo::setStringProp, sVal)
                        .build(),
                    CompositeFunction.of(p -> StringUtils.equals(p.getStringProp(), "5")))))
        .apply(pojo);
    assertTrue(isFive);
  }
}
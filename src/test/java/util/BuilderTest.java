package util;

import org.testng.annotations.Test;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.testng.Assert.*;

public class BuilderTest
{
  /**
   * Demonstrates {@link Builder#with(BiConsumer, Object)}
   */
  @Test
  public void test_with()
  {
    final Pojo pojo1 = Builder.of(Pojo::new)
        .with(Pojo::setStringProp, "hello")
        .with(Pojo::setIntProp, 1)
        .with(Pojo::setBoolProp, true)
        .build();
    assertEquals(pojo1.getStringProp(), "hello");
    assertEquals(pojo1.getIntProp(), 1);
    assertTrue(pojo1.isBoolProp());
  }

  /**
   * Demonstrates {@link Builder#of(Supplier, Consumer[])}
   */
  @Test
  public void test_of()
  {
    final Pojo pojo2 = Builder.of(Pojo::new,
        pojo -> pojo.setStringProp("goodbye"),
        pojo -> pojo.setIntProp(2)
    );
    assertEquals(pojo2.getStringProp(), "goodbye");
    assertEquals(pojo2.getIntProp(), 2);
    assertFalse(pojo2.isBoolProp());
  }
}
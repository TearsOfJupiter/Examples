package util;

import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.testng.Assert.*;

public class VisitorTest
{
  @Test
  public void testChaining()
  {
    final Pojo pojo1 = new Pojo();
    assertEquals(pojo1.getIntProp(), 0);
    assertNull(pojo1.getStringProp());
    assertFalse(pojo1.isBoolProp());

    final Pojo pojo2 = new Pojo();
    assertEquals(pojo2.getIntProp(), 0);
    assertNull(pojo2.getStringProp());
    assertFalse(pojo2.isBoolProp());

    final List<Pojo> pojos = Stream.of(pojo1, pojo2)
          // Demonstrates .visit(Consumer<T>, T)
        .map(pojo -> Visitor.visit(p -> p.setBoolProp(true), pojo))
          // Demonstrates .visit(BiConsumer<T, U>, T, U)
        .map(pojo -> Visitor.visit(Pojo::setDoubleProp, pojo, 1.5))
          // Demonstrates .of(), .visit(), and .get()
        .map(pojo -> Visitor.of(pojo)
            .visit(Pojo::setStringProp, "string")
            .visit(Pojo::setIntProp, 5)
            .get())
        .toList();

    for (Pojo pojo : pojos)
    {
      assertEquals(pojo.getStringProp(), "string");
      assertEquals(pojo.getIntProp(), 5);
      assertEquals(pojo.getDoubleProp(), 1.5);
      assertTrue(pojo.isBoolProp());
    }
  }

  @Test
  public void test_visit_varConsumers()
  {
    final Pojo pojo = Visitor.visit(
        new Pojo(),
        p -> p.setStringProp("One"),
        p -> p.setIntProp(1),
        p -> p.setBoolProp(true)
    );
    assertEquals(pojo.getStringProp(), "One");
    assertEquals(pojo.getIntProp(), 1);
    assertTrue(pojo.isBoolProp());
  }

  @Test
  public void test_visitEach()
  {
    assertNull(Visitor.visitEach(null));

    final List<Pojo> pojos = Visitor.visitEach(List.of(new Pojo(), new Pojo()));
    pojos.forEach(pojo -> Visitor.visit(
        pojo,
        p -> assertEquals(p.getIntProp(), 0),
        p -> assertNull(p.getStringProp()),
        p -> assertFalse(p.isBoolProp())
    ));

    Visitor.visitEach(
        pojos,
        p -> p.setStringProp("string"),
        p -> p.setIntProp(1),
        p -> p.setBoolProp(true)
    ).forEach(pojo -> Visitor.visit(
        pojo,
        p -> assertEquals(p.getIntProp(), 1),
        p -> assertEquals(p.getStringProp(), "string"),
        p -> assertTrue(p.isBoolProp())
    ));
  }
}
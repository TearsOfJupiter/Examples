package functions.util;

import java.util.List;
import java.util.stream.Stream;

public class VisitorDemo
{
  public static void main(String[] args)
  {
    final Pojo pojo1 = new Pojo();
    assert pojo1.getIntProp() == 0;
    assert pojo1.getStringProp() == null;
    assert !pojo1.isBoolProp();

    final Pojo pojo2 = new Pojo();
    assert pojo2.getIntProp() == 0;
    assert pojo2.getStringProp() == null;
    assert !pojo2.isBoolProp();

    final List<Pojo> pojos = Stream.of(pojo1, pojo2)
            // Demonstrates .visit(Consumer<T>, T)
        .map(pojo -> Visitor.visit(p -> p.setBoolProp(true), pojo))
            // Demonstrates .visit(BiConsumer<T, U>, T, U)
        .map(pojo -> Visitor.visit(Pojo::setDoubleProp, pojo, 1.5))
            // Demonstrates .of(), .visit(BiConsumer<T, U>, U), and .get()
        .map(pojo -> Visitor.of(pojo)
            .visit(Pojo::setStringProp, "goodbye")
            .visit(Pojo::setIntProp, 5)
            .get())
        .toList();
    for (Pojo pojo : pojos)
    {
      assert "goodbye".equals(pojo.getStringProp());
      assert 5 == pojo.getIntProp();
      assert 1.5 == pojo.getDoubleProp();
      assert pojo.isBoolProp();
    }

    /*
     * Demonstrates .visit() with var-arg consumers
     */
    final Pojo pojo = new Pojo();
    Visitor.visit(pojo,
        p -> p.setStringProp("One"),
        p -> p.setIntProp(1),
        p -> p.setBoolProp(true)
    );
    assert "One".equals(pojo.getStringProp());
    assert 1 == pojo.getIntProp();
    assert pojo.isBoolProp();
  }

}

package util;

public class BuilderDemo
{
  public static void main(String[] args)
  {
    /*
     * Demonstrates chained .with() calls
     */
    final Pojo pojo1 = Builder.of(Pojo::new)
        .with(Pojo::setStringProp, "hello")
        .with(Pojo::setIntProp, 1)
        .with(Pojo::setBoolProp, true)
        .build();
    assert "hello".equals(pojo1.getStringProp());
    assert 1 == pojo1.getIntProp();
    assert pojo1.isBoolProp();

    /*
     * Demonstrates .of() with var-arg consumers
     */
    final Pojo pojo2 = Builder.of(Pojo::new,
        pojo -> pojo.setStringProp("goodbye"),
        pojo -> pojo.setIntProp(2)
    );
    assert "goodbye".equals(pojo2.getStringProp());
    assert 2 == pojo2.getIntProp();
    assert !pojo2.isBoolProp();
  }
}
package monads;

import org.testng.annotations.Test;
import util.Builder;
import util.Pojo;

import java.util.Random;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class WhenTest
{
  @Test
  public void test_when_thenDo()
  {
    final Runnable under4 = mock(Runnable.class);
    final Runnable under7 = mock(Runnable.class);
    final Runnable under10 = mock(Runnable.class);
    final Runnable equals10 = mock(Runnable.class);

    final Pojo pojo = getPojo();

    final Runnable action;
    if (pojo.getIntProp() < 4)
      action = under4;
    else if (pojo.getIntProp() < 7)
      action = under7;
    else if (pojo.getIntProp() < 10)
      action = under10;
    else
      action = equals10;

    When.<Pojo, Runnable>newWhen(
              p -> p.getIntProp() < 4)
        .thenDo(under4)
        .when(p -> p.getIntProp() < 7)
        .thenDo(under7)
        .when(p -> p.getIntProp() < 10)
        .thenDo(under10)
        .elseDo(equals10)
        .test(pojo)
        .doAction();
    verify(action, times(1))
        .run();
  }

  @Test
  public void test_whenThenDo()
  {
    final Runnable under4 = mock(Runnable.class);
    final Runnable under7 = mock(Runnable.class);
    final Runnable under10 = mock(Runnable.class);
    final Runnable equals10 = mock(Runnable.class);

    final Pojo pojo = getPojo();

    final Runnable action;
    if (pojo.getIntProp() < 4)
      action = under4;
    else if (pojo.getIntProp() < 7)
      action = under7;
    else if (pojo.getIntProp() < 10)
      action = under10;
    else
      action = equals10;

    When.<Pojo, Runnable>newWhenThenDo(
                    p -> p.getIntProp() < 4,  under4)
        .whenThenDo(p -> p.getIntProp() < 7,  under7)
        .whenThenDo(p -> p.getIntProp() < 10, under10)
        .elseDo(equals10)
        .test(pojo)
        .doAction();
    verify(action, times(1))
        .run();
  }

  @Test
  public void test_when_thenGet()
  {
    final Pojo pojo = getPojo();

    final String under4 = "under4";
    final String under7 = "under7";
    final String under10 = "under10";
    final String equals10 = "equals10";

    final String expectedResult;
    if (pojo.getIntProp() < 4)
      expectedResult = under4;
    else if (pojo.getIntProp() < 7)
      expectedResult = under7;
    else if (pojo.getIntProp() < 10)
      expectedResult = under10;
    else
      expectedResult = equals10;

    final String result = When.<Pojo, String>newWhen(
              p -> p.getIntProp() < 4)
        .thenGet(() -> under4)
        .when(p -> p.getIntProp() < 7)
        .thenGet(() -> under7)
        .when(p -> p.getIntProp() < 10)
        .thenGet(() -> under10)
        .elseGet(() -> equals10)
        .test(pojo)
        .get();
    assertEquals(result, expectedResult);
  }

  @Test
  public void test_whenThenGet()
  {
    final Pojo pojo = getPojo();

    final String under4 = "under4";
    final String under7 = "under7";
    final String under10 = "under10";
    final String equals10 = "equals10";

    final String expectedResult;
    if (pojo.getIntProp() < 4)
      expectedResult = under4;
    else if (pojo.getIntProp() < 7)
      expectedResult = under7;
    else if (pojo.getIntProp() < 10)
      expectedResult = under10;
    else
      expectedResult = equals10;

    final String result = When.<Pojo, String>newWhenThenGet(
                     p -> p.getIntProp() < 4,  () -> under4)
        .whenThenGet(p -> p.getIntProp() < 7,  () -> under7)
        .whenThenGet(p -> p.getIntProp() < 10, () -> under10)
        .elseGet(() -> equals10)
        .test(pojo)
        .get();
    assertEquals(result, expectedResult);
  }

  @Test
  public void test_when_thenAccept()
  {
    final Pojo pojo = getPojo();

    final Pojo under4 = new Pojo();
    final Pojo under7 = new Pojo();
    final Pojo under10 = new Pojo();
    final Pojo equals10 = new Pojo();

    final Pojo expected;
    if (pojo.getIntProp() < 4)
      expected = under4;
    else if (pojo.getIntProp() < 7)
      expected = under7;
    else if (pojo.getIntProp() < 10)
      expected = under10;
    else
      expected = equals10;

    When.<Pojo, Pojo>newWhen(
              p -> p.getIntProp() < 4)
        .thenAccept(p -> under4.setIntProp(p.getIntProp()))
        .when(p -> p.getIntProp() < 7)
        .thenAccept(p -> under7.setIntProp(p.getIntProp()))
        .when(p -> p.getIntProp() < 10)
        .thenAccept(p -> under10.setIntProp(p.getIntProp()))
        .elseAccept(p -> equals10.setIntProp(p.getIntProp()))
        .test(pojo)
        .accept(pojo);
    assertEquals(expected.getIntProp(), pojo.getIntProp());
  }

  @Test
  public void test_whenThenAccept()
  {
    final Pojo pojo = getPojo();

    final Pojo under4 = new Pojo();
    final Pojo under7 = new Pojo();
    final Pojo under10 = new Pojo();
    final Pojo equals10 = new Pojo();

    final Pojo expected;
    if (pojo.getIntProp() < 4)
      expected = under4;
    else if (pojo.getIntProp() < 7)
      expected = under7;
    else if (pojo.getIntProp() < 10)
      expected = under10;
    else
      expected = equals10;

    When.<Pojo, Pojo>newWhenThenAccept(
                        p -> p.getIntProp() < 4,  p -> under4.setIntProp(p.getIntProp()))
        .whenThenAccept(p -> p.getIntProp() < 7,  p -> under7.setIntProp(p.getIntProp()))
        .whenThenAccept(p -> p.getIntProp() < 10, p -> under10.setIntProp(p.getIntProp()))
        .elseAccept(p -> equals10.setIntProp(p.getIntProp()))
        .test(pojo)
        .accept(pojo);
    assertEquals(expected.getIntProp(), pojo.getIntProp());
  }

  @Test
  public void test_when_thenApply()
  {
    final String under4 = "under4";
    final String under7 = "under7";
    final String under10 = "under10";
    final String equals10 = "equals10";
    final QuadString quadString = new QuadString()
        .withA(under4)
        .withB(under7)
        .withC(under10)
        .withD(equals10);

    final String expected;
    if (quadString.getRand() < 4)
      expected = under4;
    else if (quadString.getRand() < 7)
      expected = under7;
    else if (quadString.getRand() < 10)
      expected = under10;
    else
      expected = equals10;

    final String result = When.<QuadString, String>newWhen(
              qs -> qs.getRand() < 4)
        .thenApply(QuadString::getA)
        .when(qs -> qs.getRand() < 7)
        .thenApply(QuadString::getB)
        .when(qs -> qs.getRand() < 10)
        .thenApply(QuadString::getC)
        .elseApply(QuadString::getD)
        .test(quadString)
        .apply(quadString);
    assertEquals(result, expected);
  }

  @Test
  public void test_whenThenApply()
  {
    final String under4 = "under4";
    final String under7 = "under7";
    final String under10 = "under10";
    final String equals10 = "equals10";
    final QuadString quadString = new QuadString()
        .withA(under4)
        .withB(under7)
        .withC(under10)
        .withD(equals10);

    final String expected;
    if (quadString.getRand() < 4)
      expected = under4;
    else if (quadString.getRand() < 7)
      expected = under7;
    else if (quadString.getRand() < 10)
      expected = under10;
    else
      expected = equals10;

    final String result = When.newWhenThenApply(
            qs -> qs.getRand() < 4,  QuadString::getA)
        .whenThenApply(qs -> qs.getRand() < 7,  QuadString::getB)
        .whenThenApply(qs -> qs.getRand() < 10, QuadString::getC)
        .elseApply(QuadString::getD)
        .test(quadString)
        .apply(quadString);
    assertEquals(result, expected);
  }

  private static Pojo getPojo()
  {
    return Builder.of(Pojo::new)
        .with(Pojo::setIntProp, new Random().nextInt(1, 11))
        .build();
  }

  private static class QuadString
  {
    private final int rand;
    private String a;
    private String b;
    private String c;
    private String d;

    public QuadString()
    {
      rand = new Random().nextInt(1, 11);
    }

    public int getRand()
    {
      return rand;
    }

    public String getA()
    {
      return a;
    }
    public QuadString withA(final String a)
    {
      this.a = a;
      return this;
    }

    public String getB()
    {
      return b;
    }
    public QuadString withB(final String b)
    {
      this.b = b;
      return this;
    }

    public String getC()
    {
      return c;
    }
    public QuadString withC(final String c)
    {
      this.c = c;
      return this;
    }

    public String getD()
    {
      return d;
    }
    public QuadString withD(final String d)
    {
      this.d = d;
      return this;
    }
  }
}
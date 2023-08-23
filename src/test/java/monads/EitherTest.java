package monads;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class EitherTest
{
  @Test
  public void test_either()
  {
    final String s1 = "1";
    final String s2 = "2";
    assertEquals(Either.either(s1, s2), s1);
    assertEquals(Either.either(s2, s1), s2);
    assertEquals(Either.either(s1, null), s1);
    assertEquals(Either.either(null, s2), s2);
    assertNull(Either.either(null, null));
  }
}
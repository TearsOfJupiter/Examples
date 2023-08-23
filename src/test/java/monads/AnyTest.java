package monads;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class AnyTest
{
  @Test
  public void testAny()
  {
    final String s1 = "1";
    final String s2 = "2";
    final String s3 = "3";
    final String s4 = "4";
    assertEquals(Any.any(s1, s2, s3, s4), s1);
    assertEquals(Any.any(null, s2, null, s4), s2);
    assertEquals(Any.any(null, null, null, s4), s4);
    assertNull(Any.any(null, null, null));
    assertNull(Any.any());
  }
}
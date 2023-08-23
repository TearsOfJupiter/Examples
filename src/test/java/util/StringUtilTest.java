package util;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class StringUtilTest
{
  @Test
  public void test_surroundWith_bookends()
  {
    final String s = "4";
    final String bookends = "3";
    final String expected = "343";
    assertEquals(StringUtil.surroundWith(s, bookends), expected);
  }

  @Test
  public void test_surroundWith()
  {
    final String s = "1";
    final String left = "{";
    final String right = "}";
    final String expected = "{1}";
    assertEquals(StringUtil.surroundWith(s, left, right), expected);
  }

  @Test
  public void test_parenthesize()
  {
    final String s = "1 and 2";
    final String expected = "(1 and 2)";
    assertEquals(StringUtil.parenthesize(s), expected);
  }
}
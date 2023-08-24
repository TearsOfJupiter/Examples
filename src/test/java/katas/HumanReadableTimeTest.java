package katas;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class HumanReadableTimeTest
{
  @Test
  public void test_makeReadable()
  {
    assertEquals(HumanReadableTime.makeReadable(86399), "23:59:59");
    assertEquals(HumanReadableTime.makeReadable(0), "00:00:00");
    assertEquals(HumanReadableTime.makeReadable(5), "00:00:05");
    assertEquals(HumanReadableTime.makeReadable(60), "00:01:00");
    assertEquals(HumanReadableTime.makeReadable(359999), "99:59:59");
  }
}
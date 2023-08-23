package util;

import org.testng.annotations.Test;

public class PojoTest
{
  @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "kablooey!")
  public void testKablooey()
  {
    new Pojo().kablooey();
  }
}
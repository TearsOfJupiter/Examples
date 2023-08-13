package util;

import org.testng.annotations.Test;

import java.util.function.Function;

public class ExceptionUtilTest
{
  @Test(expectedExceptions = Exception.class, expectedExceptionsMessageRegExp = "yikes!")
  public void testThrowAsUnchecked()
  {
    final Function<Integer, String> badFunction = i -> {throw ExceptionUtil.throwAsUnchecked(new Exception("yikes!"));};
    badFunction.apply(1);
  }
}
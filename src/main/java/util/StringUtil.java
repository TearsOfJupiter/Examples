package util;

public class StringUtil
{
  private StringUtil() {}

  /* *******************************************************************************************************************
   *                                              TRANSFORMATIVE METHODS
   * ***************************************************************************************************************** */

  public static String surroundWith(final String s, final String bookends)
  {
    return surroundWith(s, bookends, bookends);
  }
  public static String surroundWith(final String s, final String left, final String right)
  {
    return left + s + right;
  }

  public static String parenthesize(final String s)
  {
    return surroundWith(s, "(", ")");
  }
}
package parsing;

import org.apache.commons.lang3.StringUtils;
import util.ExceptionUtil;
import util.StringUtil;

public enum BooleanAlgebraOperator
{
  AND("and"),
  OR("or");

  private final String value;
  BooleanAlgebraOperator(final String value)
  {
    this.value = value;
  }

  public static BooleanAlgebraOperator from(final String operator)
  {
    if (StringUtils.equalsIgnoreCase(StringUtils.trim(operator), AND.value))
      return AND;
    else if (StringUtils.equalsIgnoreCase(StringUtils.trim(operator), OR.value))
      return OR;
    else
      throw ExceptionUtil.throwAsUnchecked(new IllegalArgumentException("Illegal operator \"" + operator + "\""));
  }

  public String getValueWithSpaces()
  {
    return StringUtil.surroundWith(value, " ");
  }
}
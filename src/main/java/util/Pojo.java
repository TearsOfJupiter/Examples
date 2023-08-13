package util;

/**
 * This class demonstrates a POJO class that doesn't have builder-pattern "with" methods
 */
public class Pojo
{
  private String stringProp;
  private int intProp;
  private double doubleProp;
  private boolean boolProp;

  public String getStringProp()
  {
    return stringProp;
  }

  public void setStringProp(final String stringProp)
  {
    this.stringProp = stringProp;
  }

  public int getIntProp()
  {
    return intProp;
  }
  public void setIntProp(final int intProp)
  {
    this.intProp = intProp;
  }

  public double getDoubleProp()
  {
    return doubleProp;
  }
  public void setDoubleProp(final double doubleProp)
  {
    this.doubleProp = doubleProp;
  }

  public boolean isBoolProp()
  {
    return boolProp;
  }
  public void setBoolProp(final boolean boolProp)
  {
    this.boolProp = boolProp;
  }

  public Pojo kablooey()
  {
    throw ExceptionUtil.throwAsUnchecked(new IllegalStateException("kablooey!"));
  }
}
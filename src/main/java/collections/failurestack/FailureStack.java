package collections.failurestack;

import java.util.Stack;

@SuppressWarnings("rawtypes")
public class FailureStack extends Stack<FailureStackFrame>
{
  @SuppressWarnings("unchecked")
  public <C> C popVal()
  {
    if (isEmpty())
    {
      return null;
    }
    else
    {
      return (C) pop().getValue();
    }
  }
}
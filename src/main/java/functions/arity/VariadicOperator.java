package functions.arity;

public interface VariadicOperator<T> extends VariadicFunction<T, T>
{
  static <T> VariadicOperator<T> of(final VariadicOperator<T> operator)
  {
    return operator;
  }
}

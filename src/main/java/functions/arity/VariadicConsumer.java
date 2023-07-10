package functions.arity;

@FunctionalInterface
public interface VariadicConsumer<T>
{
  void accept(final T... ts);
}

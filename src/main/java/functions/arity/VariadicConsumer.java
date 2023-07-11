package functions.arity;

@SuppressWarnings("unchecked")
@FunctionalInterface
public interface VariadicConsumer<T>
{
  void accept(final T... ts);
}

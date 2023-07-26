package functions.util;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Builder<T>
{
  private T value;

  private Builder() {}

  public static <T> Builder<T> of(final Supplier<? extends T> supplier)
  {
    final Builder<T> builder = new Builder<>();
    builder.value = supplier.get();
    return builder;
  }

  public <U> Builder<T> with(final BiConsumer<? super T, ? super U> consumer,
                             final U value)
  {
    consumer.accept(this.value, value);
    return this;
  }

  public T build()
  {
    return value;
  }

  @SafeVarargs
  public static <T> T of(final Supplier<? extends T> supplier,
                         final Consumer<? super T>... consumers)
  {
    final T value = supplier.get();
    Arrays.asList(consumers).forEach(consumer -> consumer.accept(value));
    return value;
  }
}

package functions;

import functions.arity.TriConsumer;
import functions.arity.TriFunction;
import util.PassThru;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionUtils
{
  @PassThru
  public static <T, R> Function<? super T, ? extends R> of(final Function<? super T, ? extends R> function)
  {
    return function;
  }

  @PassThru
  public static <T, U, R> BiFunction<? super T, ? super U, ? extends R> of(final BiFunction<? super T, ? super U, ? extends R> function)
  {
    return function;
  }

  @PassThru
  public static <T, U, V, R> TriFunction<? super T, ? super U, ? super V, ? extends R> of(
      final TriFunction<? super T, ? super U, ? super V, ? extends R> triFunction)
  {
    return triFunction;
  }

  @PassThru
  public static <T> Consumer<? super T> of(final Consumer<? super T> consumer)
  {
    return consumer;
  }

  @PassThru
  public static <T, R> BiConsumer<? super T, ? super R> of(final BiConsumer<? super T, ? super R> consumer)
  {
    return consumer;
  }

  @PassThru
  public static <T, R, V> TriConsumer<? super T, ? super R, ? super V> of(final TriConsumer<? super T, ? super R, ? super V> consumer)
  {
    return consumer;
  }
}
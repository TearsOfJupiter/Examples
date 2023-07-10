package functions.curry;

import java.util.function.BiFunction;

public interface QuadCurriedFunction<T, U, V, W, R> extends BiFunction<T, U, BiFunction<V, W, R>>
{
}

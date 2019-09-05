import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

class DefaultUtils<T>
{
  private static DefaultUtils instance;
  private Optional<T> optional;
  
  private DefaultUtils() {}
  private static DefaultUtils getInstance()
  {
    instance = Optional.ofNullable(instance).orElseGet(DefaultUtils::new);
    return instance;
  }
  
  public static void main(String[] args)
  {
    final String _default = "default";

    System.out.println("******* valueOrDefault *******");
    String testString = "";
    System.out.println("testString=\"" + testString + "\", predicate=StringUtils::isNotEmpty, alternative=\"" + _default + "\", output should be \"" + _default + "\": " + DefaultUtils.valueOrDefault(testString, StringUtils::isNotEmpty, "default"));
    
    testString = "not empty";
    System.out.println("testString=\"" + testString + "\", predicate=StringUtils::isNotEmpty, alternative=\"" + _default + "\", output should be \"" + testString + "\": " + DefaultUtils.valueOrDefault(testString, StringUtils::isNotEmpty, "default"));

    System.out.println("******* ofNullable/withPredicate ********");
    testString = "ABC123456$%^";
    System.out.println("testString=\"" + testString + "\", predicate=StringUtils::isAlphanumeric, alternative=\"" + _default + "\", output should be \"" + _default + "\": " + DefaultUtils.ofNullable(testString).withPredicate(StringUtils::isAlphanumeric).orDefault(() -> "default"));
    testString = "ABC";
    System.out.println("testString=\"" + testString + "\", predicate=StringUtils::isAlphanumeric, alternative=\"" + _default + "\", output should be \"" + testString + "\": " + DefaultUtils.ofNullable(testString).withPredicate(StringUtils::isAlphanumeric).orDefault(() -> "default"));
  }

  public static <T> T valueOrDefault(@Nullable final T value, @Nonnull final Predicate<T> predicate, @Nonnull final Supplier<T> supplier)
  {
    return Optional.ofNullable(value)
        .filter(predicate)
        .orElseGet(supplier);
  }

  public static <T> T valueOrDefault(@Nullable final T value, @Nonnull final Predicate<T> predicate, @Nonnull final T alternative)
  {
    return valueOrDefault(value, predicate, (Supplier<T>) () -> alternative);
  }
  
  public static <T> DefaultUtils<T> ofNullable(@Nullable final T value)
  {
    getInstance().optional = Optional.ofNullable(value);
    return getInstance();
  }
  
  public <T> DefaultUtils<T> withPredicate(@Nonnull final Predicate<T> predicate)
  {
    instance.optional = instance.optional.filter(predicate);
    return instance;
  }
  
  public <T> T orDefault(@Nonnull final Supplier<T> supplier)
  {
    return (T) instance.optional.orElseGet(supplier);
  }
}

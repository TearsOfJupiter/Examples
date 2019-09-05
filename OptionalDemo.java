import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

class OptionalDemo
{
  public static void main(String[] args)
  {
    Integer rand = ThreadLocalRandom.current().nextInt();
    System.out.println(
        getEvenOddString_Imperative(rand).equals(getEvenOddString_ternary(rand)) &&
        getEvenOddString_Imperative(rand).equals(getEvenOddString_Functional(rand))
    );
  }
  
  public static String getEvenOddString_Imperative(Integer value)
  {
    if (value % 2 == 0)
      return "isEven";
    else
      return "isOdd";
  }
  
  public static String getEvenOddString_ternary(Integer value)
  {
    return value % 2 == 0 ? "isEven" : "isOdd";
  }
  
  public static String getEvenOddString_Functional(@Nonnull Integer value)
  {
    return Optional.of(value)
        .filter(r -> r % 2 == 0)
        .map(r -> "isEven")
        .orElseGet(() -> "isOdd");
  }
}

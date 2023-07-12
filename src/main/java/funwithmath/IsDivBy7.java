package funwithmath;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * Inspired by <a href="https://youtu.be/UDQjn_-pDSs">Numberphile</a>
 */
public class IsDivBy7
{
  public static void main(String[] args)
  {
    printIsDivBy7(3);
    printIsDivBy7(49);
    printIsDivBy7(63);
    printIsDivBy7(213);
    printIsDivBy7(210);
    printIsDivBy7(35864);
    IntStream.range(0, 10)
        .map(i -> ThreadLocalRandom.current()
            .nextInt(0, 1000))
        .forEach(IsDivBy7::printIsDivBy7);
  }

  private static void printIsDivBy7(final int num)
  {
    System.out.println(num + " divBy7? " + (isDivBy7(num) && num % 7 == 0) + ", " + num + " % 7 == " + num % 7);
  }

  private static boolean isDivBy7(final int num)
  {
    if (num < 7)
      return false;

    final int calc = 5 * (num % 10) + (num / 10);
    return calc >= 7
        && (calc == 7 || calc == num || // 7 ^ (2n)
            isDivBy7(calc));
  }
}

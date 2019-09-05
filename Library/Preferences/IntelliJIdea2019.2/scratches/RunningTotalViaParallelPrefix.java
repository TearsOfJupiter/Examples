import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class RunningTotalViaParallelPrefix
{
  public static void main(String[] args)
  {
    List<BigDecimal> list = Arrays.asList(new BigDecimal(1.00), new BigDecimal(2.00), new BigDecimal(3.00));
    BigDecimal[] array = (BigDecimal[]) list.toArray();
    BigDecimal[] runningTotalArray = Arrays.copyOf(array, array.length);
    process(runningTotalArray);
    System.out.println("array=" + Arrays.asList(array) + ", runningTotalsArray=" + Arrays.asList(runningTotalArray));
  }
  
  private static void process(BigDecimal[] array)
  {
    Arrays.parallelPrefix(array, BigDecimal::add);
  }
}
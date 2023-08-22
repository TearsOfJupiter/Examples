package util;

import monads.Try;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableFunction;
import org.apache.commons.lang3.function.FailableRunnable;
import org.apache.commons.lang3.function.FailableSupplier;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TimerTest
{
  private static final String S = "1";
  private static final int I = 1;

  @Test
  public void testTime_consumer()
  {
    final Set<String> strings = new HashSet<>();

    final FailableConsumer<String, Exception> consumer = s -> {
      Try.ofRunnable(() -> Thread.sleep(1000));
      strings.add(s);
    };

    final Set<Duration> durations = new HashSet<>();
    Timer.time(consumer, S, durations::add);

    assertTrue(strings.contains(S));
    final long durationMillis = durations.iterator().next().toMillis();
    assertTrue(durationMillis > 1000);
  }

  @Test
  public void testTime_function()
  {
    final FailableFunction<String, Integer, Exception> function = s -> {
      Try.ofRunnable(() -> Thread.sleep(1000));
      return Integer.parseInt(s);
    };

    final Set<Duration> durations = new HashSet<>();
    final int i = Timer.time(function, S, durations::add);
    assertEquals(i, I);

    final long durationMillis = durations.iterator().next().toMillis();
    assertTrue(durationMillis > 1000);
  }

  @Test
  public void testTime_supplier()
  {
    final FailableSupplier<String, Exception> supplier = () -> {
      Try.ofRunnable(() -> Thread.sleep(1000));
      return S;
    };

    final Set<Duration> durations = new HashSet<>();
    final String s = Timer.time(supplier, durations::add);
    assertEquals(s, S);

    final long durationMillis = durations.iterator().next().toMillis();
    assertTrue(durationMillis > 1000);
  }

  @Test
  public void testTime_runnable()
  {
    final Set<String> strings = new HashSet<>();
    final FailableRunnable<Exception> runnable = () -> {
      Try.ofRunnable(() -> Thread.sleep(1000));
      strings.add(S);
    };

    final Set<Duration> durations = new HashSet<>();
    Timer.time(runnable, durations::add);
    assertTrue(strings.contains(S));

    final long durationMillis = durations.iterator().next().toMillis();
    assertTrue(durationMillis > 1000);
  }
}
package util;

import monads.Try;
import org.apache.commons.lang3.function.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class Timer implements AutoCloseable
{
  private final Instant start;
  private final FailableConsumer<? super Duration, ? extends Throwable> timeConsumer;

  private Timer(final FailableConsumer<? super Duration, ? extends Throwable> timeConsumer)
  {
    this.start = Instant.now();
    this.timeConsumer = Objects.requireNonNull(timeConsumer);
  }

  @Override
  public void close()
  {
    Try.ofRunnable(() ->
            timeConsumer.accept(Duration.between(start, Instant.now())))
        .orElseThrow();
  }

  public static <T> void time(final FailableConsumer<? super T, ? extends Throwable> consumer,
                              final T val,
                              final FailableConsumer<? super Duration, ? extends Throwable> timeConsumer)
  {
    try (Timer ignored = new Timer(timeConsumer))
    {
      Failable.accept(Objects.requireNonNull(consumer), val);
    }
  }

  public static <T, R> R time(final FailableFunction<? super T, ? extends R, ? extends Throwable> mapper,
                              final T val,
                              final FailableConsumer<? super Duration, ? extends Throwable> timeConsumer)
  {
    try (Timer ignored = new Timer(timeConsumer))
    {
      return Failable.apply(Objects.requireNonNull(mapper), val);
    }
  }

  public static <T> T time(final FailableSupplier<? extends T, ? extends Throwable> supplier,
                           final FailableConsumer<? super Duration, ? extends Throwable> timeConsumer)
  {
    try (Timer ignored = new Timer(timeConsumer))
    {
      return Failable.get(Objects.requireNonNull(supplier));
    }
  }

  public static void time(final FailableRunnable<? extends Throwable> runnable,
                          final FailableConsumer<? super Duration, ? extends Throwable> timeConsumer)
  {
    try (Timer ignored = new Timer(timeConsumer))
    {
      Failable.run(Objects.requireNonNull(runnable));
    }
  }
}
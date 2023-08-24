package katas;

public class HumanReadableTime
{
  public static String makeReadable(int seconds)
  {
    return
        String.format("%02d", (int) Math.floor((float) seconds / 3600)) + ":" +
        String.format("%02d", (int) Math.floor((float) seconds / 60) % 60) + ":" +
        String.format("%02d", (seconds % 60));
  }
}
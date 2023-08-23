package util.tuples;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class TuplesTest
{
  @Test
  public void testTuples()
  {
    final Pair<Integer, String> pair = new Pair<>(1, "1");
    assertEquals((int) pair.getA(), 1);
    assertEquals(pair.getB(), "1");
    assertEquals(pair, pair);

    final Twin<String> twin = new Twin<>("a", "b");
    assertEquals(twin.getA(), "a");
    assertEquals(twin.getB(), "b");

    final Trio<Integer, String, String> trio = Trio.from(pair, "uno");
    assertEquals((int) trio.getA(), 1);
    assertEquals(trio.getB(), "1");
    assertEquals(trio.getC(), "uno");

    final Triplet<String> triplet = Triplet.from(twin, "c");
    assertEquals(triplet.getA(), "a");
    assertEquals(triplet.getB(), "b");
    assertEquals(triplet.getC(), "c");
  }

  @SuppressWarnings("AssertBetweenInconvertibleTypes")
  @Test
  public void test_equals()
  {
    final Pair<String, Integer> pair1 = new Pair<>("1", 1);
    final Pair<String, Integer> pair2 = new Pair<>("1", 1);
    final Pair<String, Integer> pair3 = new Pair<>("2", 2);
    assertEquals(pair1, pair2);
    assertNotEquals(pair1, pair3);
    assertNotEquals(pair1, "And now, for something completely different");
  }
}
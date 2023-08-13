package utils.tuples;

import org.testng.annotations.Test;
import util.tuples.Pair;
import util.tuples.Trio;
import util.tuples.Triplet;
import util.tuples.Twin;

import static org.testng.Assert.assertEquals;

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
}
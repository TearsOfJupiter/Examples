package functions.util.tuples;

public class TuplesDemo
{
  public static void main(String[] args)
  {
    final Pair<Integer, String> pair = new Pair<>(1, "1");
    assert pair.getA() == 1;
    assert "1".equals(pair.getB());

    final Twin<String> twin = new Twin<>("a", "b");
    assert "a".equals(twin.getA());
    assert "b".equals(twin.getB());

    final Trio<Integer, String, String> trio = Trio.from(pair, "uno");
    assert trio.getA() == 1;
    assert "1".equals(trio.getB());
    assert "uno".equals(trio.getC());

    final Triplet<String> triplet = Triplet.from(twin, "c");
    assert "a".equals(triplet.getA());
    assert "b".equals(triplet.getB());
    assert "c".equals(triplet.getC());
  }
}
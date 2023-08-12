package collections;

import util.tuples.Pair;
import monads.Try;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CollectionUtilsTest
{
  /* *******************************************************************************************************************
   *                                              TEST DEFAULT METHODS
   * ***************************************************************************************************************** */

  @SuppressWarnings("ConstantValue")
  @Test
  public void test_defaultList()
  {
    assertTrue(CollectionUtils.defaultList(null).isEmpty());
    assertTrue(CollectionUtils.defaultList(Collections.emptyList()).isEmpty());
    assertEquals(CollectionUtils.defaultList(List.of(1, 2, 3)), List.of(1, 2, 3));
  }

  @Test
  public void test_defaultCollection()
  {
    assertEquals(CollectionUtils.defaultCollection(null, ArrayList::new), Collections.emptyList());
    assertEquals(CollectionUtils.defaultCollection(Collections.emptyList(), ArrayList::new), Collections.emptyList());
    assertEquals(CollectionUtils.defaultCollection(List.of(1, 2, 3), ArrayList::new), List.of(1, 2, 3));
  }

  @Test
  public void test_defaultStream()
  {
    assertTrue(CollectionUtils.defaultStream(null).findAny().isEmpty());
    assertTrue(CollectionUtils.defaultStream(List.of(1, 2, 3)).findAny().isPresent());
  }

  /* *******************************************************************************************************************
   *                                              TEST MUTATIVE METHODS
   * ***************************************************************************************************************** */

  @Test
  public void test_add()
  {
    final List<String> strings = CollectionUtils.add(new ArrayList<>(), "one");
    assertEquals(strings.size(), 1);
    assertEquals(strings.get(0), "one");
  }

  /* *******************************************************************************************************************
   *                                           TEST TRANSFORMATIVE METHODS
   * ***************************************************************************************************************** */

  @Test
  public void test_reverseList()
  {
    assertTrue(CollectionUtils.reverseList(null).isEmpty());
    assertTrue(CollectionUtils.reverseList(Collections.emptyList()).isEmpty());
    assertEquals(CollectionUtils.reverseList(List.of(1, 2, 3)), List.of(3, 2, 1));
  }

  @Test
  public void test_mapToList()
  {
    assertTrue(Try.ofSupplier(() -> CollectionUtils.mapToList(null, null)).isFailure());
    assertTrue(CollectionUtils.<String, Integer>mapToList(null, Integer::parseInt).isEmpty());
    assertEquals(CollectionUtils.mapToList(List.of("1", "2", "3"), Integer::parseInt), List.of(1, 2, 3));
  }

  /* *******************************************************************************************************************
   *                                           TEST COMBINATORICS METHODS
   * ***************************************************************************************************************** */

  @Test
  public void test_cartesianProduct()
  {
    assertTrue(CollectionUtils.cartesianProduct(null, null).isEmpty());
    assertTrue(CollectionUtils.cartesianProduct(Collections.emptyList(), null).isEmpty());
    assertTrue(CollectionUtils.cartesianProduct(null, Collections.emptyList()).isEmpty());

    final List<Integer> numbers = List.of(1, 2, 3);
    assertTrue(CollectionUtils.cartesianProduct(numbers, null).isEmpty());

    final List<String> letters = List.of("a", "b", "c");
    assertTrue(CollectionUtils.cartesianProduct(null, letters).isEmpty());

    assertEquals(
        CollectionUtils.cartesianProduct(numbers, letters),
        List.of(
            new Pair<>(1, "a"),
            new Pair<>(1, "b"),
            new Pair<>(1, "c"),
            new Pair<>(2, "a"),
            new Pair<>(2, "b"),
            new Pair<>(2, "c"),
            new Pair<>(3, "a"),
            new Pair<>(3, "b"),
            new Pair<>(3, "c")
        )
    );
  }
}
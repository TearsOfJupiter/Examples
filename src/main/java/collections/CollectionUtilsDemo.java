package collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionUtilsDemo
{
  public static void main(String[] args)
  {
    /* ****************** defaultCollection ***************** */

    // Demonstrates defaultCollection(null)
    System.out.println(CollectionUtils.defaultCollection(null, ArrayList::new) + " == []");
    assert CollectionUtils.defaultCollection(null, ArrayList::new).equals(Collections.emptyList());

    // Demonstrates defaultCollection(Collections.emptyList())
    System.out.println(CollectionUtils.defaultCollection(Collections.emptyList(), ArrayList::new) + " == []");
    assert CollectionUtils.defaultCollection(Collections.emptyList(), ArrayList::new).equals(Collections.emptyList());

    // Demonstrates defaultCollection with non-empty collection
    System.out.println(CollectionUtils.defaultCollection(List.of(1, 2, 3), ArrayList::new) + " == [1, 2, 3]");
    assert CollectionUtils.defaultCollection(List.of(1, 2, 3), ArrayList::new).equals(List.of(1, 2, 3));

    /* ****************** addPassThru ***************** */
    final List<String> strings = CollectionUtils.addPassThru(new ArrayList<>(), "one");
    assert strings.size() == 1;
    assert "one".equals(strings.get(0));

    /* ****************** reverseList ***************** */

    // Demonstrates reverseList(null)
    System.out.println(CollectionUtils.reverseList(null) + " == []");
    assert CollectionUtils.reverseList(null).equals(Collections.emptyList());

    // Demonstrates reverseList(Collections.emptyList())
    System.out.println(CollectionUtils.reverseList(Collections.emptyList()) + " == []");
    assert CollectionUtils.reverseList(Collections.emptyList()).equals(Collections.emptyList());

    // Demonstrates reverseList(List.of(1, 2, 3))
    System.out.println(CollectionUtils.reverseList(List.of(1, 2, 3)) + " == [3, 2, 1]");
    assert CollectionUtils.reverseList(List.of(1, 2, 3)).equals(List.of(3, 2, 1));
  }
}
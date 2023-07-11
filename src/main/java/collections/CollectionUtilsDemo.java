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
    // Demonstrates defaultCollection(Collections.emptyList())
    System.out.println(CollectionUtils.defaultCollection(Collections.emptyList(), ArrayList::new) + " == []");
    // Demonstrates defaultCollection with non-empty collection
    System.out.println(CollectionUtils.defaultCollection(List.of(1, 2, 3), ArrayList::new) + " == [1, 2, 3]");

    /* ****************** reverseList ***************** */
    // Demonstrates reverseList(null)
    System.out.println(CollectionUtils.reverseList(null) + " == []");
    // Demonstrates reverseList(Collections.emptyList())
    System.out.println(CollectionUtils.reverseList(Collections.emptyList()) + " == []");
    // Demonstrates reverseList(List.of(1, 2, 3))
    System.out.println(CollectionUtils.reverseList(List.of(1, 2, 3)) + " == [3, 2, 1]");
  }
}

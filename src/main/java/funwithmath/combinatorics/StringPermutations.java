package funwithmath.combinatorics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringPermutations
{
  public static void main(String[] args)
  {
    System.out.println(getPermutations(null) + " == []");
    assert getPermutations(null).equals(Collections.emptyList());

    System.out.println(getPermutations("") + " == []");
    assert getPermutations("").equals(Collections.emptyList());

    System.out.println(getPermutations("a") + " == [a]");
    assert getPermutations("a").equals(Collections.singletonList("a"));

    System.out.println(getPermutations("aa") + " == [aa]");
    assert getPermutations("aa").equals(Collections.singletonList("aa"));

    System.out.println(getPermutations("ab") + " ==\n[ab, ba]");
    assert getPermutations("ab").equals(List.of("ab", "ba"));

    System.out.println(getPermutations("aab") + " ==\n[aab, aba, baa]");
    assert getPermutations("aab").equals(List.of("aab", "aba", "baa"));

    System.out.println(getPermutations("abc") + " ==\n[abc, acb, bac, bca, cab, cba]");
    assert getPermutations("abc").equals(List.of("abc", "acb", "bac", "bca", "cab", "cba"));
  }

  public static List<String> getPermutations(String s)
  {
    return getPermutationsRecursive(s).stream()
        .distinct()
        .collect(Collectors.toList());
  }

  public static List<String> getPermutationsRecursive(String s)
  {
    if (s == null || s.length() == 0)
      return Collections.emptyList();
    else if (s.length() == 1)
      return Collections.singletonList(s);
    else if (s.length() == 2)
      return List.of(s, new StringBuilder(s).reverse().toString());

    final List<String> perms = new ArrayList<>();
    for (int i = 0; i < s.length(); ++i)
    {
      final int iIndex = i;
      final String remainder = IntStream.range(0, s.length())
          .filter(index -> index != iIndex)
          .mapToObj(s::charAt)
          .map(String::valueOf)
          .collect(Collectors.joining());
      getPermutationsRecursive(remainder).forEach(perm -> perms.add(s.charAt(iIndex) + perm));
    }

    return perms;
  }
}

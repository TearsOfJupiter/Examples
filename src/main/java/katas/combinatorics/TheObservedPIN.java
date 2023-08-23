package katas.combinatorics;

import java.util.*;

/**
 * Solution to <a href="https://www.codewars.com/kata/5263c6999e0f40dee200059d">The observed PIN</a>
 * Description:
 * Alright, detective, one of our colleagues successfully observed our target person, Robby the robber. We followed him to a secret warehouse, where we assume to find all the stolen stuff. The door to this warehouse is secured by an electronic combination lock. Unfortunately our spy isn't sure about the PIN he saw, when Robby entered it.
 * The keypad has the following layout:
 * ┌───┬───┬───┐
 * │ 1 │ 2 │ 3 │
 * ├───┼───┼───┤
 * │ 4 │ 5 │ 6 │
 * ├───┼───┼───┤
 * │ 7 │ 8 │ 9 │
 * └───┼───┼───┘
 *     │ 0 │
 *     └───┘
 * He noted the PIN 1357, but he also said, it is possible that each of the digits he saw could actually be another adjacent digit (horizontally or vertically, but not diagonally). E.g. instead of the 1 it could also be the 2 or 4. And instead of the 5 it could also be the 2, 4, 6 or 8.
 */
public class TheObservedPIN
{
  private static final Map<String, List<String>> relations = Map.of(
      "1", new ArrayList<>(List.of("1", "2", "4")),
      "2", new ArrayList<>(List.of("1", "2", "3", "5")),
      "3", new ArrayList<>(List.of("2", "3", "6")),
      "4", new ArrayList<>(List.of("1", "4", "5", "7")),
      "5", new ArrayList<>(List.of("2", "4", "5", "6", "8")),
      "6", new ArrayList<>(List.of("3", "5", "6", "9")),
      "7", new ArrayList<>(List.of("4", "7", "8")),
      "8", new ArrayList<>(List.of("5", "7", "8", "9", "0")),
      "9", new ArrayList<>(List.of("6", "8", "9")),
      "0", new ArrayList<>(List.of("8", "0"))
  );

  public static void main(String[] args)
  {
    assert getPINs("")
        .equals(Collections.emptyList());
    assert getPINs("1")
        .equals(List.of("1", "2", "4"));
    assert getPINs("11")
        .equals(List.of("11", "12", "14",
                       "21", "22", "24",
                       "41", "42", "44"));
    assert getPINs("12")
        .equals(List.of("11", "12", "13", "15",
                       "21", "22", "23", "25",
                       "41", "42", "43", "45"));
    assert getPINs("123")
        .equals(List.of("112", "113", "116", "122", "123", "126", "132", "133", "136", "152", "153", "156",
                        "212", "213", "216", "222", "223", "226", "232", "233", "236", "252", "253", "256",
                        "412", "413", "416", "422", "423", "426", "432", "433", "436", "452", "453", "456"));
  }

  public static List<String> getPINs(String observed)
  {
    if (observed == null || observed.isEmpty())
      return Collections.emptyList();

    final List<String> digits = Arrays.stream(observed.split(""))
        .toList();

    final List<List<String>> mappings = digits.stream()
        .map(relations::get)
        .toList();
    return getPINs(mappings);
  }

  private static List<String> getPINs(List<List<String>> lists)
  {
    final List<String> strings = new ArrayList<>();

    if (lists.size() == 1)
    {
      return lists.get(0);
    }
    else if (lists.size() == 2)
    {
      for (int i = 0; i < lists.get(0).size(); ++i)
      {
        for (int j = 0; j < lists.get(1).size(); ++j)
        {
          strings.add(lists.get(0).get(i) + lists.get(1).get(j));
        }
      }
    }
    else
    {
      final List<String> subLists = getPINs(lists.subList(1, lists.size()));
      for (int i = 0; i < lists.get(0).size(); ++i)
      {
        for (String subString : subLists)
        {
          strings.add(lists.get(0).get(i) + subString);
        }
      }
    }

    return strings;
  }
}
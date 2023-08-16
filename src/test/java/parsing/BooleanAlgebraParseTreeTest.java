package parsing;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class BooleanAlgebraParseTreeTest
{
  @Test
  public void test_parse()
  {
    testRelation("1 and 2");
    testRelation("1 or 2");
    testRelation("(1 or 2) and 3");
    testRelation("(1 or 2) and (3 or 4)");
    testRelation("1 and (2 or 3)");
    testRelation("1 and (2 or 3) and 4");
    testRelation("((1 or 2) and 3) or (4 and 5)");
    testRelation("(((1 or 2) and 3) or (4 and 5)) and 6");
  }

  private void testRelation(final String relation)
  {
    final BooleanAlgebraParseTree tree = BooleanAlgebraParseTree.parse(relation)
        .orElseThrow();
    assertEquals(tree.toRelationString(), relation);
  }
}
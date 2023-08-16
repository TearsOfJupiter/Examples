package parsing;

import org.apache.commons.lang3.StringUtils;
import util.StringUtil;

import java.util.*;

public class BooleanAlgebraParseTree
{
  private final BooleanAlgebraParseTree left;
  private final BooleanAlgebraParseTree right;
  private final BooleanAlgebraOperator operator;
  private final String nodeValue;

  private BooleanAlgebraParseTree(final String nodeValue)
  {
    this(null, null, null, nodeValue);
  }
  private BooleanAlgebraParseTree(final BooleanAlgebraParseTree left,
                                  final BooleanAlgebraOperator operator,
                                  final BooleanAlgebraParseTree right)
  {
    this(left, operator, right, null);
  }
  private BooleanAlgebraParseTree(final BooleanAlgebraParseTree left,
                                  final BooleanAlgebraOperator operator,
                                  final BooleanAlgebraParseTree right,
                                  final String nodeValue)
  {
    this.left = left;
    this.right = right;
    this.operator = operator;
    this.nodeValue = nodeValue;
  }

  public static Optional<BooleanAlgebraParseTree> parse(final String criteriaRelation)
  {
    if (StringUtils.isBlank(criteriaRelation))
      return Optional.empty();
    else if (StringUtils.isNumeric(criteriaRelation))
      return Optional.of(new BooleanAlgebraParseTree(criteriaRelation));

    final String relation = StringUtils.trim(criteriaRelation);
    final BooleanAlgebraParseTree left;
    final BooleanAlgebraOperator operator;
    final BooleanAlgebraParseTree right;

    final Queue<String> piecesQueue = new ArrayDeque<>(Arrays.asList(relation.split("\\s")));
    if (StringUtils.startsWith(piecesQueue.element(), "("))
    {
      left = getRelationBetweenParentheses(piecesQueue);
      if (piecesQueue.isEmpty())
        return Optional.of(left);
    }
    else
    {
      final String piece = piecesQueue.poll();
      left = BooleanAlgebraParseTree.parse(piece).orElseThrow();
    }

    operator = BooleanAlgebraOperator.from(piecesQueue.poll());
    final String remainder = String.join(" ", piecesQueue);
    right = BooleanAlgebraParseTree.parse(remainder).orElseThrow();

    return Optional.of(new BooleanAlgebraParseTree(left, operator, right));
  }

  private static BooleanAlgebraParseTree getRelationBetweenParentheses(final Queue<String> piecesQueue)
  {
    int openParen = 0;
    final List<String> pieces = new ArrayList<>();
    do
    {
      final String piece = piecesQueue.poll();
      if (StringUtils.startsWith(piece, "("))
        openParen += StringUtils.countMatches(piece, "(");
      else if (StringUtils.endsWith(piece, ")"))
        openParen -= StringUtils.countMatches(piece, ")");

      pieces.add(piece);
    }
    while (openParen > 0);

    final String leftRelation = String.join(" ", pieces);
    return BooleanAlgebraParseTree.parse(StringUtils.substring(leftRelation, 1, leftRelation.length() - 1))
        .orElseThrow();
  }

  public String toRelationString()
  {
    return toRelationString(operator);
  }
  public String toRelationString(final BooleanAlgebraOperator operator)
  {
    if (StringUtils.isNotBlank(nodeValue))
      return nodeValue;
    else
      return
          (left.operator != null && !Objects.equals(operator, left.operator) ? StringUtil.parenthesize(left.toRelationString(left.operator)) : left.toRelationString(left.operator)) +
          operator.getValueWithSpaces() +
          (right.operator != null && !Objects.equals(operator, right.operator) ? StringUtil.parenthesize(right.toRelationString(right.operator)) : right.toRelationString(right.operator));
  }

  @Override
  public String toString()
  {
    return toRelationString();
  }
}
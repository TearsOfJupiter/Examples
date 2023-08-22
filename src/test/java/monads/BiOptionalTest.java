package monads;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;
import util.Pojo;
import util.Visitor;
import util.tuples.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.testng.Assert.*;

public class BiOptionalTest
{
  private static final String S = "1";
  private static final int I = 1;
  private static final String KABLOOEY = "Kablooey!";
  private static final Supplier<Throwable> KABLOOEY_SUPPLIER = () -> new IllegalStateException(KABLOOEY);

  @Test
  public void test_empty()
  {
    assertTrue(BiOptional.empty().isEmpty());
  }

  @Test
  public void test_of()
  {
    final BiOptional<String, String> biop = BiOptional.of(S, S);
    assertEquals(biop.orElseThrowIfA(), S);
    assertEquals(biop.orElseThrowIfB(), S);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void test_of_AIsNull()
  {
    BiOptional.of(null, S);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void test_of_BIsNull()
  {
    BiOptional.of(S, null);
  }

  @Test
  public void testOfNullable()
  {
    assertTrue(BiOptional.ofNullable(null, null).isEmpty());

    assertTrue(BiOptional.ofNullable(null, I).isAEmpty());
    assertTrue(BiOptional.ofNullable(S, null).isBEmpty());

    final BiOptional<String, Integer> biop = BiOptional.ofNullable(S, I);
    assertTrue(biop.isPresent());
    final Pair<String, Integer> pair = biop.get();
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), I);
  }

  @Test(expectedExceptions = NoSuchElementException.class)
  public void testGet_AEmpty()
  {
    BiOptional.ofNullable(null, I).get();
  }

  @Test(expectedExceptions = NoSuchElementException.class)
  public void testGet_BEmpty()
  {
    BiOptional.ofNullable(S, null).get();
  }

  @Test
  public void testIfPresent()
  {
    final Pojo pojo = new Pojo();
    final Set<String> strings = new HashSet<>();
    final Set<Integer> ints = new HashSet<>();

    final BiOptional<String, Integer> biop = BiOptional.ofNullable(S, I)
        .ifPresent((a, b) ->
            Visitor.of(pojo)
                .visit(Pojo::setStringProp, a)
                .visit(Pojo::setIntProp, b)
                .get())
        .ifAPresent(strings::add)
        .ifBPresent(ints::add);

    assertTrue(biop.isPresent());
    assertEquals(pojo.getStringProp(), S);
    assertEquals(pojo.getIntProp(), I);
    assertTrue(strings.contains(S));
    assertTrue(ints.contains(I));
  }

  @Test
  public void testIfPresent_empty()
  {
    final Pojo pojo = new Pojo();
    final Set<String> strings = new HashSet<>();
    final Set<Integer> ints = new HashSet<>();

    final BiOptional<String, Integer> biop = BiOptional.<String, Integer>empty()
        .ifPresent((s, i) ->
            Visitor.of(pojo)
                .visit(Pojo::setStringProp, s)
                .visit(Pojo::setIntProp, i)
                .get())
        .ifAPresent(strings::add)
        .ifBPresent(ints::add);

    assertTrue(biop.isEmpty());
    assertNull(pojo.getStringProp());
    assertEquals(pojo.getIntProp(), 0);
    assertTrue(strings.isEmpty());
    assertTrue(ints.isEmpty());
  }

  @Test
  public void testFilter()
  {
    assertFalse(BiOptional.empty().filter((a, b) -> Objects.nonNull(a) && Objects.nonNull(b)).isPresent());

    final Pair<String, Integer> pair = BiOptional.of(S, I)
        .filter((a, b) -> StringUtils.equals(a, S) && b == I)
        .orElseThrow();
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), I);
  }

  @Test
  public void testFilter_AFails()
  {
    assertTrue(BiOptional.of(S, I)
        .filter((a, b) -> StringUtils.equals(a, "2") && b == I)
        .isEmpty());
  }

  @Test
  public void testFilter_BFails()
  {
    assertTrue(BiOptional.of(S, I)
        .filter((a, b) -> StringUtils.equals(a, S) && b == 2)
        .isEmpty());
  }

  @Test
  public void testFilterAB()
  {
    assertFalse(BiOptional.empty().filterA(Objects::nonNull).isPresent());
    assertFalse(BiOptional.empty().filterB(Objects::nonNull).isPresent());

    final BiOptional<String, Integer> biop = BiOptional.of(S, I);
    assertTrue(biop
        .filterA(a -> StringUtils.equals(a, S))
        .isPresent());
    assertFalse(biop
        .filterA(StringUtils::isBlank)
        .isPresent());

    assertTrue(biop
        .filterB(b -> b == I)
        .isPresent());
    assertFalse(biop
        .filterB(b -> b == 0)
        .isPresent());
  }

  @Test
  public void testMap()
  {
    final Pair<Integer, String> inversePair = BiOptional.ofNullable(S, I)
        .map(Integer::parseInt, String::valueOf)
        .orElseThrow();
    assertEquals(inversePair.getA(), I);
    assertEquals(inversePair.getB(), S);
  }

  @Test
  public void testMap_empty()
  {
    assertTrue(BiOptional.<String, Integer>empty()
        .map(Integer::parseInt, String::valueOf)
        .isEmpty());
  }

  @Test
  public void testMap_AMapperReturnsNull()
  {
    assertTrue(BiOptional.ofNullable(S, I)
        .map(a -> null, String::valueOf)
        .isEmpty());
  }

  @Test
  public void testMap_BMapperReturnsNull()
  {
    assertTrue(BiOptional.ofNullable(S, I)
        .map(Integer::parseInt, b -> null)
        .isEmpty());
  }

  @Test
  public void testMapA()
  {
    assertTrue(BiOptional.<Integer, Integer>empty()
        .mapA(String::valueOf)
        .isEmpty());

    final Pair<String, String> pair = BiOptional.ofNullable(I, S)
        .mapA(String::valueOf)
        .orElseThrow();
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), S);
  }

  @Test
  public void testMapB()
  {
    assertTrue(BiOptional.<String, String>empty()
        .mapB(Integer::parseInt)
        .isEmpty());

    final Pair<String, String> pair = BiOptional.ofNullable(S, I)
        .mapB(String::valueOf)
        .orElseThrow();
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), S);
  }

  @Test
  public void testMapToOptional()
  {
    final int result = BiOptional.ofNullable(S, "2")
        .mapToOptional((a, b) -> a + b)
        .map(Integer::parseInt)
        .orElseThrow();
    assertEquals(result, 12);
  }

  @Test
  public void testMapToOptional_empty()
  {
    assertTrue(BiOptional.<String, String>empty()
        .mapToOptional((a, b) -> a + b)
        .isEmpty());
  }

  @Test
  public void testMapToOptional_optionalIsEmpty()
  {
    assertTrue(BiOptional.empty()
        .mapToOptional((a, b) -> Optional.empty())
        .isEmpty());
  }

  @Test
  public void testFlatMap()
  {
    final Pair<String, Integer> pair = BiOptional.ofNullable(I, S)
        .flatMap((a, b) -> BiOptional.ofNullable(a, b)
            .map(String::valueOf, Integer::parseInt))
        .orElseThrow();
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), I);
  }

  @Test
  public void testFlatMap_initiallyEmpty()
  {
    assertTrue(BiOptional.empty()
        .flatMap(BiOptional::of)
        .isEmpty());
  }

  @Test
  public void testFlatMap_flatMapReturnsEmpty()
  {
    assertTrue(BiOptional.ofNullable(I, S)
        .flatMap((a, b) -> BiOptional.empty())
        .isEmpty());
  }

  @Test
  public void testFlatMapA()
  {
    final Pair<String, String> pair = BiOptional.ofNullable(I, S)
        .flatMapA(a -> Optional.ofNullable(a)
            .map(String::valueOf))
        .orElseThrow();
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), S);
  }

  @Test
  public void testFlatMapA_initiallyEmpty()
  {
    assertTrue(BiOptional.<Integer, Integer>empty()
        .flatMapA(a -> Optional.ofNullable(a)
        .map(String::valueOf)).isEmpty());
  }

  @Test
  public void testFlatMapA_flatMapAReturnsEmpty()
  {
    assertTrue(BiOptional.ofNullable(I, I)
        .flatMapA(a -> Optional.empty())
        .isEmpty());
  }

  @Test
  public void testFlatMapB()
  {
    final Pair<Integer, Integer> pair = BiOptional.ofNullable(I, S)
        .flatMapB(b -> Optional.ofNullable(b)
            .map(Integer::parseInt))
        .orElseThrow();
    assertEquals(pair.getA(), I);
    assertEquals(pair.getB(), I);
  }

  @Test
  public void testFlatMapB_initiallyEmpty()
  {
    assertTrue(BiOptional.<Integer, Integer>empty()
        .flatMapB(b -> Optional.ofNullable(b)
        .map(String::valueOf)).isEmpty());
  }

  @Test
  public void testFlatMapB_flatMapBReturnsEmpty()
  {
    assertTrue(BiOptional.ofNullable(S, S)
        .flatMapB(b -> Optional.empty())
        .isEmpty());
  }

  @Test
  public void testOr()
  {
    final Pair<String, Integer> pair = BiOptional.ofNullable(S, I)
        .or(() -> BiOptional.of("2", 2))
        .orElseThrow();
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), I);
  }

  @Test
  public void testOr_empty_orIsNotEmpty()
  {
    final Pair<String, Integer> pair = BiOptional.<String, Integer>ofNullable(null, null)
        .or(() -> BiOptional.ofNullable(S, I))
        .orElseThrow();
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), I);
  }

  @Test
  public void testOr_bothEmpty()
  {
    assertTrue(BiOptional.empty()
        .or(BiOptional::empty)
        .isEmpty());
  }

  @Test
  public void testStream()
  {
    final Pair<Integer, Integer> pair = Stream.concat(
            Stream.of(new Pair<>(1, 2)),
            BiOptional.ofNullable(3, 4).stream())
        .reduce((p1, p2) -> new Pair<>(p1.getA() + p2.getA(), p1.getB() + p2.getB()))
        .orElseThrow();
    assertEquals(pair.getA(), 4);
    assertEquals(pair.getB(), 6);
  }

  @Test
  public void testStream_empty()
  {
    assertTrue(BiOptional.empty()
        .stream()
        .findFirst()
        .isEmpty());
  }

  @Test
  public void testStreamA()
  {
    assertEquals(BiOptional.ofNullable(S, I)
        .streamA()
        .findFirst()
        .orElseThrow(), S);
  }

  @Test
  public void testStreamA_empty()
  {
    assertTrue(BiOptional.ofNullable(null, I)
        .streamA()
        .findFirst()
        .isEmpty());
  }

  @Test
  public void testStreamB()
  {
    assertEquals(BiOptional.ofNullable(S, I)
        .streamB()
        .findFirst()
        .orElseThrow(), I);
  }

  @Test
  public void testStreamB_empty()
  {
    assertTrue(BiOptional.ofNullable(S, null)
        .streamB()
        .findFirst()
        .isEmpty());
  }

  @Test
  public void testOrElse()
  {
    final Pair<String, Integer> pair = BiOptional.ofNullable(S, I)
        .orElse(new Pair<>("2", 2));
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), I);
  }

  @Test
  public void testOrElse_empty()
  {
    final Pair<String, Integer> pair = BiOptional.<String, Integer>empty()
        .orElse(new Pair<>(S, I));
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), I);
  }

  @Test
  public void testOrElse_elseIsNull()
  {
    assertNull(BiOptional.empty()
        .orElse(null));
  }

  @Test
  public void testOrElseGet()
  {
    final Pair<String, Integer> pair = BiOptional.ofNullable(S, I)
        .orElseGet(() -> new Pair<>("2", 2));
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), I);
  }

  @Test
  public void testOrElseGet_empty()
  {
    final Pair<String, Integer> pair = BiOptional.<String, Integer>empty()
        .orElseGet(() -> new Pair<>(S, I));
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), I);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testOrElseGet_empty_nullSupplier()
  {
    BiOptional.empty()
        .orElseGet(null);
  }

  @Test
  public void testOrElseThrow()
  {
    final Pair<String, Integer> pair = BiOptional.ofNullable(S, I)
        .orElseThrow();
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), I);
  }

  @Test(expectedExceptions = NoSuchElementException.class)
  public void testOrElseThrow_empty()
  {
    BiOptional.empty()
        .orElseThrow();
  }

  @Test
  public void testOrElseThrow_withSupplier()
  {
    final Pair<String, Integer> pair = BiOptional.ofNullable(S, I)
        .orElseThrow(KABLOOEY_SUPPLIER);
    assertEquals(pair.getA(), S);
    assertEquals(pair.getB(), I);
  }

  @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = KABLOOEY)
  public void testOrElseThrow_withSupplier_empty()
  {
    BiOptional.empty()
        .orElseThrow(KABLOOEY_SUPPLIER);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testOrElseThrow_withSupplier_nullSupplier()
  {
    BiOptional.empty()
        .orElseThrow(null);
  }

  @Test
  public void testOrElseA()
  {
    assertEquals(BiOptional.ofNullable(S, I)
        .orElseA("2"), S);
  }

  @Test
  public void testOrElseA_empty()
  {
    assertEquals(BiOptional.ofNullable(null, I)
        .orElseA("2"), "2");
  }

  @Test
  public void testOrElseA_empty_nullOtherA()
  {
    assertNull(BiOptional.ofNullable(null, I)
        .orElseA(null));
  }

  @Test
  public void testOrElseGetA()
  {
    assertEquals(BiOptional.ofNullable(S, I)
        .orElseGetA(() -> "2"), S);
  }

  @Test
  public void testOrElseGetA_empty()
  {
    assertEquals(BiOptional.ofNullable(null, I)
        .orElseGetA(() -> "2"), "2");
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testOrElseGetA_empty_nullSupplier()
  {
    BiOptional.ofNullable(null, I)
        .orElseGetA(null);
  }

  @Test
  public void testOrElseThrowIfA()
  {
    assertEquals(BiOptional.ofNullable(S, I)
        .orElseThrowIfA(), S);
  }

  @Test(expectedExceptions = NoSuchElementException.class)
  public void testOrElseThrowIfA_empty()
  {
    BiOptional.ofNullable(null, I)
        .orElseThrowIfA();
  }

  @Test
  public void testOrElseThrowIfA_withSupplier()
  {
    assertEquals(BiOptional.ofNullable(S, I)
        .orElseThrowIfA(KABLOOEY_SUPPLIER), S);
  }

  @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = KABLOOEY)
  public void testOrElseThrowIfA_withSupplier_empty()
  {
    BiOptional.ofNullable(null, I)
        .orElseThrowIfA(KABLOOEY_SUPPLIER);
  }

  @Test
  public void testOrElseB()
  {
    assertEquals(BiOptional.ofNullable(S, I)
        .orElseB(2), I);
  }

  @Test
  public void testOrElseB_empty()
  {
    assertEquals(BiOptional.ofNullable(S, null)
        .orElseB(2), 2);
  }

  @Test
  public void testOrElseB_nullOtherB()
  {
    assertNull(BiOptional.ofNullable(S, null)
        .orElseB(null));
  }

  @Test
  public void testOrElseGetB()
  {
    assertEquals(BiOptional.ofNullable(S, I)
        .orElseGetB(() -> 2), I);
  }

  @Test
  public void testOrElseGetB_empty()
  {
    assertEquals(BiOptional.ofNullable(S, null)
        .orElseGetB(() -> 2), 2);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testOrElseGetB_nullSupplier()
  {
    BiOptional.ofNullable(S, null)
        .orElseGetB(null);
  }

  @Test
  public void testOrElseThrowIfB()
  {
    assertEquals(BiOptional.ofNullable(S, I).orElseThrowIfB(), I);
  }

  @Test(expectedExceptions = NoSuchElementException.class)
  public void testOrElseThrowIfB_empty()
  {
    BiOptional.ofNullable(S, null)
        .orElseThrowIfB();
  }

  @Test
  public void testOrElseThrowIfB_withSupplier()
  {
    assertEquals(BiOptional.ofNullable(S, I)
        .orElseThrowIfB(KABLOOEY_SUPPLIER), I);
  }

  @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = KABLOOEY)
  public void testOrElseThrowIfB_withSupplier_empty()
  {
    BiOptional.ofNullable(S, null)
        .orElseThrowIfB(KABLOOEY_SUPPLIER);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testOrElseThrowIfB_nullSupplier()
  {
    BiOptional.ofNullable(S, null)
        .orElseThrowIfB(null);
  }

  @SuppressWarnings({"UnnecessaryLocalVariable", "AssertBetweenInconvertibleTypes"})
  @Test
  public void testEquals()
  {
    final BiOptional<String, Integer> biop1 = BiOptional.ofNullable(S, I);
    final BiOptional<String, Integer> biop2 = biop1;
    assertEquals(biop1, biop2);

    final BiOptional<String, Integer> biop3 = BiOptional.ofNullable(S, I);
    assertEquals(biop1, biop3);

    final BiOptional<String, Integer> biop4 = BiOptional.ofNullable("2", I);
    assertNotEquals(biop1, biop4);

    final BiOptional<String, Integer> biop5 = BiOptional.ofNullable(S, 2);
    assertNotEquals(biop1, biop5);

    assertNotEquals(biop1, "and now, for something completely different");
  }

  @Test
  public void testHashCode()
  {
    final int expectedHash = Objects.hash(S, I);
    assertEquals(BiOptional.ofNullable(S, I).hashCode(), expectedHash);
  }

  @Test
  public void testToString()
  {
    assertEquals(BiOptional.empty().toString(), "BiOptional.empty");
    assertEquals(BiOptional.ofNullable(S, I).toString(), "BiOptional[" + S + ", " + I + "]");
  }
}
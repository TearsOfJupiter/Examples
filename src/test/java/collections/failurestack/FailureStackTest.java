package collections.failurestack;

import monads.Try;
import org.testng.annotations.Test;
import util.Builder;
import util.Pojo;

import static org.testng.Assert.*;

public class FailureStackTest
{
  @SuppressWarnings("unchecked")
  @Test
  public void testFailureStack()
  {
    final Pojo pojo = Builder.of(Pojo::new)
        .with(Pojo::setStringProp, "one")
        .with(Pojo::setIntProp, 1)
        .with(Pojo::setDoubleProp, 1.0)
        .with(Pojo::setBoolProp, true)
        .build();

    final FailureStack fs = new FailureStack();
    fs.push(FailureStackFrame.of("hello", String.class, new Exception()));
    fs.push(FailureStackFrame.of(1, Integer.class, new IllegalArgumentException()));
    fs.push(FailureStackFrame.of(pojo, Pojo.class, new RuntimeException()));

    assertEquals(pojo, fs.popVal());

    final int intVal = fs.popVal();
    assertEquals(intVal, 1);

    final String stringVal = fs.popVal();
    assertEquals(stringVal, "hello");

    fs.push(FailureStackFrame.of(1, Integer.class, new RuntimeException("kablooey!")));
    final FailureStackFrame<Integer, Class<Integer>, RuntimeException> frame = fs.pop();
    final RuntimeException error = frame.getError();
    assertEquals(error.getMessage(), "kablooey!");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testFailureStackWithTry()
  {
    final FailureStack stack = new FailureStack();

    final Pojo pojo = Builder.of(Pojo::new)
        .with(Pojo::setStringProp, "hello")
        .build();

    final Try<Integer> t = Try.ofFunction(Pojo::kablooey, pojo)
        .ifFailure((e, p) -> stack.push(FailureStackFrame.of(p, Pojo.class, e)))
        .or(() -> Try.of(pojo))
        .map(Pojo::getStringProp)
        .map(Integer::parseInt)
        .ifFailure((e, i) -> stack.push(FailureStackFrame.of(i, Integer.class, e)))
        .or(() -> Try.of(1));
    assertTrue(t.isSuccessful());
    assertEquals(t.get(), 1);

    final FailureStackFrame<Integer, Class<Integer>, Exception> frame = stack.pop();
    assertNull(frame.getValue());
    assertEquals(frame.getValueClass(), Integer.class);
    assertTrue(frame.getError() instanceof NumberFormatException);

    assertEquals(stack.popVal(), pojo);

    assertNull(stack.popVal());
  }
}
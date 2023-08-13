package fluentbuilder.basic;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class BasicFluentBuilderTest
{
  @Test
  public void testBasicFluentBuilder()
  {
    final String name1 = "Harry";
    final Person person = Person.newPersonBuilder()
        .withName(name1)
        .build();
    assertEquals(person.getName(), name1);
    assertEquals(person.toString(), "Person{name='" + name1 + "'}");

    final String name2 = "John";
    final String company = "MyCompany, Ltd.";
    final Person employee = Person.newEmployeeBuilder()
        .withName(name2)
        .worksAt(company)
        .build();
    assertEquals(employee.getName(), name2);
    assertEquals(employee.getCompany(), company);
    assertEquals(employee.toString(), "Person{name='" + name2 + "', company='" + company + "'}");
  }
}
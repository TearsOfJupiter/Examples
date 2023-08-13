package fluentbuilder.advanced;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class AdvancedFluentBuilderTest
{
  @Test
  public void testAdvancedFluentBuilder()
  {
    final String name1 = "Harry";
    final Person person = Person.newPersonBuilder()
        .withName(name1)
        .build();
    assertEquals(person.getName(), name1);
    assertEquals(person.toString(), "Person{name='" + name1 + "'}");

    final String name2 = "John";
    final String company1 = "MyCompany, Ltd.";
    final Employee employee = Employee.newEmployeeBuilder()
        .withName(name2)
        .worksAt(company1)
        .build();
    assertEquals(employee.getName(), name2);
    assertEquals(employee.getCompany(), company1);
    assertEquals(employee.toString(), "Employee{name='" + name2 + "', company='" + company1 + "'}");

    final String name3 = "Tony";
    final String company2 = "Fluent, Inc.";
    final String language = "Java";
    final Developer developer = Developer.newDeveloperBuilder()
        .withName(name3)
        .worksAt(company2)
        .writes(language)
        .build();
    assertEquals(developer.getName(), name3);
    assertEquals(developer.getCompany(), company2);
    assertEquals(developer.getLanguage(), language);
    assertEquals(developer.toString(), "Developer{name='" + name3 + "', company='" + company2 + "', language='" + language + "'}");
  }
}
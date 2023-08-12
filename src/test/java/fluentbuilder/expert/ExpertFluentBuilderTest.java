package fluentbuilder.expert;

import collections.CollectionUtils;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class ExpertFluentBuilderTest
{
  @Test
  public void testExpertFluentBuilder()
  {
    String name1 = "John";
    final IPerson person = new Person<>()
        .withName(name1);
    assertEquals(person.getName(), name1);
    assertEquals(person.toString(), "Person{name='" + name1 + "'}");

    String name2 = "Paul";
    String company1 = "MyCompany, Ltd.";
    final IEmployee employee = new Employee<>()
        .withName(name2)
        .worksAt(company1);
    assertEquals(employee.getName(), name2);
    assertEquals(employee.getCompany(), company1);
    assertEquals(employee.toString(), "Employee{name='" + name2 + "',company='" + company1 + "'}");

    String name3 = "George";
    String company2 = "Fluent, Inc.";
    String language = "Java";
    final IDeveloper developer = new Developer<>()
        .withName(name3)
        .worksAt(company2)
        .writes(language);
    assertEquals(developer.getName(), name3);
    assertEquals(developer.getCompany(), company2);
    assertEquals(developer.getLanguage(), language);
    assertEquals(developer.toString(), "Developer{name='" + name3 + "',company='" + company2 + "',language='" + language + "'}");

    final List<IPerson> people = List.of(person, employee, developer);
    final List<String> names = CollectionUtils.mapToList(people, IPerson::getName);
    assertEquals(names, List.of(name1, name2, name3));

    final List<IEmployee> employees = List.of(employee, developer);
    final List<String> companies = CollectionUtils.mapToList(employees, IEmployee::getCompany);
    assertEquals(companies, List.of(company1, company2));

    final List<IDeveloper> developers = List.of(developer);
    final List<String> languages = CollectionUtils.mapToList(developers, IDeveloper::getLanguage);
    assertEquals(languages, List.of(language));
  }
}
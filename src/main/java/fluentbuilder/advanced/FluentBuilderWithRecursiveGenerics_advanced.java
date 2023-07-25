package fluentbuilder.advanced;

public class FluentBuilderWithRecursiveGenerics_advanced
{
  public static void main(String[] args)
  {
    final Person person = Person.newPersonBuilder()
        .withName("Harry")
        .build();
    System.out.println(person);
    assert "Harry".equals(person.getName());
    
    final Employee employee = Employee.newEmployeeBuilder()
        .withName("John")
        .worksAt("Fluent, Inc.")
        .build();
    System.out.println(employee);
    assert "John".equals(employee.getName());
    assert "Fluent, Inc.".equals(employee.getCompany());
    
    final Developer developer = Developer.newDeveloperBuilder()
        .withName("Tony")
        .worksAt("Fluent, Inc.")
        .writes("Java")
        .build();
    System.out.println(developer);
    assert "Tony".equals(developer.getName());
    assert "Fluent, Inc.".equals(developer.getCompany());
    assert "Java".equals(developer.getLanguage());
  }
}


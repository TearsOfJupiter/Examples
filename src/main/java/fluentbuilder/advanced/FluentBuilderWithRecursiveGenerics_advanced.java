package fluentbuilder.advanced;

public class FluentBuilderWithRecursiveGenerics_advanced
{
  public static void main(String[] args)
  {
    final Person person = Person.newPersonBuilder()
        .withName("Harry")
        .build();
    System.out.println(person);
    
    final Employee employee = Employee.newEmployeeBuilder()
        .withName("John")
        .worksAt("Fluent, Inc.")
        .build();
    System.out.println(employee);
    
    final Developer developer = Developer.newDeveloperBuilder()
        .withName("Tony")
        .worksAt("Fluent, Inc.")
        .writes("Java")
        .build();
    System.out.println(developer);
  }
}


package fluentbuilder.basic;

public class FluentBuilderWithRecursiveGenerics_basic
{
  public static void main(String[] args)
  {
    final Person person = Person.newPersonBuilder()
        .withName("John")
        .build();
    System.out.println(person);

    final Person employee = Person.newEmployeeBuilder()
        .withName("Tony")
        .worksAt("Fluent, Inc.")
        .build();
    System.out.println(employee);
  }
}


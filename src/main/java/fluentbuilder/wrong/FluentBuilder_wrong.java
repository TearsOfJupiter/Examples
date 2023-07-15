package fluentbuilder.wrong;

public class FluentBuilder_wrong
{
  public static void main(String[] args)
  {
    final Person person = Person.newPersonBuilder()
        .withName("John")
        .build();
    System.out.println(person);

    final Person employee = Person.newEmployeeBuilder()
        .withName("Tony")
        //.worksAt("Fluent, Inc.") //Doesn't work
        .build();
    System.out.println(employee);
  }
}


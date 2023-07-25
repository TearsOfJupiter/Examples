package fluentbuilder.semiadvanced;

class FluentBuilderWithRecursiveGenerics_semiAdvanced
{
  public static void main(String[] args)
  {
    final Person person = Person.newPersonBuilder()
        .withName("Harry")
        .build();
    System.out.println(person);
    assert "Harry".equals(person.getName());
    
    final Person employee = Person.newEmployeeBuilder()
        .withName("John")
        .worksAt("Fluent, Inc.")
        .build();
    System.out.println(employee);
    assert "John".equals(employee.getName());
    assert "Fluent, Inc.".equals(employee.getCompany());
    
    final Person developer = Person.newDeveloperBuilder()
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


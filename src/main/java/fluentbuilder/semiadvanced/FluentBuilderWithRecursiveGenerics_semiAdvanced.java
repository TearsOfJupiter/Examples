package fluentbuilder.semiadvanced;

class FluentBuilderWithRecursiveGenerics_semiAdvanced
{
  public static void main(String[] args)
  {
    final Person person = Person.newPersonBuilder()
        .withName("Harry")
        .build();
    System.out.println(person);
    
    final Person employee = Person.newEmployeeBuilder()
        .withName("John")
        .worksAt("Fluent, Inc.")
        .build();
    System.out.println(employee);
    
    final Person developer = Person.newDeveloperBuilder()
        .withName("Tony")
        .worksAt("Fluent, Inc.")
        .writes("Java")
        .build();
    System.out.println(developer);
  }
}

class Person
{
  String name;
  String company;
  String language;
  
  Person() {}


  public String getName()
  {
    return name;
  }
  void setName(String name)
  {
    this.name = name;
  }
  
  public String getCompany()
  {
    return company;
  }
  void setCompany(String company)
  {
    this.company = company;
  }

  public String getLanguage()
  {
    return language;
  }
  void setLanguage(String language)
  {
    this.language = language;
  }
  
  public static class Builder extends PersonBuilder<Builder> {}
  public static Builder newPersonBuilder()
  {
    return new Builder();
  }
  
  public static class EmpBuilder extends EmployeeBuilder<EmpBuilder> {}
  public static EmpBuilder newEmployeeBuilder()
  {
    return new EmpBuilder();
  }
  
  public static class DevBuilder extends DeveloperBuilder<DevBuilder> {}
  public static DevBuilder newDeveloperBuilder()
  {
    return new DevBuilder();
  }

  @Override
  public String toString()
  {
    return "Person{" +
        "name='" + name + '\'' +
        ", company='" + company + '\'' +
        ", language='" + language + '\'' +
        '}';
  }
}

class PersonBuilder<SELF extends PersonBuilder<SELF>>
{
  Person person;

  public PersonBuilder()
  {
    person = new Person();
  }

  SELF withName(String name)
  {
    person.setName(name);
    return self();
  }

  /** @noinspection unchecked*/
  SELF self()
  {
    return (SELF) this;
  }

  Person build()
  {
    return person;
  }
}

class EmployeeBuilder<SELF extends EmployeeBuilder<SELF>> extends PersonBuilder<EmployeeBuilder<SELF>>
{
  public EmployeeBuilder()
  {
    person = new Person();
  }

  SELF worksAt(String company)
  {
    person.setCompany(company);
    return self();
  }

  /** @noinspection unchecked*/
  @Override
  protected SELF self()
  {
    return (SELF) this;
  }
}

class DeveloperBuilder<SELF extends DeveloperBuilder<SELF>> extends EmployeeBuilder<DeveloperBuilder<SELF>>
{
  public DeveloperBuilder()
  {
    person = new Person();
  }

  SELF writes(String language)
  {
    person.setLanguage(language);
    return self();
  }

  /** @noinspection unchecked*/
  @Override
  protected SELF self()
  {
    return (SELF) this;
  }
}

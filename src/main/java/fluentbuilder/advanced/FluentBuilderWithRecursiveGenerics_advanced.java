package fluentbuilder.advanced;

class FluentBuilderWithRecursiveGenerics
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

class Person
{
  String name;
  
  Person() {}

  /** @noinspection unused*/
  public String getName()
  {
    return name;
  }
  void setName(String name)
  {
    this.name = name;
  }
  
  public static class Builder extends PersonBuilder<Builder>
  {}
  public static Builder newPersonBuilder()
  {
    return new Builder();
  }

  @Override
  public String toString()
  {
    return "Person{" +
        "name='" + name + '\'' +
        '}';
  }
}

class Employee extends Person
{
  String company;
  
  Employee() {}

  /** @noinspection unused*/
  public String getCompany()
  {
    return company;
  }
  void setCompany(String company)
  {
    this.company = company;
  }
  
  public static class EmpBuilder extends EmployeeBuilder<EmpBuilder>
  {}
  public static EmpBuilder newEmployeeBuilder()
  {
    return new EmpBuilder();
  }

  @Override
  public String toString()
  {
    return "Employee{" +
        "name='" + name + '\'' +
        ", company='" + company + '\'' +
        '}';
  }
}

class Developer extends Employee
{
  String language;
  
  Developer() {}
  
  /** @noinspection unused*/
  public String getLanguage()
  {
    return language;
  }
  void setLanguage(String language)
  {
    this.language = language;
  }
  
  public static class DevBuilder extends DeveloperBuilder<DevBuilder> {}
  public static DevBuilder newDeveloperBuilder()
  {
    return new DevBuilder();
  }

  @Override
  public String toString()
  {
    return "Developer{" +
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

class EmployeeBuilder<SELF extends EmployeeBuilder<SELF>> extends PersonBuilder<SELF>
{
  public EmployeeBuilder()
  {
    person = new Employee();
  }
  
  private Employee getEmployee()
  {
    return (Employee) person;
  }
  
  SELF worksAt(String company)
  {
    getEmployee().setCompany(company);
    return self();
  }
  
  @Override
  public Employee build()
  {
    return getEmployee();
  }
}

class DeveloperBuilder<SELF extends DeveloperBuilder<SELF>> extends EmployeeBuilder<DeveloperBuilder<SELF>>
{
  public DeveloperBuilder()
  {
    person = new Developer();
  }
  
  private Developer getDeveloper()
  {
    return (Developer) person;
  }
  
  DeveloperBuilder writes(String language)
  {
    getDeveloper().setLanguage(language);
    return self();
  }
  
  @Override
  public Developer build()
  {
    return getDeveloper();
  }
}

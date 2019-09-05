class FluentBuilderWithRecursiveGenerics
{
  public static void main(String[] args)
  {
    final Person person = new PersonBuilder()
        .withName("John")
        .build();
    System.out.println(person);

    final Person employee = new EmployeeBuilder()
        .withName("Tony")
        .worksAt("Fluent, Inc.")
        .build();
    System.out.println(employee);
  }
}

class Person
{
  String name;
  String company;

  /** @noinspection unused*/
  public String getName()
  {
    return name;
  }
  void setName(String name)
  {
    this.name = name;
  }

  /** @noinspection unused*/
  public String getCompany()
  {
    return company;
  }
  void setCompany(String company)
  {
    this.company = company;
  }

  @Override
  public String toString()
  {
    return "Person{" +
        "name='" + name + '\'' +
        ((company != null && !"".equals(company)) ? ", company='" + company + '\'' : "") +
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

class EmployeeBuilder extends PersonBuilder<EmployeeBuilder>
{
  public EmployeeBuilder()
  {
    person = new Person();
  }

  EmployeeBuilder worksAt(String company)
  {
    person.setCompany(company);
    return self();
  }

  @Override
  protected EmployeeBuilder self()
  {
    return this;
  }
}
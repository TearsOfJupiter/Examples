package fluentbuilder.basic;

@SuppressWarnings("unused")
public class Person
{
  private String name;
  private String company;

  public String getName()
  {
    return name;
  }
  private void setName(final String name)
  {
    this.name = name;
  }

  public String getCompany()
  {
    return company;
  }
  private void setCompany(final String company)
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

  public static <PB extends PersonBuilder<PB>> PersonBuilder<PB> newPersonBuilder()
  {
    return new PersonBuilder<>();
  }
  public static class PersonBuilder<PB extends PersonBuilder<PB>>
  {
    protected Person person;

    private PersonBuilder()
    {
      person = new Person();
    }

    public PB withName(final String name)
    {
      person.setName(name);
      return self();
    }

    @SuppressWarnings("unchecked")
    protected PB self()
    {
      return (PB) this;
    }

    public Person build()
    {
      return person;
    }
  }

  public static EmployeeBuilder newEmployeeBuilder()
  {
    return new EmployeeBuilder();
  }
  public static class EmployeeBuilder extends PersonBuilder<EmployeeBuilder>
  {
    private EmployeeBuilder()
    {
      person = new Person();
    }

    public EmployeeBuilder worksAt(final String company)
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
}

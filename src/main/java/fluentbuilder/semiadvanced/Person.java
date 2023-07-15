package fluentbuilder.semiadvanced;

@SuppressWarnings("unused")
public class Person
{
  private String name;
  private String company;
  private String language;

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

  public String getLanguage()
  {
    return language;
  }
  private void setLanguage(final String language)
  {
    this.language = language;
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

  public static <PB extends PersonBuilder<PB>> PersonBuilder<PB> newPersonBuilder()
  {
    return new PersonBuilder<>();
  }
  public static class PersonBuilder<SELF extends PersonBuilder<SELF>>
  {
    protected Person person;

    private PersonBuilder()
    {
      person = new Person();
    }

    public SELF withName(final String name)
    {
      person.setName(name);
      return self();
    }

    @SuppressWarnings("unchecked")
    protected SELF self()
    {
      return (SELF) this;
    }

    public Person build()
    {
      return person;
    }
  }

  public static <EB extends EmployeeBuilder<EB>> EmployeeBuilder<EB> newEmployeeBuilder()
  {
    return new EmployeeBuilder<>();
  }
  public static class EmployeeBuilder<SELF extends EmployeeBuilder<SELF>> extends PersonBuilder<EmployeeBuilder<SELF>>
  {
    private EmployeeBuilder()
    {
      person = new Person();
    }

    public SELF worksAt(final String company)
    {
      person.setCompany(company);
      return self();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected SELF self()
    {
      return (SELF) this;
    }
  }

  public static <DB extends DeveloperBuilder<DB>> DeveloperBuilder<DB> newDeveloperBuilder()
  {
    return new DeveloperBuilder<>();
  }
  public static class DeveloperBuilder<SELF extends DeveloperBuilder<SELF>> extends EmployeeBuilder<DeveloperBuilder<SELF>>
  {
    private DeveloperBuilder()
    {
      person = new Person();
    }

    public SELF writes(final String language)
    {
      person.setLanguage(language);
      return self();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected SELF self()
    {
      return (SELF) this;
    }
  }
}

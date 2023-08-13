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
        ((company != null && !"".equals(company)) ? ", company='" + company + '\'' : "") +
        ((language != null && !"".equals(language)) ? ", language='" + language + '\'' : "") +
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

  public static <EB extends EmployeeBuilder<EB>> EmployeeBuilder<EB> newEmployeeBuilder()
  {
    return new EmployeeBuilder<>();
  }
  public static class EmployeeBuilder<EB extends EmployeeBuilder<EB>> extends PersonBuilder<EmployeeBuilder<EB>>
  {
    private EmployeeBuilder()
    {
      person = new Person();
    }

    public EB worksAt(final String company)
    {
      person.setCompany(company);
      return self();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected EB self()
    {
      return (EB) this;
    }
  }

  public static <DB extends DeveloperBuilder<DB>> DeveloperBuilder<DB> newDeveloperBuilder()
  {
    return new DeveloperBuilder<>();
  }
  public static class DeveloperBuilder<DB extends DeveloperBuilder<DB>> extends EmployeeBuilder<DeveloperBuilder<DB>>
  {
    private DeveloperBuilder()
    {
      person = new Person();
    }

    public DB writes(final String language)
    {
      person.setLanguage(language);
      return self();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected DB self()
    {
      return (DB) this;
    }
  }
}
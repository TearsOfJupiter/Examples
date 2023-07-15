package fluentbuilder.advanced;

@SuppressWarnings("unused")
class Developer extends Employee
{
  private String language;

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
    return "Developer{" +
        "name='" + name + '\'' +
        ", company='" + company + '\'' +
        ", language='" + language + '\'' +
        '}';
  }

  public static <DB extends DeveloperBuilder<DB>> DeveloperBuilder<DB> newDeveloperBuilder()
  {
    return new DeveloperBuilder<>();
  }
  public static class DeveloperBuilder<SELF extends DeveloperBuilder<SELF>> extends EmployeeBuilder<DeveloperBuilder<SELF>>
  {
    private DeveloperBuilder()
    {
      person = new Developer();
    }

    private Developer getDeveloper()
    {
      return (Developer) person;
    }

    public DeveloperBuilder<SELF> writes(final String language)
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
}

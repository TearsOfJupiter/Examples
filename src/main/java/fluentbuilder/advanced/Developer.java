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
  protected static class DeveloperBuilder<DB extends DeveloperBuilder<DB>> extends EmployeeBuilder<DeveloperBuilder<DB>>
  {
    private DeveloperBuilder()
    {
      person = new Developer();
    }

    private Developer getDeveloper()
    {
      return (Developer) person;
    }

    public DeveloperBuilder<DB> writes(final String language)
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

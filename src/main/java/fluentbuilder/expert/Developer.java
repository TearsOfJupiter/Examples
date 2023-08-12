package fluentbuilder.expert;

public class Developer<T extends Developer<T>> extends Employee<T> implements IDeveloper
{
  private String language;

  @Override
  public String getLanguage()
  {
    return language;
  }
  @SuppressWarnings("unchecked")
  public T writes(final String language)
  {
    this.language = language;
    return (T) this;
  }

  @Override
  public String toString()
  {
    return
        "Developer{" +
          "name='" + getName() + "'," +
          "company='" + getCompany() + "'," +
          "language='" + language + '\'' +
        "}";
  }
}
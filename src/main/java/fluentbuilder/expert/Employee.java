package fluentbuilder.expert;

public class Employee<T extends Employee<T>> extends Person<T> implements IEmployee
{
  private String company;


  @Override
  public String getCompany()
  {
    return company;
  }
  @SuppressWarnings("unchecked")
  public T worksAt(final String company)
  {
    this.company = company;
    return (T) this;
  }

  @Override
  public String toString()
  {
    return
        "Employee{" +
          "name='" + getName() + "'," +
          "company='" + company + '\'' +
        "}";
  }
}
package fluentbuilder.advanced;

@SuppressWarnings("unused")
public class Employee extends Person
{
  protected String company;

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
    return "Employee{" +
        "name='" + name + '\'' +
        ", company='" + company + '\'' +
        '}';
  }

  public static <EB extends EmployeeBuilder<EB>> EmployeeBuilder<EB> newEmployeeBuilder()
  {
    return new EmployeeBuilder<>();
  }
  protected static class EmployeeBuilder<SELF extends EmployeeBuilder<SELF>> extends PersonBuilder<SELF>
  {
    protected EmployeeBuilder()
    {
      person = new Employee();
    }

    private Employee getEmployee()
    {
      return (Employee) person;
    }

    public SELF worksAt(final String company)
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
}

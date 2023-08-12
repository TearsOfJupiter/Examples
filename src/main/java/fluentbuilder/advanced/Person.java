package fluentbuilder.advanced;

@SuppressWarnings("unused")
public class Person
{
  protected String name;

  public String getName()
  {
    return name;
  }
  private void setName(final String name)
  {
    this.name = name;
  }

  @Override
  public String toString()
  {
    return
        "Person{" +
          "name='" + name + '\'' +
        '}';
  }

  public static <PB extends PersonBuilder<PB>> PersonBuilder<PB> newPersonBuilder()
  {
    return new PersonBuilder<>();
  }
  protected static class PersonBuilder<PB extends PersonBuilder<PB>>
  {
    protected Person person;

    public PersonBuilder()
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
}
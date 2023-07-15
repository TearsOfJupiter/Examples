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
    return "Person{" +
        "name='" + name + '\'' +
        '}';
  }

  public static <PB extends PersonBuilder<PB>> PersonBuilder<PB> newPersonBuilder()
  {
    return new PersonBuilder<>();
  }
  protected static class PersonBuilder<SELF extends PersonBuilder<SELF>>
  {
    protected Person person;

    public PersonBuilder()
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
}

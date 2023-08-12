package fluentbuilder.expert;

public class Person<T extends Person<T>> implements IPerson
{
  private String name;

  @Override
  public String getName()
  {
    return name;
  }
  @SuppressWarnings("unchecked")
  public T withName(final String name)
  {
    this.name = name;
    return (T) this;
  }

  @Override
  public String toString()
  {
    return
        "Person{" +
          "name='" + name + '\'' +
        '}';
  }
}
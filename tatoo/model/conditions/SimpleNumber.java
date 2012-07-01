package tatoo.model.conditions;

public class SimpleNumber extends AbstractNumberCondition<Integer> {

  protected Integer number;

  public SimpleNumber()
  {this(0);}
  
  public SimpleNumber(int num) {
    number = num;
  }

  public Integer getValue() {
    return number;
  }
  
  @Override
  public void setValue(Number val) {
    number = val.intValue();
    fireValueChanged();
  }

  public String toString() {
    return number.toString();
  }

}

package tatoo.model.conditions;

public class SimpleNumber extends AbstractNumberCondition {

  protected Integer number;

  public SimpleNumber(int num) {
    number = num;
  }

  public Integer getValue() {
    return number;
  }

  public void setValue(Integer val) {
    number = val;
    fireValueChanged();
  }

  public String toString() {
    return number.toString();
  }

}

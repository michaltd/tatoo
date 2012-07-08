package tatoo.model.conditions;

/**
 * Eine Condition welche eine Zahl repräsentiert. Die Condition macht nichts anderes als einen Integer zu kapseln.
 * @author mkortz
 *
 */
public class SimpleNumber extends AbstractNumberCondition<Integer> {

  /**
   * Das gekapselte Integer-Objekt.
   */
  protected Integer number;

  /**
   * Initialisiert die Condition mit dem Wert 0
   */
  public SimpleNumber()
  {this(0);}
  
  /**
   * Initialisiert die Conditin mit dem Wert von des übergebenen Parameters.
   * @param num Der Wert mit dem die Condition initialisiert werden soll.
   */
  public SimpleNumber(int num) {
    number = num;
  }

  @Override
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

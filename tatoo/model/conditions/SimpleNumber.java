package tatoo.model.conditions;

import java.util.IdentityHashMap;

import tatoo.model.entities.AbstractEntity;

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

  
  IdentityHashMap<AbstractEntity, SimpleNumber> copies = new IdentityHashMap<AbstractEntity, SimpleNumber>();

  /**
   * Initialisiert die Condition mit dem Wert 0
   */
  public SimpleNumber(){
    this(0);
  }
  
  /**
   * Initialisiert die Conditin mit dem Wert von des übergebenen Parameters.
   * @param num Der Wert mit dem die Condition initialisiert werden soll.
   */
  public SimpleNumber(int num) {
    number = num;
  }
  
  public SimpleNumber(SimpleNumber condition) {
    this(condition.number);
  }
  
  @Override
  public SimpleNumber clone() throws CloneNotSupportedException {
    
    AbstractEntity e = getOwnerNode();    
    
    SimpleNumber copy = copies.get(e);
    
    if (copy == null){
      copy = new SimpleNumber(number);
      copies.put(e, copy);
    }
    return copy;
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

package tatoo.model.conditions;

import java.util.IdentityHashMap;

import tatoo.model.entities.AbstractEntity;

/**
 * Stellt eine NumberCondition dar, die von einer anderen NumberCondition
 * abhängt und dadurch während der Laufzeit andere Werte annehmen kann. Die
 * Klasse enthält zwei NumberConditions source und value. Value wird
 * normalerweise als {@link SimpleNumber} angegeben und source ist die Condition
 * welche die Grundlage der Berechnung ist.<br />
 * Es können jedoch auch zwei <code>CalculatedNumber</code> als Parameter
 * angegeben werden und so beliebig geschachtelt werden. Dabe ist jedoch
 * Vorsicht geboten: Kreise werden derzeit noch nicht abgefangen.
 * 
 * @author mkortz
 * 
 */
public class CalculatedNumber extends AbstractNumberCondition<Integer>
    implements ConditionListener {

  /**
   * Die Arithmetik die für die Berechnung der Condition zuständig ist.
   * 
   * @author mkortz
   * 
   */
  public enum Arithmetic {
    /**
     * Steht für die Addition.
     */
    ADD("+") {
      protected Integer solve(NumberCondition<Integer> src,
          NumberCondition<Integer> val) {
        return src.getValue().intValue() + val.getValue().intValue();
      }
    },
    /**
     * Steht für die Subtraktion.
     */
    SUBTRACT("-") {
      protected Integer solve(NumberCondition<Integer> src,
          NumberCondition<Integer> val) {
        return src.getValue().intValue() - val.getValue().intValue();
      }
    },
    /**
     * Steht für die Multiplikation.
     */
    MULTIPLY("*") {
      protected Integer solve(NumberCondition<Integer> src,
          NumberCondition<Integer> val) {
        return src.getValue().intValue() * val.getValue().intValue();
      }
    },
    /**
     * Steht für die Division.
     */
    DIVIDE("/") {
      protected Integer solve(NumberCondition<Integer> src,
          NumberCondition<Integer> val) {
        return src.getValue().intValue() / val.getValue().intValue();
      }
    };

    /**
     * Die Arithmetik als String
     */
    private String calculator;

    private Arithmetic(String calculator) {
      this.calculator = calculator;
    }

    /**
     * Berechnet mittels der hinterlegten Arithmetik ein Ergebnis aus den Beiden
     * enthaltenen NumberConditions und gibt es als Integer zurück. 
     * 
     * @param src die source Condition
     * @param val die value Condition
     * @return Das Ergebnis der Berechnung als Integer
     */
    protected abstract Integer solve(NumberCondition<Integer> src,
        NumberCondition<Integer> val);

    public String toString() {
      return calculator;
    }

  }

  /**
   * Die sourcecondition.
   */
  NumberCondition<Integer> source;
  /**
   * Die Valuecondition.
   */
  NumberCondition<Integer> value;
  /**
   * Die Arithmetik mit der source und value berechnet werden.
   */
  Arithmetic arith;
  
  IdentityHashMap<AbstractEntity, CalculatedNumber> copies = new IdentityHashMap<AbstractEntity, CalculatedNumber>();

  public CalculatedNumber() {
    this( new SimpleNumber(0), new SimpleNumber(0), Arithmetic.ADD );
  }


  public CalculatedNumber(NumberCondition<Integer> src, Integer value, Arithmetic a) {
    this( src, new SimpleNumber(value), a);
  }

  public CalculatedNumber(NumberCondition<Integer> src, NumberCondition<Integer> value, Arithmetic a) {
    this.source = src;
    this.source.setOwner(getOwnerNode());
    this.arith = a;
    this.value = value;
    this.value.setOwner(getOwnerNode());
    src.addChangeListener(this);
    value.addChangeListener(this);
  }
  
  @Override
  public AbstractNumberCondition<Integer> cloneFor(AbstractEntity entity) throws CloneNotSupportedException {
    
    AbstractEntity owner = entity;
    // zunächst mal eine neue Condition erzeugen:
    CalculatedNumber copy = new CalculatedNumber();
    // gibt es eine Kopie für source? die nehmen. Ansonsten source clonen.
    copy.source = (NumberCondition<Integer>) source.cloneFor(owner);
    copy.source.addChangeListener(copy);
    copy.value = (NumberCondition<Integer>) value.cloneFor(owner);
    copy.value.addChangeListener(copy);
    copy.arith = this.arith;
    
    copies.put(owner, copy);
    return copy;
  }

  @Override
  public Integer getValue() {
    return arith.solve(source, value);
  }

  @Override
  public void setValue(Number val) {
    value.setValue(val);
  }

  public String toString() {
    return getValue().toString();
  }

  @Override
  public void valueChanged() {
    fireValueChanged();
  }

}

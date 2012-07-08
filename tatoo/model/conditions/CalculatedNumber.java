package tatoo.model.conditions;

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
  NumberCondition<Integer> source = new SimpleNumber(0);
  /**
   * Die Valuecondition.
   */
  NumberCondition<Integer> value = new SimpleNumber(0);
  /**
   * Die Arithmetik mit der source und value berechnet werden.
   */
  Arithmetic arith;

  public CalculatedNumber() {
  }

  public CalculatedNumber(NumberCondition<Integer> condition, Integer value,
      Arithmetic a) {
    this.source = condition;
    this.arith = a;
    this.value.setValue(value);
    condition.addChangeListener(this);
  }

  public CalculatedNumber(NumberCondition<Integer> src,
      NumberCondition<Integer> value, Arithmetic a) {
    this.source = src;
    this.arith = a;
    this.value = value;
    src.addChangeListener(this);
    value.addChangeListener(this);
  }

  /**
   * Setzt eine neue source-Condition
   * @param src die Sourcecoondition.
   */
  public void setCalculationSource(NumberCondition<Integer> src) {
    source = src;
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
    return "";
  }

  @Override
  public void valueChanged() {
    fireValueChanged();
  }

}

package tatoo.model.conditions;

public class CalculatedNumber extends AbstractNumberCondition implements ConditionListener {

  public enum Arithmetic {
    ADD("+") {
      protected Integer solve(NumberCondition src, NumberCondition val) {
        return src.getValue() + val.getValue();
      }
    },
    SUBTRACT("-") {
      protected Integer solve(NumberCondition src, NumberCondition val) {
        return src.getValue() - val.getValue();
      }
    },
    MULTIPLY("*") {
      protected Integer solve(NumberCondition src, NumberCondition val) {
        return src.getValue() * val.getValue();
      }
    },
    DIVIDE("/") {
      protected Integer solve(NumberCondition src, NumberCondition val) {
        return src.getValue() / val.getValue();
      }
    };

    String calculator;
    Arithmetic(String calculator){
      this.calculator = calculator;
    }
    protected abstract Integer solve(NumberCondition src, NumberCondition val);
    public String toString(){
      return calculator;
    }
    
  }

  NumberCondition source = new SimpleNumber(0);
  NumberCondition value = new SimpleNumber(0);
  Arithmetic arith;

  public CalculatedNumber(NumberCondition condition, Integer value, Arithmetic a) {
    this.source = condition;
    this.arith = a;
    this.value.setValue(value);
    condition.addChangeListener(this);
  }

  public CalculatedNumber(NumberCondition src, NumberCondition value, Arithmetic a) {
    this.source = src;
    this.arith = a;
    this.value = value;
    src.addChangeListener(this);
    value.addChangeListener(this);
  }

  public void setCalculationSource(NumberCondition src) {
    source = src;
  }

  @Override
  public Integer getValue() {
    return arith.solve(source, value);
  }

  @Override
  public void setValue(Integer val) {
    value.setValue(val);
  }
  
  public String toString(){
  	return "";
  }

	@Override
	public void valueChanged() {
		fireValueChanged();		
	}

}

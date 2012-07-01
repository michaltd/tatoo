package tatoo.model.conditions;

public class CalculatedNumber extends AbstractNumberCondition<Integer> implements ConditionListener {

  public enum Arithmetic {
    ADD("+") {
      protected Integer solve(NumberCondition<Integer> src, NumberCondition<Integer> val) {
        return src.getValue().intValue() + val.getValue().intValue();
      }
    },
    SUBTRACT("-") {
      protected Integer solve(NumberCondition<Integer> src, NumberCondition<Integer> val) {
        return src.getValue().intValue() - val.getValue().intValue();
      }
    },
    MULTIPLY("*") {
      protected Integer solve(NumberCondition<Integer> src, NumberCondition<Integer> val) {
        return src.getValue().intValue() * val.getValue().intValue();
      }
    },
    DIVIDE("/") {
      protected Integer solve(NumberCondition<Integer> src, NumberCondition<Integer> val) {
        return src.getValue().intValue() / val.getValue().intValue();
      }
    };

    String calculator;
    Arithmetic(String calculator){
      this.calculator = calculator;
    }
    protected abstract Integer solve(NumberCondition<Integer> src, NumberCondition<Integer> val);
    public String toString(){
      return calculator;
    }
    
  }

  NumberCondition<Integer> source = new SimpleNumber(0);
  NumberCondition<Integer> value = new SimpleNumber(0);
  Arithmetic arith;
  
  public CalculatedNumber(){}

  public CalculatedNumber(NumberCondition<Integer> condition, Integer value, Arithmetic a) {
    this.source = condition;
    this.arith = a;
    this.value.setValue(value);
    condition.addChangeListener(this);
  }

  public CalculatedNumber(NumberCondition<Integer> src, NumberCondition<Integer> value, Arithmetic a) {
    this.source = src;
    this.arith = a;
    this.value = value;
    src.addChangeListener(this);
    value.addChangeListener(this);
  }

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
  
  public String toString(){
  	return "";
  }

	@Override
	public void valueChanged() {
		fireValueChanged();		
	}


}

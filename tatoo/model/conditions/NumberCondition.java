package tatoo.model.conditions;

public interface NumberCondition extends Condition<Integer>, Comparable<NumberCondition> {
  
  public abstract int compareTo(NumberCondition numbCond);
  
  public void addChangeListener(ConditionListener l);
  
  public void removeChangeListener(ConditionListener l);

}
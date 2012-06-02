package tatoo.model.conditions;




public interface Condition<T> {
	
  public T getValue();
  public void setValue(T val);
  
}

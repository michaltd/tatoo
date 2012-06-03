package tatoo.model.conditions;



/**
 * Eine 
 * @author mkortz
 *
 * @param <T>
 */
public interface Condition<T> {
	
  public T getValue();
  public void setValue(T val);
  
}

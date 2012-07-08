package tatoo.model.conditions;

import java.util.EventListener;

/**
 * Oberklasse aller Klassen die auf Conditions lauschen.
 * 
 * @author mkortz
 */
public interface ConditionListener extends EventListener {

  /**
   * Horcht auf das ValueChangedEvent.
   */
  public void valueChanged();

}

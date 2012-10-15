package tatoo.model.entities.events;

import java.util.EventObject;

import tatoo.model.conditions.Condition.ConditionTypes;


@SuppressWarnings("serial")
public class EntityModelEvent extends EventObject {
  
  ConditionTypes attrib;
  
  public EntityModelEvent(Object source, ConditionTypes attrib) {
    super(source);
    this.attrib = attrib;
  }
  
  public ConditionTypes getAttrib(){
  	return attrib;
  }

}

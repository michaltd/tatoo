package tatoo.model.entities;

import java.util.EventListener;

import tatoo.model.conditions.Condition.ConditionTypes;

public interface EntityListener extends EventListener {
	
  
	public void AttribChanged(AbstractEntity entity, ConditionTypes attrib);

}

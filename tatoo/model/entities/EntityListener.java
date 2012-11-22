package tatoo.model.entities;

import java.util.EventListener;

import tatoo.model.conditions.Condition.ConditionTypes;

public interface EntityListener extends EventListener {

    public void AttribChanged (AbstractEntity entity, ConditionTypes attrib);
    
    public void ChildInserted (AbstractEntity entity, AbstractEntity child);
    
    public void ChildRemoved (AbstractEntity entity, AbstractEntity child, int index);

}

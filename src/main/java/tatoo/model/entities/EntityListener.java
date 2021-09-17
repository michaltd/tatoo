package tatoo.model.entities;

import java.util.EventListener;

import tatoo.model.conditions.Condition.ConditionTypes;

public interface EntityListener extends EventListener
{
    void AttribChanged(AbstractEntity entity, ConditionTypes attrib);

    void ChildInserted(AbstractEntity entity, AbstractEntity child);

    void ChildRemoved(AbstractEntity entity, AbstractEntity child, int index);
}

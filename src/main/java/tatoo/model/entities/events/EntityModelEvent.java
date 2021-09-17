package tatoo.model.entities.events;

import lombok.Getter;
import tatoo.model.conditions.Condition.ConditionTypes;

import java.util.EventObject;

public class EntityModelEvent extends EventObject
{
    @Getter
    ConditionTypes attrib;

    public EntityModelEvent(Object source, ConditionTypes attrib)
    {
        super(source);

        this.attrib = attrib;
    }
}

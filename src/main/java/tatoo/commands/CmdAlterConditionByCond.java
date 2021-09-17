package tatoo.commands;

import tatoo.Command;
import tatoo.db.DBFactory;
import tatoo.model.conditions.Condition;
import tatoo.model.conditions.Condition.ConditionTypes;
import tatoo.model.entities.AbstractEntity;

@SuppressWarnings("rawtypes")
public class CmdAlterConditionByCond implements Command
{
    AbstractEntity entity;
    ConditionTypes conditionType;
    Condition oldValue;
    Condition newValue;

    public CmdAlterConditionByCond(AbstractEntity entity, Condition condition, ConditionTypes conditionType)
    {
        this.entity = entity;
        this.conditionType = conditionType;
        this.newValue = condition;
        this.oldValue = entity.getAttribute(conditionType);
    }

    @Override
    public void execute()
    {
        entity.setAttribute(newValue, conditionType);
    }

    @Override
    public void undo()
    {
        entity.setAttribute(oldValue, conditionType);
    }

    @Override
    public void redo()
    {
        entity.setAttribute(newValue, conditionType);
    }

    @Override
    public void writeObject()
    {
        DBFactory.getInstance().write(entity);
    }

    @Override
    public void deleteObject()
    {
        DBFactory.getInstance().write(entity);
    }
}

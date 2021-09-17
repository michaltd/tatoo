package tatoo.commands;

import tatoo.Command;
import tatoo.db.DBFactory;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.AbstractEntity.EntityType;

public class CmdSetEntityType implements Command
{
    AbstractEntity entity;
    EntityType newType;
    EntityType oldType;

    public CmdSetEntityType(AbstractEntity entity, EntityType type)
    {
        this.entity = entity;
        newType = type;
        oldType = entity.getType();
    }

    @Override
    public void execute()
    {
        entity.setType(newType);
    }

    @Override
    public void undo()
    {
        entity.setType(oldType);
    }

    @Override
    public void redo()
    {
        entity.setType(newType);
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

package tatoo.commands;

import tatoo.Command;
import tatoo.db.DBFactory;
import tatoo.model.entities.AbstractEntity;

public class CmdRemoveEntityChild implements Command
{
    AbstractEntity parent;
    AbstractEntity child;

    public CmdRemoveEntityChild(AbstractEntity parent, AbstractEntity child)
    {
        this.parent = parent;
        this.child = child;
    }

    @Override
    public void execute()
    {
        parent.removeEntity(child);
    }

    @Override
    public void undo()
    {
        parent.addEntity(child);
    }

    @Override
    public void redo()
    {
        parent.removeEntity(child);
    }

    @Override
    public void writeObject()
    {
        DBFactory.getInstance().write(parent);
        DBFactory.getInstance().delete(child);
    }

    @Override
    public void deleteObject()
    {
        DBFactory.getInstance().write(parent);
    }
}

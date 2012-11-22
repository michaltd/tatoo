package tatoo.commands;

import tatoo.Command;
import tatoo.model.entities.AbstractEntity;


public class CmdInsertEntityChild implements Command {
    
    AbstractEntity parent;
    AbstractEntity child;

    public CmdInsertEntityChild (AbstractEntity parent, AbstractEntity child) {
        this.parent = parent;
        this.child = child;
    }

    @Override
    public void execute () {
        parent.addEntity (child);
    }

    @Override
    public void undo () {
        parent.removeEntity (child);
    }

    @Override
    public void redo () {
        parent.addEntity (child);
    }

}

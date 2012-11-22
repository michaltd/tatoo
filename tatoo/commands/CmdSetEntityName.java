package tatoo.commands;

import tatoo.Command;
import tatoo.model.entities.AbstractEntity;


public class CmdSetEntityName implements Command {

    AbstractEntity entity;
    String newName;
    String oldName;
    
    public CmdSetEntityName (AbstractEntity entity, String name) {
        this.entity = entity;
        newName = name;
        oldName = entity.getName ();
    }

    @Override
    public void execute () {
        entity.setName (newName);
    }

    @Override
    public void undo () {
        entity.setName (oldName);
    }

    @Override
    public void redo () {
        entity.setName (newName);
    }

}

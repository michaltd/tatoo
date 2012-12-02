package tatoo.commands;

import tatoo.Command;
import tatoo.db.DBFactory;
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

    @Override
    public void writeObject () {
        DBFactory.getInstance ().write(entity);
    }

    @Override
    public void deleteObject () {
        DBFactory.getInstance ().write(entity);
    }

}

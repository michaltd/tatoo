package tatoo.commands;

import tatoo.Command;
import tatoo.db.DBFactory;
import tatoo.model.conditions.AbstractNumberCondition;
import tatoo.model.conditions.Condition;

@SuppressWarnings ("rawtypes")
public class CmdAlterConditionByVal implements Command {

    private int                     oldValue;
    private int                     newValue;
    private AbstractNumberCondition dataset;

    public CmdAlterConditionByVal (Condition dataset, int newValue) {
        this.dataset = (AbstractNumberCondition)dataset;
        oldValue = dataset.getValue ();
        this.newValue = newValue;
    }

    @SuppressWarnings ("unchecked")
    @Override
    public void execute () {
        dataset.setValue (newValue);
    }

    @SuppressWarnings ("unchecked")
    @Override
    public void undo () {
        dataset.setValue (oldValue);

    }

    @SuppressWarnings ("unchecked")
    @Override
    public void redo () {
        dataset.setValue (newValue);

    }

    @Override
    public void writeObject () {
        DBFactory.getInstance ().write(dataset);
    }

    @Override
    public void deleteObject () {
        DBFactory.getInstance ().write(dataset);
    }

}

package tatoo.commands;

import tatoo.Command;
import tatoo.model.conditions.AbstractNumberCondition;

@SuppressWarnings ("rawtypes")
public class CmdAlterAttribute implements Command {

    private int                     oldValue;
    private int                     newValue;
    private AbstractNumberCondition dataset;

    public CmdAlterAttribute (AbstractNumberCondition dataset, int newValue) {
        this.dataset = dataset;
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

}

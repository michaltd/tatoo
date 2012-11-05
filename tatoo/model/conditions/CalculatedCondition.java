package tatoo.model.conditions;

@SuppressWarnings ("rawtypes")
public interface CalculatedCondition <T> extends Condition <T> {
    
    public abstract Condition getSourceCondition ();

    public abstract Condition getValueCondition ();

    public abstract void setSource (Condition src);
    
    public abstract Character getOperator();

}
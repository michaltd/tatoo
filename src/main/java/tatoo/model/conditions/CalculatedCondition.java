package tatoo.model.conditions;

@SuppressWarnings("rawtypes")
public interface CalculatedCondition<T> extends Condition<T>
{
    Condition getSourceCondition();

    Condition getValueCondition();

    void setSource(Condition src);

    Character getOperator();
}
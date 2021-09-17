package tatoo.model.conditions;

/**
 * Eine Condition welche eine Zahl repräsentiert.
 *
 * @param <T>
 * @author mkortz
 */
public interface NumberCondition<T> extends Condition<T>, Comparable<NumberCondition<T>>
{
    @Override
    int compareTo(NumberCondition<T> numbCond);
}
package tatoo.model.conditions;

/**
 * Eine Condition welche eine Zahl repräsentiert.
 * 
 * @author mkortz
 * @param <T>
 */
public interface NumberCondition<T> extends Condition <Number>, Comparable <NumberCondition <T>> {

    @Override
    public abstract int compareTo (NumberCondition <T> numbCond);

}
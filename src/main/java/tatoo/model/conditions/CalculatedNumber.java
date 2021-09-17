package tatoo.model.conditions;

import tatoo.model.entities.AbstractEntity;

/**
 * Stellt eine NumberCondition dar, die von einer anderen NumberCondition
 * abhängt und dadurch während der Laufzeit andere Werte annehmen kann. Die
 * Klasse enthält zwei NumberConditions source und value. Value wird
 * normalerweise als {@link SimpleNumber} angegeben und source ist die Condition
 * welche die Grundlage der Berechnung ist.<br />
 * Es können jedoch auch zwei <code>CalculatedNumber</code> als Parameter
 * angegeben werden und so beliebig geschachtelt werden. Dabe ist jedoch
 * Vorsicht geboten: Kreise werden derzeit noch nicht abgefangen.
 *
 * @author mkortz
 */
public class CalculatedNumber extends AbstractNumberCondition<Integer> implements ConditionListener, CalculatedCondition<Integer>
{
    /**
     * Die sourcecondition.
     */
    Condition<Integer> source;

    /**
     * Die Valuecondition.
     */
    Condition<Integer> value;

    /**
     * Die Arithmetik mit der source und value berechnet werden.
     */
    Arithmetic arith;

    public CalculatedNumber()
    {
        this(new SimpleNumber(0), new SimpleNumber(0), Arithmetic.MULTIPLY);
    }

    public CalculatedNumber(Condition<Integer> src, Integer value, Arithmetic a)
    {
        this(src, new SimpleNumber(value), a);
    }

    public CalculatedNumber(Condition<Integer> src, Condition<Integer> value, Arithmetic a)
    {
        this.source = src;
        if (source.getOwnerNode() == null)
            this.source.setOwner(getOwnerNode());
        this.arith = a;
        this.value = value;
        if (value.getOwnerNode() == null)
            this.value.setOwner(getOwnerNode());
        src.addChangeListener(this);
        value.addChangeListener(this);
    }

    @Override
    public AbstractNumberCondition<Integer> cloneFor(AbstractEntity entity) throws CloneNotSupportedException
    {
        AbstractEntity sourceNode = entity.getEntityNode();

        // zunächst mal eine neue Condition erzeugen:

        CalculatedNumber copy = new CalculatedNumber();
        copy.setOwner(entity);

        copy.source = source.cloneFor(sourceNode);
        copy.source.addChangeListener(copy);
        copy.value = value.cloneFor(sourceNode);
        copy.value.addChangeListener(copy);
        copy.arith = this.arith;

        return copy;
    }

    /* (non-Javadoc)
     * @see tatoo.model.conditions.CalculatedCondition#getSourceCondition()
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Condition getSourceCondition()
    {
        return source;
    }

    /* (non-Javadoc)
     * @see tatoo.model.conditions.CalculatedCondition#getValueCondition()
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Condition getValueCondition()
    {
        return value;
    }

    @Override
    public Integer getValue()
    {
        return arith.solve(source, value);
    }

    @Override
    public void setValue(Integer val)
    {
        value.setValue(val);
        fireValueChanged(this);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setValue(Condition val)
    {
        value = val;
        value.addChangeListener(this);
        fireValueChanged(this);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void setSource(Condition src)
    {
        source = src;
    }

    public String toString()
    {
        return getValue().toString();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void valueChanged(Condition source)
    {
        fireValueChanged(source);
    }

    @Override
    public Character getOperator()
    {
        return arith.toString().toCharArray()[0];
    }

    /**
     * Die Arithmetik die für die Berechnung der Condition zuständig ist.
     *
     * @author mkortz
     */
    public enum Arithmetic
    {
        /**
         * Steht für die Addition.
         */
        ADD('+') {
            protected Integer solve(Condition<Integer> src, Condition<Integer> val)
            {
                return src.getValue() + val.getValue();
            }
        },
        /**
         * Steht für die Subtraktion.
         */
        SUBTRACT('-') {
            protected Integer solve(Condition<Integer> src, Condition<Integer> val)
            {
                return src.getValue() - val.getValue();
            }
        },
        /**
         * Steht für die Multiplikation.
         */
        MULTIPLY('*') {
            protected Integer solve(Condition<Integer> src, Condition<Integer> val)
            {
                return src.getValue() * val.getValue();
            }
        },
        /**
         * Steht für die Division.
         */
        DIVIDE('/') {
            protected Integer solve(Condition<Integer> src, Condition<Integer> val)
            {
                return src.getValue() / val.getValue();
            }
        };

        /**
         * Die Arithmetik als String
         */
        private final char calculator;

        Arithmetic(char calculator)
        {
            this.calculator = calculator;
        }

        /**
         * Berechnet mittels der hinterlegten Arithmetik ein Ergebnis aus den
         * Beiden enthaltenen NumberConditions und gibt es als Integer zurück.
         *
         * @param source die source Condition
         * @param value  die value Condition
         * @return Das Ergebnis der Berechnung als Integer
         */
        protected abstract Integer solve(Condition<Integer> source, Condition<Integer> value);

        public String toString()
        {
            return String.valueOf(calculator);
        }
    }
}

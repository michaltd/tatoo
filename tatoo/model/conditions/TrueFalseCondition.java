package tatoo.model.conditions;

import java.util.EventListener;
import java.util.IdentityHashMap;

import javax.swing.event.EventListenerList;

import tatoo.db.Dataset;
import tatoo.model.entities.AbstractEntity;

/**
 * Eine Condition welche wahr (1) oder falsch (0) zurückgibt. Ähnlich der <code>CalculatedNumber</code> können zwei 
 * Conditions übergeben werden die <code>value</code> und die <code>source</code>
 * @author mkortz
 *
 */
@SuppressWarnings ("rawtypes")
public class TrueFalseCondition extends AbstractNumberCondition<Boolean> implements ConditionListener, CalculatedCondition <Boolean> {

    /**
     * Die Arithmetik die für die Berechnung der Condition zuständig ist.
     * 
     * @author mkortz
     */
    public enum Arithmetic {
        /**
         * Liefert genau dann 1 zurück wenn "value" größer als "source" ist,
         * ansonsten 0.
         */
        GT ('>') {

            protected Integer solve (Condition src, Condition val) {
                return src.getValue ().intValue () > val.getValue ().intValue () ? 1 : 0;
            }
        },
        /**
         * Liefert genau dann true wenn "value" kleiner als "source" ist,
         * ansonsten 0
         */
        LT ('<') {

            protected Integer solve (Condition src, Condition val) {
                return src.getValue ().intValue () < val.getValue ().intValue () ? 1 : 0;
            }
        },
        /**
         * Liefert genau dann true wenn "value" gleich "source" ist, ansonsten 0
         */
        EQ ('=') {

            protected Integer solve (Condition src, Condition val) {
                return src.getValue ().intValue () == val.getValue ().intValue () ? 1 : 0;
            }
        };

        /**
         * Die Arithmetik als Charakter
         */
        private char calculator;

        private Arithmetic (char calculator) {
            this.calculator = calculator;
        }

        /**
         * Berechnet mittels der hinterlegten Arithmetik ein Ergebnis aus den
         * Beiden enthaltenen NumberConditions und gibt es als Integer zurück.
         * Gibt 1 zurück wenn das Ergebnis wahr ist und null (0) sonst.
         * 
         * @param src
         * die source Condition
         * @param val
         * die value Condition
         * @return Das Ergebnis der Berechnung als Integer
         */
        protected abstract Integer solve (Condition src, Condition val);

        public String toString () {
            return String.valueOf (calculator);
        }

    }

    /**
     * Die sourcecondition.
     */
    Condition                                          source;
    /**
     * Die Valuecondition.
     */
    Condition                                          value;
    /**
     * Die Arithmetik mit der source und value berechnet werden.
     */
    Arithmetic                                         arith;

    IdentityHashMap <AbstractEntity, CalculatedNumber> copies       = new IdentityHashMap <AbstractEntity, CalculatedNumber> ();

    /**
     * Konstruktor. Liefert immer true (1)
     */
    public TrueFalseCondition () {
        this (new SimpleNumber (0), new SimpleNumber (0), Arithmetic.EQ);
    }

    /**
     * Konstruktor
     * @param src die Surce-Condition
     * @param value die Value-Condition
     * @param a die Arithmetik die verwendung findet
     */
    public TrueFalseCondition (Condition src, Condition value, Arithmetic a) {
        this.source = src;
        if (source.getOwnerNode () == null)
            this.source.setOwner (getOwnerNode ());
        this.arith = a;
        this.value = value;
        if (value.getOwnerNode () == null)
            this.value.setOwner (getOwnerNode ());
        src.addChangeListener (this);
        value.addChangeListener (this);
    }

    /**
     * Kopiert die Condition für das übergebene Entity
     */
    public TrueFalseCondition cloneFor (AbstractEntity entity) throws CloneNotSupportedException {

        AbstractEntity sourceNode = entity.getEntityNode ();

        // zunächst mal eine neue Condition erzeugen:
        TrueFalseCondition copy = new TrueFalseCondition ();
        copy.setOwner (entity);

        copy.source = source.cloneFor (sourceNode);
        copy.source.addChangeListener (copy);
        copy.value = value.cloneFor (sourceNode);
        copy.value.addChangeListener (copy);
        copy.arith = this.arith;

        return copy;
    }

    @Override
    public Condition getSourceCondition () {
        return source;
    }

    @Override
    public Condition getValueCondition () {
        return value;
    }

    public Integer getValue () {
        return arith.solve (source, value);
    }
    
    @SuppressWarnings ("unchecked")
    public void setValue (Boolean val) {
        // wird eh nicht aufgerufen. 
        // hmm ob das Sinn ergibt das dann so zu implementieren hier?
//         value.setValue (val);
//         fireValueChanged (this);
    }

    public void setValue (Condition val) {
        value = val;
        value.addChangeListener (this);
        fireValueChanged (this);
    }

    public void setSource (Condition src) {
        source = src;
    }

    public String toString () {
        return getValue ().toString ();
    }

    @Override
    public void valueChanged (Condition source) {
        fireValueChanged (source);
    }

    @Override
    public Character getOperator () {
        return arith.toString ().toCharArray ()[0];
    }
}

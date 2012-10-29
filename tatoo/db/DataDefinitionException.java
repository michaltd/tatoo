package tatoo.db;

/**
 * Wird von einer Datendefinition geworfen, wenn etwas schief läuft.
 * 
 * @author mkortz
 */
public class DataDefinitionException extends RuntimeException {

    public DataDefinitionException (String message) {
        super (message);
    }

}

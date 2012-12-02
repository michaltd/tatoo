package tatoo;

/**
 * Interface für ein Kommando
 * @author mkortz
 *
 */
public interface Command {
    
    /**
     * Das Kommando ausführen
     */
    public void execute();
    
    /**
     * Das Kommando rückgängig machen
     */
    public void undo();
    
    /**
     * Das Kommando erneut ausführen
     */
    public void redo();

    /**
     * Schreibt das Kommando in die Datenstruktur
     */
    public void writeObject ();

    /**
     * Löscht das Kommando aus der Datenstruktur
     */
    public void deleteObject ();

}

package tatoo;

/**
 * Interface für ein Kommando
 *
 * @author mkortz
 */
public interface Command
{
    /**
     * Das Kommando ausführen
     */
    void execute();

    /**
     * Das Kommando rückgängig machen
     */
    void undo();

    /**
     * Das Kommando erneut ausführen
     */
    void redo();

    /**
     * Schreibt das Kommando in die Datenstruktur
     */
    void writeObject();

    /**
     * Löscht das Kommando aus der Datenstruktur
     */
    void deleteObject();
}

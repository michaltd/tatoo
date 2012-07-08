package tatoo.db;

/**
 * Oberklasse aller Klassen welche Serialisiert werden sollen.
 * @author mkortz
 *
 */
public class Dataset {

  /**
   * Die id des Objektes in der Datenbank
   */
  private int id;
  
  /**
   * Gibt die Id des Objektes zurück.
   * @return Die Id des Objektes.
   */
  public int getId() {
    return id;
  }

  /**
   * Setzt die Id des Objektes.
   * @param id Die Id des Objektes.
   */
  @SuppressWarnings("unused")
  private void setId(int id) {
    this.id = id;
  }
  
}

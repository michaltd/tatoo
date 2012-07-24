package tatoo.db;

import java.util.LinkedList;

import tatoo.db.sql.DBSchema;

/**
 * Eine Abfrage auf den Daten. Liest Datensätze aufgrund der übergebenen
 * Kriterien und gibt sie als Objekte zurück.
 * 
 * @author mkortz
 * 
 */
public abstract class Query extends DataHandler {

  public Query(DBSchema schema) {
    super(schema);
  }

  /**
   * Legt die Klasse fest für die ein Datensatz gelesen werden soll.
   * 
   * @param cl
   *          die Klasse für die ein Objekt aus der Datenbank gelesen werden
   *          soll.
   * @return Ein Objekt dieser Query.
   */
  public abstract Query get(Class<?> cl);

  /**
   * Setzt eine Bedingung für die Abfrage. Wenn z.B. nur Datensätze, welche
   * einem bestimmten Kriterium entsprechen abgefragt werden sollen, wird eine
   * Condition gesetzt. Es werden nur Datensätze geändert, welche mit der
   * Condition übereinstimmen.
   * 
   * @param condition
   *          Der String muss in der Form "name=bedingung" vorliegen. z.B.
   *          dataset_id=5
   * @return Ein Objekt dieser Abfrage.
   */
  public abstract Query addCondition(String condition);

  /**
   * Setzt den String, der String der hinter "ORDER BY" steht. Darf nur die
   * Namen der Felder enthalten, nach denen sortiert werden soll. Die Felder
   * müssen als ein langer String übergeben werden.
   * 
   * @param order
   *          Der String muss in der Form "name=bedingung" vorliegen. z.B.
   *          dataset_id=5
   * @return Ein Objekt dieser Abfrage.
   */
  public abstract Query orderBy(String order);

  /**
   * Der String der hinter "GROUP BY" steht. t_groupBy darf nur die Namen der
   * Felder enthalten nach denen gruppiert werden soll. Die Felder müssen als
   * ein langer String übergeben werden.
   * 
   * @param group Der String muss als Komma-Separierte Liste übergeben werden, so wie er auch in eine SQL-Anweisung auftauchen würde.
   * @return Ein Objekt dieser Abfrage.
   */
  public abstract Query groupBy(String group);

  /**
   * Führt die Abfrage aus. Liefert die gefundenen Objekte zurück. Wenn ein
   * Objekt nicht gefunden wurde, oder ein anderer Fehler auftritt wird
   * <code>null</code> zurück gegeben.
   * 
   * @return eine {@link LinkedList} von gefundenen Datasets.
   */
  public abstract LinkedList<Dataset> execute();

}

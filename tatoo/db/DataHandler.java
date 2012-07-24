package tatoo.db;

import java.util.HashMap;

import tatoo.db.sql.DBSchema;

/**
 * Oberklasse aller Datenbankoperationen.
 * Die Klasse hält das Datenbankschema vor, welches von den Datenoperationen genutz wird. Ausserdem ist ein Cache für 
 * aus der Datenbank gelesene Objekte implementiert.
 * @see #entityCache
 * @author mkortz
 *
 */
public class DataHandler {

  /**
   * Hält alle Datasets vor, welche wärend eines Lesevorgangs ausgelesen, oder während
   * eines Schreibvorgangs geschrieben wurden.
   * So können Referenzen auf diese bereits gelesenen Objekte in neu gelesene Objekte eingefügt werden.
   * Das Problem besteht beim Einlesen von ArmyListEntity-Bäumen. Listener z.B. in einer tieferen Baumebene
   * beobachten hier manchmal Knoten in einer höheren Ebene. Um sie nicht ein zweites mal zu instanziieren wird die 
   * Referenz dann aus dem EntityCache geholt.
   * @see #addToCache(Dataset)
   * @see #getFromCache(Integer)
   *  
   */
  private static HashMap<Integer, Dataset> entityCache = null;
  
  /**
   * Das Datenbankschema wie es aus der Schema.xml gelesen wird.
   */
  protected DBSchema schema;
  
  public DataHandler()
  {
    if (DataHandler.entityCache == null)
      DataHandler.entityCache = new HashMap<Integer, Dataset>() ; 
  }
  
  public DataHandler(DBSchema schema)
  {
    this();
    this.schema = schema;
  }
  
  /**
   * Fügt ein Dataset in den Cache ein. 
   * @param dataset Das Dataset, welches in den Cache eingefügt werden soll.
   * @see #entityCache
   * @see #getFromCache(Integer)
   */
  protected void addToCache(Dataset dataset)
  {
    entityCache.put(dataset.getId(), dataset);
  }
  
  /**
   * Liest ein Dataset aus dem Cache.
   * @param datasetId Die Id des Datasets nach der im Cache gesucht wird.
   * @return Das gefundene Dataset. Wird kein DAtaset mit der übergebenen Id gefunden, wird <code>null</code> zurück gegeben.
   * @see #entityCache
   * @see #addToCache(Dataset)
   */
  protected Dataset getFromCache(Integer datasetId)
  {
    return entityCache.get(datasetId);
  }
}

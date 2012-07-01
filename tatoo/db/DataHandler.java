package tatoo.db;

import java.util.HashMap;

import tatoo.db.sql.DBSchema;

public class DataHandler {

  /**
   * Der Entity Cache hält alle Datasets vor, welche wärend eines Lesevorgangs ausgelesen wurden.
   * So können Referenzen auf diese bereits gelesenen Objekte in neu gelesene Objekte eingefügt werden.
   * Das Problem besteht beim rekursiven Einlesen von Entity-Bäumen. Listener in einer niedrigen Baumebene
   * beobachten häufig Knoten in einer tieferen Ebene. Um sie nicht ein zweites mal zu instanziieren, wird die 
   * Referenz dann aus dem EntityCache geholt.
   *  
   */
  private static HashMap<Integer, Dataset> entityCache = null;
  private static int count;
  
  protected DBSchema schema;
  
  public DataHandler()
  {
    DataHandler.count++;
    if (DataHandler.entityCache == null)
      DataHandler.entityCache = new HashMap<Integer, Dataset>() ;
    
  }
  
  protected void addToCache(Dataset dataset)
  {
    entityCache.put(dataset.getId(), dataset);
  }
  
  protected Dataset getFromCache(Integer datasetId)
  {
    return entityCache.get(datasetId);
  }
  
  protected void finalize() throws Throwable
  {
    DataHandler.count--;
    if (DataHandler.count<= 1)
      DataHandler.entityCache = null;
    //do finalization here
    
    super.finalize(); //not necessary if extending Object.
  }
  
  

}

package tatoo.db;

@Deprecated 
/**
 * holy crap!
 * @author mkortz
 *
 */
public class Dataset {

  private static DBConnection conn = DBFactory.getInstance().getConnection();
  // die id des Objektes in der Datenbank
  private long id;

  /**
   * speichert das Objekt in der Datenbank
   * 
   * @return true wenn es geklappt hat. False sonst
   * @deprecated wird ersetzt durch eine Methode in einer DB Klasse. Wo genau weiss ich noch nicht.
   * Es soll so funktionieren: dbclass.save(Objekt);
   */
  public boolean save() {
    
    // Den Datensatz zu diesem Objekt suchen
    Query query = conn.createQuery();
    query.get(this.getClass());
    query.addCondition("Entity_id = " + this.id);
    
    // wird der Datensatz nicht gefunden muss das Objekt in die Datenbank
    // eingetragen werden
    DataManipulation datset = conn.createDataManipulation();
    if (query.execute().size() == 0) {
      int new_id = datset.insert(this);
      if (new_id > 0 ){
        this.id = new_id;
        return true;
      }
      return false;
    }
    // ansonsten muss es verändert werden.
    else {
      return datset.update(this) > 0;
    }
  }
  
  public boolean delete() {
    
    // Den Datensatz zu diesem Objekt suchen
    Query query = conn.createQuery();
    query.get(this.getClass());
    //query.addCondition(this.getClass().getSimpleName() + "_id = " + this.id);
    query.addCondition("Entity_id = " + this.id);
    
    // wird der Datensatz nicht gefunden muss das Objekt in die Datenbank
    // eingetragen werden
    DataManipulation datset = conn.createDataManipulation();
    if (query.execute().size() != 0) {
      datset.delete(this);
    }
    return false;
  }

}

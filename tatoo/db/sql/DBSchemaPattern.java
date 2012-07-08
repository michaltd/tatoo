package tatoo.db.sql;

/**
 * Oberklasse für die einzelnen Elemente in der XML-Schemadatei. Für jede Elementklasse aus der Schemadatei gibt es eine Unterklasse.
 * TODO: DBSchema Definition gehört wohl eher in das Paket tatoo.db
 * @author mkortz
 *
 */
public class DBSchemaPattern {
  
  /**
   * Der Typ des Elements.
   * @deprecated wird nicht mehr gebraucht. Der Typ des Elements kann auch über den Java operator <code>instanceof</code> herausgefunden werden.
   * @see DBSchemaPattern#definitonType
   */
  @Deprecated
  public enum SchemaType           
  {
    CLASS, PROPERTY, SET;
  }
  
  /**
   * Der Typ des Elements.
   * @deprecated wird nicht mehr gebraucht. Der Typ des Elements kann auch über den Java operator <code>instanceof</code> herausgefunden werden.
   */
  @Deprecated
  protected SchemaType definitonType;
  /**
   * Der Name es Elements. Der Name entspricht dem Namen des Attributes oder der Klasse in Java.
   */
  protected String name;
  
  /**
   * Gibt den Namen der SchemaDefinition zurück. Der Name entspricht dem Namen des Attributes oder der Klasse in Java.
   * @return Der Name des Attributes der abgebildeten Klasse.
   */
  public String getName(){
    return name;
  }

  /**
   *  @deprecated 
   *  @see DBSchemaPattern#definitonType
   */
  public SchemaType getSchemaType() {
    return definitonType;
  }

}

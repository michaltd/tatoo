package tatoo.db.sql;

import java.lang.reflect.Field;

import org.jdom.Attribute;
import org.jdom.Element;

/**
 * Abbildung des Propety-Elements aus der Schemadatei. Eine Property entspricht einen einfachen Attribut (Keine 1:n Assoziationen)
 * 
 * @author mkortz
 * 
 */
public class DBSchemaPropertyPattern extends DBSchemaPattern {
  
  /**
   * Name des abzubildenden Attributes in der Datenbank.
   */
  private String databaseName;
  /**
   * Typ des abzubildenden Attributes in der Datenbank.
   */
  private String type;
  
  /**
   * Istantiiert eine Property-Schablone aus dem übergebenen Element. 
   * @param el Das XML-Element aus dem die Schablone instantiiert wird.
   */
  public DBSchemaPropertyPattern(Element el) {

    this.definitonType = SchemaType.PROPERTY;
    
    Attribute propertyNameAttrib = el.getAttribute("name");
    this.name = propertyNameAttrib.getValue();
    Attribute DatabaseNameAttrib = el.getAttribute("column");
    this.databaseName = DatabaseNameAttrib.getValue();
    Attribute TypeNameAttrib = el.getAttribute("type");
    if (TypeNameAttrib != null)
      this.type = TypeNameAttrib.getValue();
  }
  
  /**
   * Gibt den Namen der Spalte zurück welche das Attribut der Klasse repräsentiert. 
   * @return Der Name der Spalte für das Attribut.
   */
  public String getDatabaseFieldName()
  {
    return databaseName;
  }
  
  /**
   * liefert den Typ des Feldes zurück.
   * @return Der Typ des Feldes in der Datenbank.
   */
  public String getType(){
    return type;
  }
  
  /**
   * Gibt für die übergebene Klasse und Objekt den Wert des Attributes zurück.
   * @param c Die Klasse in welchem das Attribut definiert ist.
   * @param o Das Objekt aus dem der Wert geholt werden soll.
   * @return Der Wert des Attributes als Object. 
   * @throws SecurityException
   * @throws NoSuchFieldException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public Object getValue(Class<?> c, Object o) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
  {
    Field fld = c.getDeclaredField(name);
    fld.setAccessible(true);
    return fld.get(c.cast(o));
  }
  
  
  
}
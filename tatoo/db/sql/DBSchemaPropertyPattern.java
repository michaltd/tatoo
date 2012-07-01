package tatoo.db.sql;

import java.lang.reflect.Field;

import org.jdom.Attribute;
import org.jdom.Element;

public class DBSchemaPropertyPattern extends DBSchemaDefinition {
  
  private String databaseName;
  private String type;
  
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

  
  public String getClassPropertyName()
  {
    return name;
  }
  
  public String getDatabaseFieldName()
  {
    return databaseName;
  }
  
  /**
   * liefert den Typ des Feldes zurück.
   * @return
   */
  public String getType(){
    return type;
  }
  
  /**
   * Gibt für die übergebene Klasse und Objekt den Wert des Attributes zurück.
   * @param c Die Klasse in welchem das Attribut definiert ist.
   * @param o Das Objekt aus dem der Wert geholt werden soll.
   * @return
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
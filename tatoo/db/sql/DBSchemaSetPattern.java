package tatoo.db.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import org.jdom.Attribute;
import org.jdom.Element;

import tatoo.db.Dataset;

public class DBSchemaSetPattern extends DBSchemaDefinition {

  private String tableName;
  private String type;
  
  public DBSchemaSetPattern(Element el) {

      this.definitonType = SchemaType.SET;
      
      Attribute setNameAttrib = el.getAttribute("name");
      this.name = setNameAttrib.getValue();
      Attribute tableNameAttrib = el.getAttribute("table");
      this.tableName = tableNameAttrib.getValue();
      Attribute typeAttrib = el.getAttribute("type");
      this.type = typeAttrib.getValue();

  }

  public String getTableName() {
    return tableName;
  }
  
  public String getType(){
    return this.type;
  }

  /**
   * Gibt den Inhalt der Collection des Datasets zurück.
   * @return
   * @throws NoSuchFieldException 
   * @throws SecurityException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  public Object[] getContent(Class<?> c, Dataset ds) 
      throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    Field fld = c.getDeclaredField(name);
    fld.setAccessible(true);
    Object o = fld.get(c.cast(ds));
    if (type.equalsIgnoreCase("ArrayList"))
    {
      return ((ArrayList)o).toArray();
    }
    if (type.equalsIgnoreCase("EventListenerList"))
    {
      return ((EventListenerList)o).getListenerList();
    }
    return null;
  }
  
//  public Object getValue(Class<?> c, Object o) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
//  Field fld = c.getDeclaredField(name);
//  fld.setAccessible(true);
//  return fld.get(c.cast(o));

}

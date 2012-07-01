package tatoo.db.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;

public class DBSchemaClassPattern extends DBSchemaDefinition {
  
  private HashMap<String, DBSchemaPropertyPattern> fields = new HashMap<String, DBSchemaPropertyPattern>();
  private String tableName;
  private HashMap<String, DBSchemaSetPattern> sets = new HashMap<String, DBSchemaSetPattern>(); 
  
  public DBSchemaClassPattern(Element el)
  {
    this.definitonType = SchemaType.CLASS;
    
    Attribute tableNameAttrib = el.getAttribute("table");
//    if (tableNameAttrib == null)
//      throw someexception
    this.tableName = tableNameAttrib.getValue();
    Attribute classNameAttrib = el.getAttribute("name");
    this.name = classNameAttrib.getValue();
    //über die einzelnen Elemente iterieren:
    for (Element child : (List<Element>)el.getChildren()) {
      if (child.getName().equalsIgnoreCase("property")) {
          DBSchemaPropertyPattern prop = new DBSchemaPropertyPattern(child);
          fields.put(prop.getClassPropertyName(), prop);
        }
        if (child.getName().equalsIgnoreCase("set")){
          DBSchemaSetPattern set = new DBSchemaSetPattern(child);
          this.sets.put(set.getName(), set);
        }
      }
  }
  
  public String getTableName()
  {
    return tableName;
  }
  
  public DBSchemaPropertyPattern getField(String name)
  {
    return fields.get(name);
  }
  
  public Collection<DBSchemaPropertyPattern> getFields(){
    return fields.values();
  }

  public DBSchemaSetPattern getSet(String name) {
    return sets.get(name);
  }
  
  public Collection<DBSchemaSetPattern> getSets() {
    return sets.values();
  }
}

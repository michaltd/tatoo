package tatoo.db.sql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import tatoo.db.sql.DBSchemaDefinition.SchemaType;

/**
 * Represents the database-schema as defined in db_schema.xml
 * 
 * @author mkortz
 * @param <T>
 * 
 */
public class DBSchema {  
  
  /**
   * Enthält die Tabellen als <code>DBSchemaDefinition<code> mit dem Namen als Schlüssel.
   */
  HashMap<String, DBSchemaDefinition> schema = new HashMap<String, DBSchemaDefinition>();

  public DBSchema(String pathToSchema) {
    Document doc = null;
    try {
      SAXBuilder builder = new SAXBuilder();
      doc = builder.build(new File(pathToSchema));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    buildSchema(doc);
  }
  
  /**
   * Baut das Schema aus <code>DBSchemaDefinition</code> nach. 
   * @param doc
   */
  @SuppressWarnings("unchecked")
  private void buildSchema(Document doc) {
    // durch die einzelnen Definitionen iterieren:
    for (Element el : (List<Element>)doc.getRootElement().getChildren()) {
      if (el.getName().equalsIgnoreCase("class")) {
        DBSchemaClassPattern table = new DBSchemaClassPattern(el);
        schema.put( table.getName() , table);
      }
    }    
  }


  /**
   * Gibt für die übergebene Klasse den Namen der Tabelle zurück in welche
   * gespeichert werden soll. <br />
   * Wenn die Klasse nicht im Schema gefunden wird, wird <code>null</code> zurückgegeben.
   * 
   * @param cl Die Klasse zu welcher der Tabellenname geholt werden soll
   * @return Der Tabellenname in welcher die Klasse gespeichert wird
   */
  public String getTableName(Class<?> cl) {
    DBSchemaDefinition def = schema.get(cl.getSimpleName());
    DBSchemaClassPattern table;
    if (def != null && def.getSchemaType() == SchemaType.CLASS)
    {
      table = (DBSchemaClassPattern)def;
      return table.getTableName();
    }
    else
      return null;
    
  }
  
  /**
   * Gibt für die übergebene Klasse die <code>DBSchemaDefinition</code> zurück. Ist für die KLasse keine
   * Definition vorhanden wird <code>null</code> zurückgegeben.
   * @param cl
   * @return
   */
  public DBSchemaDefinition getTable(Class<?> cl){    
    return schema.get(cl.getSimpleName());
  }

}

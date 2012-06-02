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
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Represents the database-schema as defined in db_schema.xml
 * 
 * @author mkortz
 * 
 */
public class DBSchema {

  //Weil die innere Klasse kein enum zulässt muss ich das hier deklarieren
  public enum FieldAccessType {
    ATTRIBUTE,
    METHOD
  };
  
  // the document
  Document doc;

  public DBSchema(String pathToSchema) {
    try {
      SAXBuilder builder = new SAXBuilder();
      doc = builder.build(new File(pathToSchema));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Gibt die Spalten und Felder zurück welche der übergebenen Klasse in der
   * Datenbank zugeordnet sind.
   * 
   * @deprecated viel zu kompliziert und Statisch. Wird ersetzt durch eine
   *             Methode welcheein Array aus Objekten zurückgibt.
   * @param tClass
   *          Die Klasse für welche die Spalten der Datenbank zurück gegeben
   *          werden sollen
   * @return HashMap mit &lt;class.Fieldname,
   *         AbstractHashMap.SimpleEntry&lt;database.Fieldname,
   *         class.Fieldtype&gt;&gt; pairs.
   */
  @SuppressWarnings("unchecked")
  @Deprecated
  public HashMap<String, SimpleEntry<String, String>> getColumnsOf(
      Class<?> tClass) {
    HashMap<String, SimpleEntry<String, String>> map = new HashMap<String, SimpleEntry<String, String>>();
    for (Element clAttrib : (List<Element>) getElement(tClass).getChildren()) {
      if (clAttrib.getName().equalsIgnoreCase("property")) {
        map.put(clAttrib.getAttributeValue("name"),
            new SimpleEntry<String, String>(clAttrib
                .getAttributeValue("column"), clAttrib
                .getAttributeValue("type")));
      }
    }
    return map;
  }

  @SuppressWarnings("unchecked")
  public FieldSchema[] getFieldSchemas(Class<?> tClass) {
    
    ArrayList<FieldSchema> fieldSchemas = new ArrayList<FieldSchema>();
    
    //jedes Element der Tabelle durchgehen und jeweils ein FeldSchema erzeugen
    for (Element fld : (List<Element>) getElement(tClass).getChildren()) {
      
      FieldSchema fldSchema = new FieldSchema();
      
      if (fld.getName().equalsIgnoreCase("property")){
        fldSchema.setAccessType(FieldAccessType.ATTRIBUTE);
        fldSchema.setClassAttributeName(null);
        fldSchema.setSetter(fld.getAttributeValue("setter"));
        fldSchema.setGetter(fld.getAttributeValue("getter"));
      }
      if (fld.getName().equalsIgnoreCase("method")){
        fldSchema.setAccessType(FieldAccessType.METHOD);
        fldSchema.setClassAttributeName(fld.getAttributeValue("name"));
        fldSchema.setSetter(fld.getAttributeValue(null));
        fldSchema.setGetter(fld.getAttributeValue(null));
      }
      
      fldSchema.setDatabaseColumnName(fld.getAttributeValue("column"));
      
      //beim Typ zuerst prüfen ob es sich um eine Tabelle handelt (Fremdschlüssel)
      String fldType = fld.getAttributeValue("type");
      Element el = getElement(fldType);
      try {
        Class.forName(fldType);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
      
      
      
    }
    return null;
    // <property name="MinCount" column="mincount" type="int" />
    // <property name="MinCount" column="mincount" type="entity" />
    // <method getter="getParent" setter="setParent" column="parent" type="int" />
    // <method getter="getParent" setter="setParent" column="parent" type="entity" />
  }

  /**
   * Gibt für die übergebene Klasse den Namen der Tabelle zurück in welche
   * gespeichert werden soll. <br />
   * 
   * @param cl Die Klasse zu welcher der Tabellenname geholt werden soll
   * @return Der Tabellenname in welcher die Klasse gespeichert wird
   */
  public String getTableName(Class<?> cl) {
    Element classElement = getElement(cl);
    if (classElement == null) {
      // throw new classNotFoundInSchemaException();
      return null;
    }
    Attribute table = classElement.getAttribute("table");
    if (table == null) {
      // throw new schemaFileHasWrongFormatException();
      return null;
    }
    return table.getValue();
  }

  /**
   * Holt das Element welches die übergebene Klasse im Schema repräsentiert
   * 
   * @param cl
   *          Die Klasse für die das Element geholt werden soll
   * @return Das Element zur Klasse
   */
  @SuppressWarnings("unchecked")
  private Element getElement(Class<?> cl) {
    for (Element el : (List<Element>) doc.getRootElement().getChildren()) {
      if (elementIsTypeOf(el, cl)) {
        return el;
      }
    }
    return null;
  }
  
  @SuppressWarnings("unchecked")
  private Element getElement(String type) {
    for (Element el : (List<Element>) doc.getRootElement().getChildren()) {
      if (el.getAttributeValue("name").equalsIgnoreCase(type)) {
        return el;
      }
    }
    return null;
  }

  /**
   * Überprüft ob das Element die übergebene Classe repräsentiert. Gibt true
   * zurück wenn das der Fall ist. Ansonsten wird false zurückgegeben.
   * 
   * @param el
   *          Das Element das überprüft werden soll
   * @param cl
   *          Die Klasse auf deren Typ geprüft werden soll
   * @return true wenn das Element vom Typ der Klasse ist, ansonsten false
   */
  private boolean elementIsTypeOf(Element el, Class<?> cl) {
    String classnames = el.getAttributeValue("name");
    classnames = classnames.replaceAll(" ", "");
    boolean containsName = false;
    for (String className : classnames.split(","))
      if (className.equalsIgnoreCase(cl.getSimpleName()))
        containsName = true;
    return el.getName().equalsIgnoreCase("class") && containsName;
  }

  /**
   * @deprecated Sollte nicht benutzt werden. Ich habe es nur gebaut, um einen
   *             schnellen initialen-Aufbau der Schema Datei hinzubekommen.<br>
   *             <b>TODO:</b> Über kurz oder lang muss ich mir noch Gedanken
   *             machen, ob die Schema-Datei auch Automatisch aufgebaut werden
   *             kann. Auch im Hinblick auf die Migrationen, ist es unbedingt
   *             erforderlich mit den Migrationen das Schema der DB anzupassen.
   */
  @Deprecated
  public void addTable(String tableName, String className,
      Hashtable<String, String> keys, Hashtable<String, String> columns) {

    Element classElement = new Element("class");
    classElement.setAttribute("name", className);
    classElement.setAttribute("table", tableName);

    for (String column : columns.keySet()) {
      classElement.addContent(new Element("property").setAttribute("name",
          column).setAttribute("column", column).setAttribute("type",
          columns.get(column)));
    }

    doc.getRootElement().addContent(classElement);

    XMLOutputter outp = new XMLOutputter();
    outp.setFormat(Format.getPrettyFormat());
    // ---- Write the complete result document to XML file ----
    try {
      outp.output(doc, new FileOutputStream(new File(new URI(doc.getBaseURI())
          .getPath())));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

  }

  /**
   * Das Schema eines Feldes aus dem Datenbankschema
   * 
   * @author mkortz
   * 
   */
  public class FieldSchema {

    /** Der Typ des Feldes. Es wird der Java Typ gespeichert. */
    private Class<?> type;
    /** Der Typ des Zugriffs. Kann zwei Werte annehmen: ATTRIBUTE und METHOD */
    private FieldAccessType accType;
    /** Der Name des KlassenAttributes welches durch dieses Schema angesprochen wird. */
    private String classAttributeName = null;
    private String getter = null;
    private String setter = null;
    /** Der Name der Spalte, welche durch dieses Schema angesprochen wird. */
    private String databaseColumnName;

    /**
     * Der Typ des Feldes. Es wird der Java-Typ zurückgegeben.
     * 
     * @return
     */
    public Class<?> getType() {
      return type;
    }

    protected void setType(Class<?> type) {
      this.type = type;
    }

    /**
     * Gibt den Namen des KlassenAttributes zurück, welcher durch dieses Schema
     * angesprochen wird.
     * Ist der AccessType dieses Feldes = METHOD dann wird null zurück gegeben.
     * @return
     */
    public String getClassAttributeName() {
      return classAttributeName;
    }

    protected void setClassAttributeName(String classAttributeName) {
      this.classAttributeName = classAttributeName;
    }

    public String getDatabaseColumnName() {
      return databaseColumnName;
    }

    protected void setDatabaseColumnName(String databaseColumnName) {
      this.databaseColumnName = databaseColumnName;
    }
    
    public FieldAccessType getAccessType() {
      return accType;
    }

    protected void setAccessType(FieldAccessType accType) {
      this.accType = accType;
    }

    public String getGetter() {
      return getter;
    }

    protected void setGetter(String getter) {
      this.getter = getter;
    }

    public String getSetter() {
      return setter;
    }

    protected void setSetter(String setter) {
      this.setter = setter;
    }


  }

}

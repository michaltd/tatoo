package tatoo.db.sql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import tatoo.db.DataManipulation;
import tatoo.db.Dataset;
import tatoo.db.sql.DBSchemaPattern.SchemaType;

/**
 * Implementierung der DataManipulation für die Verbindung mit der SQL-Datenbank H2.
 * @see tatoo.db.DataManipulation
 * @author mkortz
 *
 */
public class SQLDataManipulation extends DataManipulation {

  Connection dbconn;

  private String t_name;
  private String t_class;
  private Hashtable<String, String> t_values = new Hashtable<String, String>();
  private String condition;

  

  public SQLDataManipulation(Connection sqlConnection, DBSchema schema) {
    super(schema);
    dbconn = sqlConnection;
  }

  @Override
  public DataManipulation setTableName(String name) {
    t_name = name;
    return this;
  }

  @Override
  public DataManipulation setCondition(String condition) {
    this.condition = condition;
    return this;
  }

  @Override
  public DataManipulation alterValues(String... columnValues) {
    for (String valString : columnValues) {
      alterValue(valString);
    }
    return this;
  }

  @Override
  public DataManipulation alterValue(String columnValue) {
    String columnSignature[] = columnValue.split("=");
    if (columnSignature.length != 2)
      ;// TODO throw someException
    t_values.put(columnSignature[0], columnSignature[1]);
    return this;
  }

  @Override
  public boolean delete() {
    String sql = new String("DELETE FROM  \"" + t_name + "\" ");
    if (condition.length() > 0)
        sql += "WHERE " + condition;
    boolean result = false;
    try {
      result = execute(sql) > 0 ? true : false;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public boolean delete(Dataset dataset) {
    collectFieldParameters(dataset, dataset.getClass());  
    return delete();
  }

  
  //@Override
  /**
   * Fügt die übergebenen Daten in die Datenbank ein. 
   * Sollte nicht aufgerufen werden wenn das einfügen über die Methode {@link #insert(Dataset)} möglich ist.
   */
  private int insert() {
    if (t_name==null)
      return -1;
    String columnNames = new String("");
    String columnValues = new String("");
    for (String columnName : t_values.keySet()) {
      if (columnNames.length() > 0)
        columnNames += ", ";
      if (columnValues.length() > 0)
        columnValues += ", ";
      columnNames += columnName;
      columnValues += t_values.get(columnName);
    }
    String sql = "";
    if (t_name.equalsIgnoreCase("dataset"))
      sql = new String("INSERT INTO \"dataset\" (type) values ('" + t_class + "');");
    else
      sql = new String("INSERT INTO \"" + t_name + "\" (" + columnNames
          + ") values (" + columnValues + ");");
    int result = -1;
    try {
      result = execute(sql);
    } catch (SQLException e) {
//      // State 42S02 = Table not Found -> Sie muss angelegt werden!
//      if (e.getSQLState() == "42S02")
        
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Fügt das Dataset in die Datenbank ein und vergibt eine eindeutige ID für das Objekt.
   */
  @Override
  public int insert(Dataset dataset) {
    return insert(dataset, dataset.getClass());
  }
  
  /**
   * Fügt das Objekt, welches der übergebenen Klasse entspricht in die Datenbank ein.
   * Da das einzufügende Dataset mitsamt seinen Parents geschrieben werden muss, wird diese
   * rekursive Methode benutzt. Sie ruft sich selbst für jede Oberklasse des übergebenen Objekts aus.
   * @param dataset
   * @param c
   * @return
   */
  private int insert(Dataset dataset, Class<?> c) {
    this.t_class = dataset.getClass().getName();
    int id = dataset.getId();
    
    if (id > 0)
      return id;
    
    Class<?> superclass = c.getSuperclass();
    // solange rekursiv insert(Dataset, Class) mit der Oberklasse aufrufen bis die Klasse Object erreicht ist.
    
    if (superclass != Object.class)
    {
      SQLDataManipulation insertStmt = new SQLDataManipulation(dbconn, schema);
      id = insertStmt.insert(dataset, superclass);
    }
    collectFieldParameters(dataset, c); 
    // wenn die id == 0 ist handelt es sich in jedem Fall um die Dataset-Klasse -> (c.getSimpleName() == "Dataset")
    // wir kommen von Oben und gehen rekursiv in der Klassenhirarchie nach unten zur Wurzel. Die Wurzel ist die Klasse Dataset!
    // dieses Objekt wird also sofort in den Cache geschoben, da es gerade in die DB eingefügt wurde.
    if (id == 0)
    {
      id = insert();
      // die id des Objektes setzen. Geschieht über Reflection weil setId eine private Methode ist.
      try {
        Method setId = c.getDeclaredMethod("setId", int.class);
        setId.setAccessible(true);
        setId.invoke(dataset, id);
      } 
      catch (SecurityException e) {e.printStackTrace();} 
      catch (NoSuchMethodException e) {e.printStackTrace();} 
      catch (IllegalArgumentException e) {e.printStackTrace();} 
      catch (IllegalAccessException e) {e.printStackTrace();} 
      catch (InvocationTargetException e) {e.printStackTrace(); }
//      dataset.setId(id);
      // ein erzeugtes Objekt wird zwecks wiederverwendung (z.B. bei Referenzen darauf) hier in den Cache gelegt
      addToCache(dataset);
    }
    else
    {
      alterValue("dataset_id = " + id); //jede Tabelle (außer das Dataset selbst) bekommt die dataset_id übergeben.
      insert();
    }
    
    // TODO: das folgende auslagern in eine eigene Methode?
    // ist der Datensatz erst mal eingetragen müssen die Sets behandelt werden
    String table = schema.getTableName(c);
    if (table != null)
    {
      DBSchemaPattern def = schema.getTable(c);
      if (def.getSchemaType() == SchemaType.CLASS){
        // die Sets durchlaufen ...
        for (DBSchemaSetPattern set : ((DBSchemaClassPattern)def).getSets()) {
          // ... und die einzelnen Objecte darin ... 
          Object[] objects = {};
          try {
            objects = set.getContent( c , dataset);
          } 
          catch (SecurityException e) {e.printStackTrace();continue;} 
          catch (IllegalArgumentException e) {e.printStackTrace();continue;} 
          catch (NoSuchFieldException e) {e.printStackTrace();continue;} 
          catch (IllegalAccessException e) {e.printStackTrace();continue;}
          
          for (Object item : objects){
            Dataset datset;
            if (Dataset.class.isInstance(item))
            {
              datset = (Dataset)item;
            }
            else
            {
//              objectClass = item.getClass();
              continue;
            }
              
            // ... in die Datenbank sichern.
            DataManipulation insertItem = new SQLDataManipulation(dbconn, schema);
            int itemId = insertItem.insert(datset);
            // ist das Objekt gesichert muss es in die entsprechende set-Tabelle eingetragen werden,
            // um die Verbindung zu dem hier behandelten Datensatz zu schaffen
            SQLDataManipulation insertSet = new SQLDataManipulation(dbconn, schema);
            insertSet.setTableName(set.getTableName());
            insertSet.alterValue( set.getName() + "_id = " + id);
            insertSet.alterValue( schema.getTableName(c) + "_id = " + itemId );
            insertSet.insert();
          }
        }
      }
    }
    
    // TODO: end TODO auslagern oben
    
    // wenn es sich um ein Dataset handelt muss die ID gesetzt werden.
//    if (c == Dataset.class)
//      dataset.setId(id);
    return id;
  }

  
  @Override
  public boolean update() {
    String setStatement = new String("");
    for (String columnName : t_values.keySet()) {
      setStatement += " " + columnName + " = " + t_values.get(columnName) + ", ";
    }
    String sql = new String("UPDATE \"" + t_name + "\" SET " + setStatement.substring(0, setStatement.lastIndexOf(',')));
    if (condition.length() > 0 )
      sql += " WHERE " + condition;
    sql += ";";
    boolean result = false;
    try {
      result = execute(sql) > 0 ? true : false;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public boolean update(Dataset dataset) {
    collectFieldParameters(dataset, dataset.getClass());
    return update();
  }

  /**
   * Führt das übergebene SQL-Statement aus und gibt die erzeugte ID zurück.
   * Wenn keine ID erzeugt wurde, wird -1 zurückgeliefert.
   * @param sqlStatement
   * @return
   * @throws SQLException
   */
  private int execute(String sqlStatement) throws SQLException {
    Statement stmt = dbconn.createStatement();
    stmt.execute(sqlStatement);
    if (stmt.getUpdateCount() == 1){
      ResultSet rslt = stmt.getGeneratedKeys();
      if (rslt.first())
        return rslt.getInt(1);
      else
        return -1;
    }
    if (stmt.getUpdateCount() > 1){
      throw new SQLException("es wurden zu viele Ergebnisse geliefert.");
    }
    return -1;
  }
  
  /**
   * Baut aus dem Dataset die Parameter für das SQL-Statement zusammen. Dabei werden nur die Attribute der 
   * zusätzlich übergebenen Klasse geschrieben.
   * @param dataset das Objekt des Datasets
   * @param class Die Klasse die behandelt werden soll
   */  
  private void collectFieldParameters(Dataset dataset, Class<?> c) {
    // zunächst den Tabellennamen heraussuchen
    t_name = schema.getTableName(c);
    //es kommt vor, dass die Klasse nicht im Schema auftaucht, weil sie selbst keine speichernden Attribute 
    // besitzt. Dann muss hier abgebrochen werden!
    if (t_name == null)
      return;

    DBSchemaPattern def = schema.getTable(c);
    if (def.getSchemaType() != SchemaType.CLASS)
      return;
    for (DBSchemaPattern field : ((DBSchemaClassPattern)def).getFields()) {
      String value = "";
      try {
          // Den Wert aus des Attributs für das Feld aus dem Dataset-Objekt holen
          Object oValue = ((DBSchemaPropertyPattern)field).getValue(c, dataset);
          if (oValue == null) continue;
          // wenn es sich hier wiederum um ein Objekt handelt welches wieder ein Dataset ist, muss
          // dieses zunächst selbst weggeschrieben werden. Ansonsten wird einfach der Wert gesetzt
          // das funktioniert nicht für bidirektionale Beziehungen! In einem solchen Fall dürfte es eine Enlosschleife geben
          // Da muss eventuell in Zukunft etwas mit "static" Listen hier in der Klasse SQLDataManipulation gemacht werden in 
          // der alle instanzen der zu speichernden Objekte Referenziert sind, um doppelte einträge zu vermeiden. 
          // Oder so.... irgendwie .... :)
          if(Dataset.class.isInstance(oValue))
          {
            SQLDataManipulation insertStmt = new SQLDataManipulation(dbconn, schema);
            if (((Dataset)oValue).getId() == 0)
              value = ((Integer)insertStmt.insert((Dataset)oValue)).toString();
            else
              value = ((Integer)((Dataset)oValue).getId()).toString();
          }
          else
          {
            if (oValue.getClass().isEnum() || oValue.getClass().getSuperclass().isEnum())
              value = ((Enum)oValue).name();
            else
              value = oValue.toString();
          }
//        }
        if (oValue.getClass().getSimpleName().equalsIgnoreCase("string") || oValue.getClass().isEnum() || oValue.getClass().getSuperclass().isEnum())
          alterValue(((DBSchemaPropertyPattern)field).getDatabaseFieldName() + " = '" + value + "'");
        else
          alterValue(((DBSchemaPropertyPattern)field).getDatabaseFieldName() + " = " + value);
      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      
    }
  }
  
}

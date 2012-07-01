package tatoo.db.sql;

import java.io.ObjectOutputStream.PutField;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.event.EventListenerList;

import tatoo.db.DBFactory;
import tatoo.db.Dataset;
import tatoo.db.Query;
import tatoo.model.conditions.ConditionListener;
import tatoo.model.entities.RootNode;

public class SQLQuery extends Query {

	private Class<?> t_class = null;
	private DBSchema schema;
	private String t_orderBy;
	private String t_groupBy;
	private String t_condition;

	Connection dbconn;

	public SQLQuery(Connection sqlConnection, DBSchema schema) {
		dbconn = sqlConnection;
		this.schema = schema;
	}

	@Override
	public Query addCondition(String condition) {
		t_condition = condition;
		return this;
	}

	@Override
	public Query get(Class<?> cl) {
		t_class = cl;
		return this;
	}

	@Override
	public Query groupBy(String group) {
		t_groupBy = group;
		return this;
	}

	@Override
	public Query orderBy(String order) {
		this.t_orderBy = order;
		return this;

	}

	@Override
	/**
   * Führt die Abfrage aus. Liefert die gefundenen Objekte zurück. Wenn ein Objekt nicht gefunden wurde,
   * oder ein anderer Fehler auftritt wird <code>null</code> zurück gegeben.
   * @return
   */
	public LinkedList<Dataset> execute() {
		try {
		  String stmt = composeStatement();
			return execute(stmt);
		} catch (SQLException e) {
		  if (!t_class.getSimpleName().equalsIgnoreCase("VersionNumber"))
		    e.printStackTrace();
			return null;
		}
	}
	
	private LinkedList<Dataset> execute(String sqlStatement) throws SQLException {

	  ResultSet results = dbconn.createStatement().executeQuery(sqlStatement);

		LinkedList<Dataset> objects = new LinkedList<Dataset>();
		while (results.next()) {
			Dataset rowObject = null;
			try {
			  Constructor<?> constructor = t_class.getConstructor();
			  constructor.setAccessible(true);
				rowObject = (Dataset)t_class.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
			rowObject = setValuesOfObject(t_class, rowObject, results);
			
			objects.add(rowObject);
		}
		if (objects.isEmpty())
		  return null;
		else
		  return objects;

	}

	/**
	 * Setzt die Werte der einzelnen Attribute des Objektes. 
	 * @param cl
	 * @param rowObject
	 * @param results
	 * @return
	 */
	private Dataset setValuesOfObject(Class<?> cl, Dataset rowObject, ResultSet results) {
	  
	  // Zunächst müssen die Superklassen rekursiv durchlaufen werden. Dazu die superklasse holen, und die Methode setValuesOfObject
	  // damit rekursiv aufrufen bis "Object" erreicht ist.
	  Class<?> superclass = cl.getSuperclass();
	  if (superclass != null)
	    rowObject = setValuesOfObject(superclass, rowObject, results);
	  
	  // Nun die Tabelle aus dem Schema holen ...
	  DBSchemaClassPattern table = (DBSchemaClassPattern)schema.getTable(cl);
	  if (table == null)
	    return rowObject;
	  // .. und zunächst die einzelnen Felder durchlaufen und mit Werten belegen ...
	  for (DBSchemaPropertyPattern field : table.getFields()) {
	    try {
        Field fld = cl.getDeclaredField(field.getName());
        setField(field, fld, rowObject, results);
        // wenn wir im Dataset selbst sind und das feld die dataset_id ist, wird zunächst geschaut ob das Object bereits im 
        // EntityCache vorhanden ist, und wenn nicht nmuss eine Referenz auf das resultierende
        // objekt in den EntityCache geschrieben werden
        if (cl.getSimpleName().equals("Dataset") && field.getName().equals("id")){
          Dataset ds = getFromCache(rowObject.getId());
          if (ds == null)
            addToCache(rowObject);
          else
            return ds;
        }
      } 
	    catch (SecurityException e) { e.printStackTrace();} 
	    catch (NoSuchFieldException e) { e.printStackTrace();}
    }
	  // test test test das objekt auslesen, für debuggingzwecke eingefügt um mir das Objekt im Cache anzusehen
	  Dataset testCache = getFromCache(rowObject.getId());
	// .. und dann die sets durchlaufen und mit Werten belegen.
	  for (DBSchemaSetPattern set : table.getSets()){
        
	    // Die entsprechenden Datensätze aus der dem Set zugeordneten Tabelle holen ...
	    String stmt = "SELECT * FROM \"" + set.getTableName() + "\" WHERE " + set.getName() + "_id = " + rowObject.getId() ;
	    ResultSet resultSet ;
	    ArrayList<Dataset> resultCollection = new ArrayList<Dataset>();
	    try {
        resultSet = dbconn.createStatement().executeQuery(stmt);
        // ... und dann einzeln durchlaufen und Objekte initialisieren
        while(resultSet.next())
        { 
          // die Dataset_Id des zu erzeugenden Objektes holen
//          int newIdReal = resultSet.getInt(1);
          int newId = resultSet.getInt(2);
          // und damit den Typen des zu erzeugenden Objektes heraussuchen
          String stmt2 = "SELECT * FROM \"dataset\" WHERE dataset_id = " + newId ;
          ResultSet datasetRslt = dbconn.createStatement().executeQuery(stmt2);
          datasetRslt.first();
          String newObjType = datasetRslt.getString("type");
          // das Objekt auslesen
          // wenn das objekt bereits im Cache ist daraus holen, ansonsten aus der Datenbank holen
          Dataset obj = getFromCache(newId);
          if (obj == null)
            obj = DBFactory.getInstance().read(Class.forName(newObjType), newId);
          resultCollection.add(obj);
        }
        Object resultCollectionAsObject = resultCollection;
        if (set.getType().equals("EventListenerList"))
        {
          resultCollectionAsObject = new EventListenerList();
          for (Dataset ds : resultCollection)
          {
              ((EventListenerList)resultCollectionAsObject).add(EventListener.class, (EventListener)ds);
          }
          
        }
        Field fld = cl.getDeclaredField(set.getName());
        fld.setAccessible(true);
        fld.set(rowObject, resultCollectionAsObject);
      } 
	    catch (SQLException e) { e.printStackTrace(); } 
	    catch (ClassNotFoundException e) { e.printStackTrace(); } 
	    catch (SecurityException e) {e.printStackTrace();} 
	    catch (NoSuchFieldException e) {e.printStackTrace();} 
	    catch (IllegalArgumentException e) {e.printStackTrace();} 
	    catch (IllegalAccessException e) {e.printStackTrace();}
	    
	  }
    return rowObject;
  }

  private Dataset setField(DBSchemaPropertyPattern field, Field fld, Dataset rowObject, ResultSet results){
    try {
      Object resultObject = null;
      // wenn das Feld vom Typ Dataset ist, dann muss zunächst ein Objekt für diesen Datentyp aus der Datenbank gelesen werden ...
      if (fld.getType().isInstance(Dataset.class))
      {
        // die Methode getLong(String columName) aus dem Resultset aufrufen
        // und für das AktuelleFeld aufrufen
        String columnName = field.getDatabaseFieldName();
        Long datasetId = (Long)results.getLong(columnName);
        //resultObject ist in diesem Fall ein Long, welcher dann für die Initialisierung des neuen Unterobjektes verwendet wird:
        resultObject = DBFactory.getInstance().getConnection().read(fld.getType(), datasetId);
        
      }
      else
        // ... ansonsten kann einfach das Feld ausgelesen und in das erzeugte objekt eingetragen werden
      {          
        String columnName = field.getDatabaseFieldName();
        try {
          //jetzt hab ich ne rekursion! aber das kann man ja ändern :)
          
          resultObject = results.getObject(columnName);
        } catch (SQLException e) {e.printStackTrace();}
      }
      if (resultObject != null)
      {
        fld.setAccessible(true);
        // Wenn das Feld wieder vom Datentyp Dataset ist, muss zunächst das tatsächliche
        // resultObjekt aus der Datenbank geholt werden. Dann muss das resultObject hier aber vom Typ Integer sein,
        // da es nur die ID des eigentlichen Objektes darstellt
        if (!( fld.getType().isPrimitive() || Number.class.isAssignableFrom(fld.getType()) || String.class.isAssignableFrom(fld.getType()) || fld.getType().isEnum()) )
        {
          // zunächst die Dataset-Tabelle abfragen, um an den zu instanziierenden Typ zu kommen
          SQLQuery typeQuery = new SQLQuery(dbconn, schema);
          typeQuery.get(Dataset.class);
          typeQuery.addCondition("dataset_id =" + ((Integer)resultObject).intValue() );
          String stmt = typeQuery.composeStatement();
          ResultSet resultSet = dbconn.createStatement().executeQuery(stmt);
          // und dann das Objekt selbst holen:            
          SQLQuery objektQuery = new SQLQuery(dbconn, schema);
          if (resultSet.first()){
            String className = resultSet.getString("type");
            Class<?> c = Class.forName(className);
            objektQuery.get(c);
            objektQuery.addCondition("\"dataset\".dataset_id=" + ((Integer)resultObject).intValue() );
            resultObject  = objektQuery.execute().getFirst();
          }
        }
        if(fld.getType().isEnum())
        {
           resultObject = Enum.valueOf((Class<Enum>) fld.getType(), resultObject.toString());
          
        }
        fld.set(rowObject, resultObject );
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return rowObject;
	}

  /**
	 * Liefert ein SQL-Statement als String zurück, welches ein JOIN über alle Tabellen welche die Klasse betreffen
	 * macht, und somit alle Felder der Klasse zurückgibt.  
	 */
	private String composeStatement() throws SQLException {
	  
	  String [] stmnt = composeStatement(t_class, "", "");
	  String result = "SELECT " + stmnt[0] + " FROM " + stmnt[1] ;
	  if (t_condition != null)
	    result += " WHERE " + t_condition;
    if (t_groupBy != null)
      result += " group by " + t_groupBy;
    if (t_orderBy != null)
      result += " order by " + t_orderBy;
	  return result + ";";
	}
	
	private String[] composeStatement(Class<?> c, String fieldsStatement, String fromStatement) throws SQLException {
	  
	  Class<?> superclass = c.getSuperclass();
	  String tablename = schema.getTableName(c);
	  String[] result = {fieldsStatement,fromStatement};
	  
	  // Die klasse muss im Schema sein, damit was zurück gegeben werden kann!
    // Wenn sie nicht im Schema ist, ohne etwas zu tun, direkt eine Stufe absteigen:
    if (tablename == null)
    {
      if (superclass == null)
        return result;
      else
        return composeStatement(superclass, fieldsStatement, fromStatement);
    }
    
    for (DBSchemaDefinition field : ((DBSchemaClassPattern)schema.getTable(c)).getFields()) {
      if (result[0].length() > 0) 
        result[0] += ", \"";
      else
        result[0] += " \"";
      result[0] += tablename + "\"." + ((DBSchemaPropertyPattern)field).getDatabaseFieldName() + " ";
      if (tablename.equalsIgnoreCase("dataset"))
        result[0] += ", \"" + tablename + "\".type ";
    }
	  
    if (superclass == null)
    {
      if(result[1].length() == 0){
        result[1] = " \"" + tablename + "\" ";
        return result;
      }
      else
      {
        result[1] += " INNER JOIN \"" + tablename + "\" ON \"" + tablename + "\".Dataset_id = ";
        return result;
      }
    }
    else
    {
      if (result[1].length() == 0){
        result[1] = " \"" + tablename + "\" ";
        String[] stmnt = composeStatement(superclass, result[0], result[1]);
        if (stmnt[1].length() != 0 && !stmnt[1].trim().equalsIgnoreCase("\"dataset\"") )
          result[1] = stmnt[1] + " \"" + tablename + "\".Dataset_id ";
        result[0] = stmnt[0];
        return result;
      }
      else
      {
        String innerJoin = " INNER JOIN ( \"" + tablename + "\" ";
        result[1] += innerJoin;
        String[] stmt = composeStatement(superclass, result[0], result[1]);
        if (stmt[1].endsWith(innerJoin))
        {
          result[1] = result[1].substring(0, result[1].length() - innerJoin.length());
          result[1] += " INNER JOIN \"" + tablename + "\" ON \"" + tablename + "\".Dataset_id = ";
          result[0] = stmt[0];
          return result;
        }
        else
        {
          result[1] = stmt[1] + " \"" + tablename + "\".Dataset_id ) ON \"" + tablename + "\".Dataset_id = "; 
          result[0] = stmt[0];
          return result;
        }
      }
    }
  }
}

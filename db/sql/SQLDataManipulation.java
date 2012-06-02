package tatoo.db.sql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.AbstractMap.SimpleEntry;

import tatoo.db.DataManipulation;
import tatoo.db.Dataset;

public class SQLDataManipulation extends DataManipulation {

  Connection dbconn;

  private String t_name;
  private Hashtable<String, String> t_values = new Hashtable<String, String>();
  private String condition;

  public SQLDataManipulation(Connection sqlConnection, DBSchema schema) {
    dbconn = sqlConnection;
    this.schema = schema;
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
  public DataManipulation addValues(String... columnValues) {
    for (String valString : columnValues) {
      addValue(valString);
    }
    return this;
  }

  @Override
  public DataManipulation addValue(String columnValue) {
    String columnSignature[] = columnValue.split("=");
    if (columnSignature.length != 2)
      ;// TODO throw someException
    t_values.put(columnSignature[0], columnSignature[1]);
    return this;
  }

  @Override
  public int delete() {
    String sql = new String("DELETE FROM  \"" + t_name + "\" WHERE "
        + condition);
    return execute(sql);
  }

  @Override
  public int delete(Dataset dataset) {
    buildFromDataset(dataset);  
    return delete();
  }

  @Override
  public int insert() {
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
    String sql = new String("INSERT INTO \"" + t_name + "\" (" + columnNames
        + ") values (" + columnValues + ");");
    return execute(sql);
  }

  @Override
  public int insert(Dataset dataset) {
    buildFromDataset(dataset);    
    return insert();
  }
  
  @Override
  public int update() {
    String setStatement = new String("");
    for (String columnName : t_values.keySet()) {
      setStatement += " " + columnName + " = " + t_values.get(columnName) + ", ";
    }
    String sql = new String("UPDATE \"" + t_name + "\" SET " + setStatement.substring(0, setStatement.lastIndexOf(',')));
    if (condition.length() > 0 )
      sql += " WHERE " + condition;
    sql += ";";
    return execute(sql);
  }

  @Override
  public int update(Dataset dataset) {
    buildFromDataset(dataset);
    return update();
  }

  /**
   * Sucht zunächst in der übergebenen Klasse nach dem Feld. Wenn das Feld nicht gefunden wird, sucht es in der Elternklasse weiter.
   * Trifft die Methode irgendwann auf die Object Klasse, einen primitiven Datentyp oder ein Interface wirft es eine 
   * NoSuchFieldException
   * @param datasetClass
   * @param column
   * @return
   * @throws NoSuchFieldException
   */
  private Field getField(Class<?> datasetClass, String column) throws NoSuchFieldException {
    if (datasetClass == null) throw new NoSuchFieldException(column);
    Field [] flds = datasetClass.getDeclaredFields();
    for (Field fld : flds){
      if (fld.getName().equalsIgnoreCase(column)){
        return fld;
      }
    }
    return getField(datasetClass.getSuperclass(), column);
  }

  private int execute(String sqlStatement) {
    try {
      Statement stmt = dbconn.createStatement();
      stmt.execute(sqlStatement);
//      if (stmt.execute(sqlStatement)){
//        //Wird das gebraucht??
//        ResultSet rslt = stmt.getResultSet();
//        rslt.first();
//        return rslt.getInt("columnLabel");
//      }
      if (stmt.getUpdateCount() >= 0){
        ResultSet rslt = stmt.getGeneratedKeys();
        rslt.first();
        return rslt.getInt(1);
      }
      return 0;

    } catch (SQLException e) {
      e.printStackTrace();
      return 0;
    }
  }
  
  private void buildFromDataset(Dataset dataset){
    Class<?> datasetClass = dataset.getClass();
    t_name = schema.getTableName(datasetClass);
    HashMap<String, SimpleEntry<String, String>> columns = 
      schema.getColumnsOf(datasetClass);
    for (String column : columns.keySet()){
      String columnName = columns.get(column).getKey();
      String columnType = columns.get(column).getValue();
      String value = "";
      try {
        //veruschen den Wert über die Methode zu holen:
        try {
          Object oValue = datasetClass.getMethod("get" + column).invoke(dataset);
          if (oValue != null)
            value = oValue.toString();
        } catch (InvocationTargetException e) {} catch (NoSuchMethodException e) {}
        if (value.equals("")){
          //wenn das nicht klappt das Feld direkt suchen:
          Field fld = getField(datasetClass, column);
          fld.setAccessible(true);
          Object oValue = fld.get(dataset);
          if (oValue == null) throw new NoSuchFieldException(column);
          value = oValue.toString();
        }
        if (columnType.equalsIgnoreCase("string"))
          addValue(columnName + " = '" + value + "'");
        else
          addValue(columnName + " = " + value);
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
    try {
      Field pk = getField(datasetClass, "id");
      pk.setAccessible(true);
      setCondition(t_name + "_id = " + pk.getLong(dataset));
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

}

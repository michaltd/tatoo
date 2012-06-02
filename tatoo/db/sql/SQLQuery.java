package tatoo.db.sql;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;

import tatoo.db.Query;

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
	public LinkedList<Object> execute() {
		try {
			return execute(composeStatement());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private LinkedList<Object> execute(String sqlStatement) throws SQLException {
		ResultSet results;
		try {
			results = dbconn.createStatement().executeQuery(sqlStatement);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		LinkedList<Object> objects = new LinkedList<Object>();
		while (results.next()) {
			Object rowObject = null;
			try {
			  Constructor<?> constructor = t_class.getConstructor();
			  constructor.setAccessible(true);
				rowObject = t_class.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
			HashMap<String, AbstractMap.SimpleEntry<String, String>> columns = schema.getColumnsOf(t_class);
			for (String column : columns.keySet()) {
				try {
					Field fld = t_class.getDeclaredField(column);
					String columnType = columns.get(column).getValue();
					columnType = columnType.substring(0, 1) .toUpperCase() + columnType.substring(1).toLowerCase();
					Method mthd = ResultSet.class.getMethod("get" + columnType, String.class);
					String columnName = columns.get(column).getKey();
					Object resultObject = mthd.invoke(results, columnName );
					fld.setAccessible(true);
					fld.set(rowObject, resultObject );
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
			}
			objects.add(rowObject);
		}
		return objects;

	}

	private String composeStatement() {
		String sql = new String("SELECT * FROM ");
		sql += "\"" + schema.getTableName(t_class) + "\"";
		if (t_condition != null)
			sql += " where " + t_condition;
		if (t_groupBy != null)
			sql += " group by " + t_groupBy;
		if (t_orderBy != null)
			sql += " order by " + t_orderBy;
		sql += ";";
		return sql;
	}

}

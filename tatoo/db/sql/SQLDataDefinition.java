package tatoo.db.sql;

import java.sql.Connection;
import java.sql.SQLException;

import tatoo.db.DataDefinition;

/**
 * Implementierung der Datadefiniton für die Verbindung mit der SQL-Datenbank
 * H2.
 * 
 * @see tatoo.db.DataDefinition
 * @author mkortz
 */
public class SQLDataDefinition extends DataDefinition {

    protected Connection dbconn;

    public SQLDataDefinition (Connection sqlConnection, DBSchema schema) {
        super (schema);
        dbconn = sqlConnection;
        // this.schema = schema;
    }

    @Override
    protected boolean create () {
        String sqlString = "CREATE TABLE \"" + t_Name + "\" (";
        for (String columnName : t_columns.keySet ()) {
            sqlString += columnName + " " + t_columns.get (columnName) + ",";
        }
        if ( !t_Name.equalsIgnoreCase ("dataset"))
            sqlString += "dataset_id INTEGER, ";
        for (String columnName : t_pk.keySet ()) {
            sqlString += columnName + " " + t_pk.get (columnName) + " AUTO_INCREMENT PRIMARY KEY ";
        }

        sqlString = sqlString.substring (0, sqlString.length () - 1) + ");";

        return execute (sqlString);
    }

    @Override
    protected boolean drop () {
        String sqlString = "DROP TABLE \"" + t_Name + "\";";
        return execute (sqlString);
    }

    protected boolean execute (String sqlStatement) {
        try {
            dbconn.createStatement ().execute (sqlStatement);
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace ();
            return false;
        }
    }

}

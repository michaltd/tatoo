package tatoo.db;

import tatoo.VersionNumber;

/**
 * Eine Migration überführt eine Datenstruktur auf die sie angewendet wird in
 * eine neue Version. Dabei kann es sich sowohl um eine niedrigere Version als
 * auch eine höhere handeln. Je nach Aufgabe wird eine der beiden Methoden
 * {@link Migration#up()} oder {@link Migration#down()} aufgerufen.<br />
 * Eine Migration wird von einer konkreten Migration implementiert, indem die
 * Methoden up(), down() und getVersion() implementiert werden. Diese beiden
 * Methoden sollten die Migrations-Methoden createTable, alterTable und
 * dropTable aufrufen. Es kann jedoch beliebiger Code eingefügt werden.<br />
 * Eine Migratin muss sicherstellen, dass die Datenstruktur nach dem Aufruf von
 * up() und down() - oder andersherum erst down() dann up() - die selbe Struktur
 * hat wie vor der Ausführung. Werden z.B. Datensätze bei up() hinzugefügt,
 * müssen diese mit down() wieder entfernt werden.
 * 
 * Ein Beispiel könnte folgendermaßen aussehen. <br />
 * <pre>
 * {@code 
    public class createTables extends Migration {
    
    public void up() {
      createTableWithID("someTable", "someField:INTEGER", "someName:VARCHAR");
      createTable("someOtherTable", "someOtherField:Integer", "AnotherField:Integer" );
    }
    
    public void down() {
      dropTable("someTable");
      dropTable("someOtherTable");
    }
    
    public VersionNumber getVersion() {
      return new VersionNumber(2010);
    }
  }

 * } </pre> 
 * Eine Migration verfügt selbst über eine {@link VersionNumber}. Diese wird mit
 * der Migration in die Datenstruktur geschrieben. So kann nachvollzogen werden
 * welche Migrationen schon ausgeführt wurden und welche nicht.
 * 
 * @author mkortz
 * 
 */
public abstract class Migration implements Comparable<Migration> {

  /**
   * 
   */
  private DBConnection dbConn = DBFactory.getInstance().getConnection();

  /**
   * Erzeugt eine Tabelle mit einer Automatisch hochzählenden ID. Die ID hat
   * immer den Namen der Tabelle mit einem angehängten "_id".
   * 
   * @param name
   *          Der Name der zu erzeugenden Tabelle.
   * @param columns
   *          Die Spalten der Tabelle. Die Spalten müssen in der Form
   *          "spaltenname:spaltentyp" übergeben werden, ansonsten wird eine
   *          {@link DataDefinitionException} geworfen.
   *          @see #createTable(String, String...)
   */
  protected void createTableWithID(String name, String... columns) {

    DataDefinition dd = priv_createTable(name, columns);
    dd.addPrimaryKey(name + "_id:INTEGER");
    dd.create();

  }

  /**
   * Erzeugt eine Tabelle. 
   * 
   * @param name
   *          Der Name der zu erzeugenden Tabelle.
   * @param columns
   *          Die Spalten der Tabelle. Die Spalten müssen in der Form
   *          "spaltenname:spaltentyp" übergeben werden, ansonsten wird eine
   *          {@link DataDefinitionException} geworfen.
   */
  protected void createTable(String name, String... columns) {
    priv_createTable(name, columns).create();

  }

  /**
   * Erzeugt eine Tabelle. Wird nur intern benutzt für DRY-Programmierung.
   * 
   * @see Migration#createTable(String, String...)
   */
  private DataDefinition priv_createTable(String name, String... columns) {
    DataDefinition dd = dbConn.createDataDefinition();
    dd.setTableName(name);
    dd.addColumns(columns);
    return dd;
  }

  /**
   * Löscht eine Tabelle aus der Datenstruktur
   * 
   * @param name
   *          Der Name der Tabelle welche gelöscht werden soll.
   */
  protected void dropTable(String name) {
    DataDefinition dd = dbConn.createDataDefinition();
    dd.setTableName(name);
    dd.drop();
  }

  /**
   * führt die Methode <code>down()</code> aus und löscht die Versionsnummer aus
   * der Datenstruktur.
   */
  // TODO: prüfen ob die migration überhaupt erlaubt ist. Beispiel: es sind noch
  // Migrationen vor dieser durchzuführen
  // oder die Datenbank hat eine niedrigere Version als diese Migration.
  public void migration_down() {
    down();
    DataManipulation dm = dbConn.createDataManipulation();
    dm.setTableName("migration_schema");
    dm.setCondition("version = " + getVersion());
    dm.delete();

  }

  /**
   * führt die Methode <code>up()</code> aus und fügt die Versionsnummer in die
   * Datenstruktur ein.
   */
  // TODO: prüfen ob die migration überhaupt erlaubt ist. Beispiel: es sind noch
  // Migrationen vor dieser durchzuführen
  // oder die Datenbank hat eine höhere Version als diese Migration.
  public void migration_up() {
    up();
    DBFactory.getInstance().write(getVersion());
  }

  @Override
  public int compareTo(Migration m) {
    return (int) (getVersion().compareTo(m.getVersion()));
  }

  /**
   * Überführt eine Datenstruktur in eine höhere Version.
   */
  public abstract void up();

  /**
   * Überführt eine Datenstruktur in eine niedrigere Version.
   */
  public abstract void down();

  /**
   * Gibt die Versionsnummer der Migration zurück.
   * 
   * @return die Versionsnummer der Migration
   */
  public abstract VersionNumber getVersion();

}

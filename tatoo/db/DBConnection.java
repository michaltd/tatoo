package tatoo.db;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedList;
import tatoo.Tatoo;
import tatoo.VersionNumber;
import tatoo.db.sql.DBSchema;

/**
 * Die Verbindung welche für das Verbinden mit einer Datenstruktur benutzt wird.
 *
 * @author mkortz
 *
 */
public abstract class DBConnection
{
	// TODO: Dieses Enum muss noch umgebaut werden,
	// so dass die Treiber auch per String aus einer config datei geladen werden
	// können
	/**
	 * Stellt die verschiedenen möglichen Datenverbindungen dar.
	 *
	 * @author mkortz
	 */
	public enum DBases
	{
		/**
		 * Verbindung zur Datenbank H2, welche als Server auf localhost läuft.
		 */
		H2Server("org.h2.Driver", "jdbc:h2:tcp://localhost/~/workspace/tatoo/db/tatoo", "/tatoo/db/sql/db_schema.xml"),
		/**
		 * Verbindung zur Datenbank H2
		 */
		H2("org.h2.Driver", "jdbc:h2:./db/tatoo", "/tatoo/db/sql/db_schema.xml"),
		/**
		 * Verbindung zu einer XML-Struktur
		 */
		XML("tatoo.model.xmlConnector", "", "");

		private final String driverString;
		private final String connectionString;
		private final String pathToSchemaFile;
		private final String USER = "tatoo";
		private final String PASSWD = "";

		private DBases(String driverString, String connectionString, String pathToSchemaFile)
		{
			this.driverString = driverString;
			this.connectionString = connectionString;
			this.pathToSchemaFile = pathToSchemaFile;
		}

		/**
		 * Liefert den Treiberstring zurück. Dieser wird für die Verbindung mit
		 * einer SQL-Datenbank benötigt, um den Treiber zu laden.
		 *
		 * @return String für das laden des Datenbanktreibers.
		 */
		public String getDriverString()
		{
			return driverString;
		}

		/**
		 * Gibt zurück wo die Verbindung zur Datenstruktur aufgebaut werden kann.
		 *
		 * @return Der Connection-String
		 */
		public String getConnectionString()
		{
			return connectionString;
		}

		/**
		 * Gibt den Benutzernamen zurück der für die Verbindung gebraucht wird.
		 *
		 * @return Der Benutzername
		 */
		public String getUSER()
		{
			return USER;
		}

		/**
		 * Gibt das Passwort zurück, welches für die Verbindung gebraucht wird.
		 *
		 * @return das Passwort
		 */
		public String getPASSWD()
		{
			return PASSWD;
		}

		/**
		 * Gibt den Pfad zurück an dem die Schemadatei für die Verbindung liegt.
		 *
		 * @return Den Pfad zur Schema-Datei
		 */
		public InputStream getPathToSchema()
		{
			return Tatoo.class.getResourceAsStream(this.pathToSchemaFile);
		}
	}

	/**
	 * Das Datenbankschema welches für die Verbindung benutzt wird.
	 */
	protected DBSchema schema;
	/**
	 * Die Datenbank welche für die Verbindung benutzt wird.
	 */
	protected DBases dbase;

	/**
	 * Baut eine Verbindung zur Datenstruktur auf.
	 *
	 * @return True wenn die Verbindung aufgebaut werden kann. False sonst.
	 */
	public abstract boolean connect();

	/**
	 * Schließt die Verbindung zur Datenstruktur.
	 *
	 * @return True wenn das Schließen geklappt hat. False sonst.
	 */
	public abstract boolean close();

	/**
	 * Liefert ein neues Query-Objekt zurück.
	 *
	 * @return Ein neues Query-Objekt
	 */
	protected abstract Query createQuery();

	/**
	 * Liefert ein neues DataManipulation-Objekt zurück.
	 *
	 * @return Ein neues DataManipulation-Objekt
	 */
	protected abstract DataManipulation createDataManipulation();

	/**
	 * Liefert ein neues DataDefinition-Objekt zurück.
	 *
	 * @return Ein neues DataDefinition-Objekt
	 */
	protected abstract DataDefinition createDataDefinition();

	/**
	 * Schreibt das übergebene Objekt o in die darunter liegende Datenstruktur.
	 * Ein Objekt, welches sich bereits in der Datenbank befindet, wird lediglich
	 * angepasst, alle anderen neu geschrieben. Datasets erhalten nach den
	 * Schreibvorgang ihre ID.
	 *
	 * @param o
	 *          Das Objekt das geschrieben werden soll.
	 * @return gibt true zurück wenn das schreiben geklappt hat. False sonst.
	 */
	public final boolean write(Dataset o)
	{
		// 1. prüfen ob Objekt bereits eingetragen ist ...
		// ... dazu zunächst prüfen ob die ID des objektes > 0 ist. Wenn die id == 0
		// ist das objekt neu
		// und noch nicht in der DB eingetragen
		if (o.getId() > 0)
		{
			// 2. wenn ja: Objekt anpassen
			return createDataManipulation().update(o);
		}
		else
		{
			// 3. wenn nein: Objekt eintragen
			int id = createDataManipulation().insert(o);
			try {
				Method setId = Dataset.class.getDeclaredMethod("setId", int.class);
				setId.setAccessible(true);
				setId.invoke(o, id);
				return true;
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		// 4. schon fertig :)
		return false;
	}

	/**
	 * Liest ein Objekt der übergebenen Klasse mit der angegebenen id aus der
	 * Datenstruktur.
	 *
	 * @param c
	 *          Die Klasse welche gelesen werden soll
	 * @param id
	 *          Die Id des zu lesenden Objektes
	 * @return Das gelesene Objekt. Kann das Objekt nicht gelesen werden wird
	 *         <code>null</code> zurückgegeben.
	 */
	public final Dataset read(Class<?> c, long id)
	{
		if (connect())
		{
			LinkedList<Dataset> results = createQuery().get(c).addCondition("\"dataset\".dataset_id=" + id).execute();

			if (results == null || results.isEmpty())
			{
				return null;
			}
			else
			{
				return results.getFirst();
			}
		}
		return null;
	}

	/**
	 * Alle anstehenden Migrationen durchführen.
	 */
	public void migrate()
	{
		LinkedList<Migration> migrationList = getMigrationList();
		System.out.println(migrationList);
		if (migrationList != null && migrationList.size() > 0)
		{
			migrate(migrationList.getLast().getVersion());
		}
	}

	/**
	 * Führt die Migrationen bis zur übergebenen Version durch. Dabei kann es sich
	 * entweder um eine kleinere als die derzeitige Version handeln, oder um eine
	 * größere.
	 *
	 * @param toVersion
	 *          Die Version auf die Migriert werden soll.
	 */
	public void migrate(VersionNumber toVersion) {

		// Migrationen holen
		LinkedList<Migration> migrations = getMigrationList();

		// Datenbankversion holen
		VersionNumber dbVersion = new VersionNumber(1);
		try {
			// sollte die Tabelle VersionNumber noch nicht existieren, muss mit
			// dbVersion 1 gestartet werden
			LinkedList<Dataset> sqlResults = createQuery().get(VersionNumber.class).orderBy("version").execute();

			if (sqlResults != null)
				dbVersion = (VersionNumber) sqlResults.getLast();
		} catch (ClassCastException ex) {
			ex.printStackTrace();
			return;
		}

		// wenn die dbVersion größer ist als toVersion muss ein migrate_down
		// ausgeführt werden:
		if (dbVersion.compareTo(toVersion) > 0)
		{
			// alle migrationen entfernen die kleiner sind als die aktuelle toVersion
			while (migrations.getFirst().getVersion().compareTo(toVersion) < 0)
			{
				migrations.removeFirst();
			}

			// und alle migrationen entfernen die größer sind als die dbVersion
			while (migrations.getFirst().getVersion().compareTo(dbVersion) > 0)
			{
				migrations.removeLast();
			}
			migrate_down(migrations);
		}
		else if (dbVersion.compareTo(toVersion) < 0)
		{
			while (migrations.getFirst().getVersion().compareTo(dbVersion) <= 0)
			{
				migrations.removeFirst();
			}

			while (migrations.getFirst().getVersion().compareTo(toVersion) > 0)
			{
				migrations.removeLast();
			}

			migrate_up(migrations);
		}
	}

	/**
	 * Führt die Methode <code>migration_up</code> der übergebenen Migrationen
	 * aus. Wenn eine der Migrationen eine Versionsnummer hat, welche über der
	 * aktuellen Versionsnummer liegt, wird eine MigrationInvocationException
	 * ausgelöst.
	 *
	 * @param migrations
	 *          Liste von auszuführenden Migrationen.
	 */
	private void migrate_up(LinkedList<Migration> migrations)
	{
		for (Migration m : migrations)
		{
			if (m.getVersion().compareTo(Tatoo.VERSION) > 0)
			{
				throw new MigrationInvocationException("VersionNumber of Migration less than actual VersionNumber");
			}

			m.migration_up();
		}
	}

	/**
	 * Führt die Methode <code>migration_down</code> der übergebenen Migrationen
	 * aus. Wenn eine der Migrationen eine Versionsnummer hat, welche über der
	 * aktuellen Versionsnummer liegt, wird eine MigrationInvocationException
	 * ausgelöst.
	 *
	 * @param migrations
	 *          Liste von auszuführenden Migrationen.
	 */
	private void migrate_down(LinkedList<Migration> migrations) {
		for (Migration m : migrations)
		{
			if (m.getVersion().compareTo(Tatoo.VERSION) > 0)
			{
				throw new MigrationInvocationException("VersionNumber of Migration less than actual VersionNumber");
			}

			m.migration_down();
		}
	}

	/**
	 *
	 * Gibt eine Liste aller Migrationen aus dem Migrationen-Verzeichnis als eine
	 * aufsteigend nach versionsnummer sortierte Liste von {@link Migration}
	 * objekten zurück.
	 *
	 * @return Liste von Migrationen
	 */
	private LinkedList<Migration> getMigrationList()
	{
		File migrationDir;
		try {
			migrationDir = new File(Tatoo.class.getResource("/tatoo/db/migrate").toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			migrationDir = null;
		}
		//    File migrationDir = new File("./tatoo/db/migrate");
		if (migrationDir != null)
		{
			migrationDir.exists();
			LinkedList<Migration> migrationList = new LinkedList<Migration>();
			// TODO muss noch auf list(FileNameFilter f ) umgestellt werden.
			for (File f : migrationDir.listFiles()) {
				String fileName = f.getName();

				if (!fileName.endsWith(".java"))
					continue;

				fileName = "tatoo.db.migrate." + fileName.substring(0, fileName.length() - 5);
				Migration migration = null;

				try {
					migration = (Migration) Class.forName(fileName).newInstance();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				if (migration != null) {
					migrationList.add(migration);
				}
			}
			Collections.sort(migrationList);
			return migrationList;
		}
		else
		{
			return null;
		}
	}

}
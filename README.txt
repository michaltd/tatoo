Diese Datei ist dazu gedacht erste wichtige Hinweise zu geben was beim Kompilieren, ausführen und erstellen der Dokumentation für 
tatoo beachtet werden muss.

Um Tatoo zu kompilieren müssen die externen Libarys unter tatoo.extlib in den Buildpath eingebunden
werden. 
Unter Exlipse kann das geschehen indem die Projektproperties geöffnet werden:
Menü "Project" -> Properties -> Java Buildpath
Dort dann unter Libaries die externen Libaries mit "Add External JARs" eingebunden werden.

Tatoo ist nahezu komplett per javadoc dokumentiert. Um diese Dokumentation zu erzeugen wird javadoc im Verzeichnis tatoo
folgendermaßen aufgerufen. Bitte das <directory> im ersten Parameter durch ein entsprechendes Verzeichnis ersetzen, in das die resultierende Dokumentation geschrieben werden soll.
javadoc -overview overview.html -d <directory> -private -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 tatoo.db tatoo.model.entities tatoo.model tatoo.db.sql tatoo.view.armyBuilder tatoo.view tatoo.model.entities.events tatoo.view.armyList tatoo.model.entities.attributesetters tatoo.db.migrate tatoo tatoo.resources tatoo.model.conditions

Um tatoo zu starten MUSS derzeit noch der Parameter "up" angefügt sein. Das hat mit der Datenbank zu tun, es werden alle Migrationen
durchgeführt.
In Eclipse kann man in den Projekt-Settings eine neue Launch Konfiguration anlegen (z.B. "tatoo up") und in diese die Parameter automatisch 
beim Start übergeben lassen.
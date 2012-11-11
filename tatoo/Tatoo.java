package tatoo;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import tatoo.db.DBFactory;
import tatoo.view.MainWindow;

/**
 * Einstiegspunkt für tatoo. Enthält die main Methode und wichtige globale
 * Objekte. Setzt ausserdem das LookAndFeel von Swing.
 * 
 * @author mkortz
 */
public class Tatoo {

    /**
     * Enthält die aktuelle Versionsnummer. Finale Konstante, welche vor dem
     * Kompilieren des Nightly Builds mal automatisch gesetzt werden soll...
     * oder so
     */
    public final static VersionNumber VERSION = new VersionNumber ("00.2712.00");
    /**
     * Enthält das tatoo-Object
     */
    private static Tatoo              tatoo;
    // Die DBFactory sollte nur einmal initialisiert werden, weil dann auch
    // Schema nur einmal eingelesen wird usw.
    // Es handelt sich hier also um ein globales Objekt. Nicht schön, aber
    // performanter als jedesmal beim Datenbankzugriff
    // alles neu zu initialisieren.
    /**
     * Referenz auf die globale DBFactory.
     * 
     * @see tatoo.db.DBFactory
     */
    private DBFactory                 dbfact;

    /**
     * Einsprungspunkt für tatoo. Instantiiert Tatoo und führt
     * {@link Tatoo#init(String[])} aus.
     * 
     * @param args
     */
    public static void main (String[] args) {
        if (tatoo == null) {
            tatoo = new Tatoo ();
        }

        tatoo.init (args);
    }

    /**
     * Führt benötigte Initialisierungen aus. Instantiiert die {@link DBFactory}
     * , setzt das Look and Feel mittels {@link #setLookAndFeel()} und ruft das
     * Hauptfenster mittels
     * {@link tatoo.view.MainWindow#createAndShowGUI(VersionNumber))} auf.
     * 
     * @param args
     */
    private void init (String[] args) {
        // Datenbank initialisieren und ggf. updaten
        dbfact = DBFactory.getInstance ();

        if (args.length > 0 && args[0].equals ("up")) {
            dbfact.getConnection ().migrate ();
        }
        else {
            dbfact.getConnection ().migrate (new VersionNumber (1));
            return;
        }

        // lookAndFeel setzen:
        setLookAndFeel ();
        // Hauptfenster öffnen
        MainWindow.createAndShowGUI (VERSION);
    }

    /**
     * setzt das Look and Feel von Swing. Versucht zunächst das Nimbus LAF zu
     * setzen. Wenn das fehlschlägt wird das native LAF ausprobiert und beim
     * nochmaligen Fehlschlag das garantiert vorhandene Motif LAF.
     */
    private void setLookAndFeel () {
        try {
            setNimbusLookAndFeel ();
        }
        catch (Exception e) {
            System.err.println ("Error setting Nimbus LAF: " + e + "\n Trying native LAF!");
            try {
                UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ());
            }
            catch (Exception ex) {
                System.err.println ("Error setting native LAF: " + ex + "\n Falling back to Motif LAF!");
                setMotifLookAndFeel ();
            }
        }
    }

    /**
     * Setzt das Motif-LAF
     */
    private void setMotifLookAndFeel () {
        try {
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        }
        catch (Exception e) {
            System.out.println ("Error setting Motif LAF: " + e);
            System.exit (1);
        }
    }

    /**
     * Versucht das Nimbus LAF zu setzen.
     * 
     * @throws Exception
     * Wenn das Nimbus LAF nicht gesetzt werden kann wird diese Exception
     * geworfen.
     */
    private void setNimbusLookAndFeel () throws Exception {
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels ()) {
            if ("Nimbus".equals (info.getName ())) {
                UIManager.setLookAndFeel (info.getClassName ());
                break;
            }
        }
    }
}
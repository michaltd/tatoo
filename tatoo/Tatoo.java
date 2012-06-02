package tatoo;

import tatoo.db.DBFactory;
import tatoo.view.MainWindow;

public class Tatoo {

  private static Tatoo tatoo;
  public static VersionNumber VERSION;
  private static final String versionString = "00.0711.00";

  /**
   * @param args
   */
  public static void main(String[] args) {
    if (tatoo == null)
      tatoo = new Tatoo();
    if (VERSION == null)
      VERSION = new VersionNumber(versionString);
    tatoo.init(args);
  }

  private void init(String[] args) {
    // Datenbank initialisieren und ggf. updaten
//    DBFactory dbfact = DBFactory.getInstance();
//    if (args[0].equals("up"))
//      dbfact.getConnection().migrate();
//    else
//      dbfact.getConnection().migrate(0);

   // addWarboss();
    
    // MainWindow aufbauen
     MainWindow.createAndShowGUI(VERSION);
  }
  

}

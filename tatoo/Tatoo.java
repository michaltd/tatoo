package tatoo;

import tatoo.db.DBFactory;
import tatoo.model.entities.RootNode;
import tatoo.view.MainWindow;

public class Tatoo {

  private static Tatoo tatoo;
  public static VersionNumber VERSION;
  private static final String versionString = "00.2612.00";
  // sollte nur einmal initialisiert werden, weil dann auch Schema nur einmal eingelesen wird usw. Im Endeffekt macht das
  // einmalige öffnen tatoo schneller
  private DBFactory dbfact;

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
    dbfact = DBFactory.getInstance();
    if (args[0].equals("up"))
      dbfact.getConnection().migrate();
    else
    {
      dbfact.getConnection().migrate(0);
      return;
    }
    
//    RootNode armeeliste = (RootNode)dbfact.read(RootNode.class,4);
    // MainWindow aufbauen
//    if (armeeliste == null)
      MainWindow.createAndShowGUI(VERSION);
  }
  

}

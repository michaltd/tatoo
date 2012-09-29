package tatoo.view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import tatoo.ArmyListInstanceSidePanel;
import tatoo.VersionNumber;
import tatoo.model.ArmyListModel;
import tatoo.model.entities.AbstractEntity;
import tatoo.resources.TextWrapper;
import tatoo.view.armyBuilder.ArmyBuilderPanel;
import tatoo.view.armyList.ArmyListPanel;

//TODO: Fehler log ins Programm verlegen. Sprich: ich brauche eine Umleitung der
// Standardausgabe (Fehlerausgabe) in eine von mir kontrollierte Konsole (Textfeld)

/**
 * Main Window and Container of tatoo
 * @author mkortz
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame implements ActionListener, ItemListener {

  /** contains all the Components of the Gui */
  private Container contentPanel = this.getContentPane();
  /** Serves the actual language */
  private TextWrapper textWrapper;

  /**
   * Constructor for the Main Window
   * @param name The name of the Main Window
   */
  private MainWindow(String name) {
    super(name);
    //initialize the language which is schown in tatoo
    textWrapper = new TextWrapper(TextWrapper.Language.LANG_EN);
  }

  /**
   * Adds the Menu bar to the Main Window
   * Build the menu and add it to the Main Window
   * @param frame The Frame where the Menu is added
   */
  private void addMenuBarToPane(final JFrame frame) {
    JMenuBar menuBar = new JMenuBar();

    JMenu showMenu = new JMenu("Anzeigen");
    menuBar.add(showMenu);

    JMenuItem showArmyList = new JMenuItem("Armeelisten Generator");
    showArmyList.setName("showArmyList");
    //TODO: warum muss this lauschen?? Wie wäre es mit einer inline klasse?
    showArmyList.addActionListener(this);
    showMenu.add(showArmyList);

    JMenuItem showArmyBuilder = new JMenuItem("Codex Generator");
    showArmyBuilder.setName("showArmyBuilder");
    showArmyBuilder.addActionListener(this);
    showMenu.add(showArmyBuilder);

    showMenu.addSeparator();
    JMenuItem closeItem = new JMenuItem("Schließen");
    closeItem.setName("closeFrame");
    closeItem.addActionListener(this);
    closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
    showMenu.add(closeItem);

    frame.setJMenuBar(menuBar);
  }


  //TODO: nur zu Testzwecken eingefügt. wieder entfernen
  private AbstractEntity armylist = new ArmyListInstanceSidePanel().armeeliste ;
  /**
   * Shows the ArmyList in the Main Window
   */
  public void showArmyList() {
  	addComponentsToPane(new ArmyListPanel(armylist));
  }

  /**
   * Shows the ArmyBuilder in the Main Window
   */
  public void showArmyBuilder() {
//TODO: Zu testzwecken deaktiviert....
//    ArmyBuilderPanel armyTree = new ArmyBuilderPanel(
//    		new ArmyListModel(new ArmyListInstanceSidePanel().armeeliste));
//TODO: und durch das hier ersetzt:
    ArmyBuilderPanel armyTree = new ArmyBuilderPanel(
      new ArmyListModel(armylist));
    addComponentsToPane(armyTree);
  }

  /**
   * Close the Main Window
   */
  public void closeFrame() {
    System.exit(0);
  }

  //TODO: übersetzung enthält
  /**
   * Method to add Components to the Compoonent-Panel.
   * Ensures, that the Panel is empty before adding new components. This Method should only called once
   * with another Panel, which "enthält" the components to show.
   * @param component the components to add
   */
  private void addComponentsToPane(JComponent component) {
	  //überlegen ob es ausreicht hier die Referenz des contentPanels einfach neu zu setzen:
	  //contentPanel = component;
    if (contentPanel.getComponents().length > 0) contentPanel.removeAll();
    contentPanel.add(component);
    component.revalidate();
  }

  /**
   * Get an appropriate Windowmanager and then create the GUI and show it.
   */
  //TODO: so aktualisieren, dass übergeben werden kann mit welchem "Fenster" die GUI aufgebaut werden soll?
  public static void createAndShowGUI(VersionNumber version) {

    // Create and set up the window.
    MainWindow frame = new MainWindow("TabletopOrganisation - Version " + version );
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.addMenuBarToPane(frame);
    // Set up the content pane.
    //TODO muss noch ersetzt werden, durch den zuletzt angezeigten bzw. gespeicherten
    frame.showArmyBuilder();
//    frame.showArmyList();
    // Display the window.
    frame.setSize(new Dimension(1024, 800));
    frame.setResizable(true);
    frame.setVisible(true);
  }

	@Override
  /**
   * Listens to the Actions from the Menu.
   * Is called if an Menuitem is selected and calls the apropriate Method.
   */
  //TODO: nicht sehr schön das aufrufen anhand von MenuItem-Namen. Noch mal überarbeiten?
  // eventuell ist das hier eh obsolet, da das beim zusammenbau des menus schon anders laufen soll.
  // siehe Bemerkung dort.
  public void actionPerformed(ActionEvent e) {

    JMenuItem item = null;
    try {
      item = (JMenuItem) e.getSource();
    } catch (ClassCastException cce) {
      System.err.println(cce.getStackTrace());
      return;
    }
    try {
      this.getClass().getMethod(item.getName()).invoke(this);
    } catch (SecurityException e1) {
      e1.printStackTrace();
    } catch (NoSuchMethodException e1) {
      e1.printStackTrace();
    } catch (IllegalArgumentException e1) {
      e1.printStackTrace();
    } catch (IllegalAccessException e1) {
      e1.printStackTrace();
    } catch (InvocationTargetException e1) {
      e1.printStackTrace();
    }
  }

  //TODO: wann aufgerufen und wofür?
  /**
   * Don't know what this is for ;P
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    System.out.println("ItemEvent");
  }
}

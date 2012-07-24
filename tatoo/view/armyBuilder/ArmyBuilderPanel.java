package tatoo.view.armyBuilder;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import tatoo.model.ArmyListModel;
import tatoo.model.EntityModel;
import tatoo.model.entities.ArmyListEntity;
import tatoo.model.entities.events.EntityModelEvent;


@SuppressWarnings("serial")
public class ArmyBuilderPanel extends JPanel implements ActionListener {

  JTree tree = null;
  JPanel sidebar = null;
  ArmyListModel treeModel = null;
  EntityModel entityModel = null;

  private enum treeTableCommand {

    ADD_ITEM("Add Item"),
    REMOVE_ITEM("Remove Item"),
    ADD_OR_UPGRADE("Add Upgrade(OR)"),
    ADD_AND_UPGRADE("Add Upgrade(AND)"),
    REMOVE_UPGRADE("Remove Upgrade");

    private final String text;

    private treeTableCommand(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }

    public String getCommand() {
      return (new Integer(ordinal()).toString());
    }
  }

  /**
   * creates a new ArmyBuilder Panel with the given model
   * @param model the model which holds the values that should be shown
   */
  public ArmyBuilderPanel(ArmyListModel model) {
    this.setLayout(new BorderLayout());
    this.treeModel = model;
    treeModel.addTreeModelListener(new PrivTreeModelHandler());
    createBuilderPane(model);
    entityModel = new EntityModel();
    sidebar = new JPanel();
    add(sidebar, BorderLayout.EAST);
    setSidePane();
    tree.getSelectionModel().addTreeSelectionListener(
        new PrivTreeSelectionHandler());
    createPopupMenu();
  }

  private void createBuilderPane(ArmyListModel model) {
  	//create a new tree
  	//Override the method to get the value of the node as text
    tree = new JTree(model) {

      @Override
      public String convertValueToText(Object value, boolean selected,
          boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value != null) {
          String sValue = new EntityModel(value).getName();
          if (sValue != null) { return sValue; }
        }
        return "";
      }
    };
    tree.setEditable(true);
    tree.setRootVisible(true);

    JScrollPane pane = new JScrollPane(tree);
    this.add(pane, BorderLayout.CENTER);

  }
  
  public void createPopupMenu() {
    JMenuItem menuItem;

    JPopupMenu popup = new JPopupMenu();

    menuItem = new JMenuItem(treeTableCommand.ADD_ITEM.getText());
    menuItem.setActionCommand(treeTableCommand.ADD_ITEM.getCommand());
    menuItem.addActionListener(this);
    popup.add(menuItem);
    menuItem = new JMenuItem(treeTableCommand.REMOVE_ITEM.getText());
    menuItem.setActionCommand(treeTableCommand.REMOVE_ITEM.getCommand());
    menuItem.addActionListener(this);
    popup.add(menuItem);
    popup.addSeparator();
    menuItem = new JMenuItem(treeTableCommand.ADD_OR_UPGRADE.getText());
    menuItem.setActionCommand(treeTableCommand.ADD_OR_UPGRADE.getCommand());
    menuItem.addActionListener(this);
    popup.add(menuItem);
    menuItem = new JMenuItem(treeTableCommand.ADD_AND_UPGRADE.getText());
    menuItem.setActionCommand(treeTableCommand.ADD_AND_UPGRADE.getCommand());
    menuItem.addActionListener(this);
    popup.add(menuItem);
    menuItem = new JMenuItem(treeTableCommand.REMOVE_UPGRADE.getText());
    menuItem.setActionCommand(treeTableCommand.REMOVE_UPGRADE.getCommand());
    menuItem.addActionListener(this);
    popup.add(menuItem);

    MouseListener popupListener = new PopupListener(popup);
    tree.addMouseListener(popupListener);
  }

  //TODO: Warum existiert das so in dieser Form?? Eigentlich sollte es doch auch inline gehen?
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(treeTableCommand.ADD_ITEM.getCommand())) {
      Object o = tree.getLastSelectedPathComponent();
      Object entity = treeModel.insertNewEntityInto(o);
      Object[] path = treeModel.getTreePathTo(entity);
      tree.startEditingAtPath(new TreePath(path));
    }
    else if (e.getActionCommand().equals(
        treeTableCommand.REMOVE_ITEM.getCommand())) {
      Object o = tree.getLastSelectedPathComponent();
      treeModel.removeNodeFromParent(o);
    }
    else if (e.getActionCommand().equals(
        treeTableCommand.ADD_OR_UPGRADE.getCommand())) {

    }
    else if (e.getActionCommand().equals(
        treeTableCommand.ADD_AND_UPGRADE.getCommand())) {

    }
    else if (e.getActionCommand().equals(
        treeTableCommand.REMOVE_UPGRADE.getCommand())) {

    }
  }

  public void setSidePane() {
    int sidebarWidth = 300;
    Object o = entityModel.getSource();
    if (o == null) {
      JPanel pane = new JPanel();
      pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
      pane.add(Box.createHorizontalStrut(sidebarWidth));
      sidebar.add(pane);
    }
    else {
//TODO was ist das für ein Blödsinn, das View kennt die Daten? Neeee... noch mal überarbeiten!
      if (o instanceof ArmyListEntity) {
        if (!(sidebar.getComponent(0) instanceof EntityEditPane)) {
          sidebar.removeAll();
          sidebar.add(new EntityEditPane(entityModel));
        }
      }
      sidebar.revalidate();
    }
  }


  class PopupListener extends MouseAdapter {

    JPopupMenu popup;

    PopupListener(JPopupMenu popupMenu) {
      popup = popupMenu;
    }
    public void mousePressed(MouseEvent e) {maybeShowPopup(e);}

    public void mouseReleased(MouseEvent e) {
      if (e.getButton() == MouseEvent.BUTTON3) setSelection(e.getPoint());
      maybeShowPopup(e);
    }

    private void setSelection(Point p) {
      int row = tree.getRowForLocation(p.x, p.y);
      if (row < 0) tree.removeSelectionInterval(0, tree.getRowCount());
      else tree.setSelectionInterval(row, row);
    }
    private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
         popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  private class PrivTreeSelectionHandler implements TreeSelectionListener {

    @Override
    public void valueChanged(TreeSelectionEvent e) {
      Object o = e.getPath().getLastPathComponent();
      entityModel.setSource(o);
      setSidePane();
    }

  }

  private class PrivTreeModelHandler implements TreeModelListener {

    @Override
    public void treeNodesChanged(TreeModelEvent e) {
      EntityModelEvent em = new EntityModelEvent(e.getSource(), "name");
      entityModel.fireAttribChanged(em);
    }

    @Override
    public void treeNodesInserted(TreeModelEvent e) {
    // nothing to do here
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {
    // nothing to do here
    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {
    // nothing to do here
    }

  }

}

package tatoo.view.armyBuilder;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

import tatoo.Tatoo;
import tatoo.commands.CmdTreeSelectionChanged;
import tatoo.db.DBFactory;
import tatoo.db.Dataset;
import tatoo.model.ArmyBuilderEntityModel;
import tatoo.model.ArmyListEntityModel;
import tatoo.model.ArmyListModel;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.Game;
import tatoo.model.entities.AbstractEntity.EntityType;
import tatoo.model.entities.events.EntityModelEvent;
import tatoo.view.TatooPanel;

@SuppressWarnings ("serial")
public class ArmyBuilderPanel extends JPanel implements ActionListener {

    JTree                  tree           = null;
    JPanel                 sidebar        = null;
    ArmyListModel          treeModel      = null;
    ArmyBuilderEntityModel entityModel    = null;
    EntityEditPane         entityEditPane = null;

    private enum treeTableCommand {

        ADD_ITEM ("Add Item", EntityType.NODE),
        REMOVE_ITEM ("Remove Item", null);

        private final String text;

        private treeTableCommand (String text, EntityType type) {
            this.text = text;
        }

        public String getText () {
            return text;
        }

        public String getCommand () {
            return (new Integer (ordinal ()).toString ());
        }
    }

    /**
     * creates a new ArmyBuilder Panel with the given model
     *
     * @param model
     * the model which holds the values that should be shown
     */
    public ArmyBuilderPanel (ArmyListModel model) {
        this.setLayout (new BorderLayout ());
        treeModel = model;
        treeModel.addTreeModelListener (new PrivTreeModelHandler ());
        createBuilderPane (treeModel);
        entityModel = new ArmyBuilderEntityModel ();
        sidebar = new TatooPanel ();
        add (sidebar, BorderLayout.EAST);
        setSidePane ();
        tree.getSelectionModel ().addTreeSelectionListener (new PrivTreeSelectionHandler ());
        createPopupMenu ();
    }

    private void createBuilderPane (ArmyListModel model) {
        // create a new tree
        // Override the method to get the value of the node as text
        tree = new JTree (model) {

            @Override
            public String convertValueToText (Object value, boolean selected, boolean expanded, boolean leaf, int row,
                            boolean hasFocus) {
                if (value != null) {
                    String sValue = new ArmyListEntityModel (value).getName ();
                    if (sValue != null) { return sValue; }
                }
                return "";
            }
        };
        tree.setEditable (true);
        tree.setRootVisible (true);

        JScrollPane scrollPane = new JScrollPane (tree);

        JPanel containerPanel = new TatooPanel ();
        containerPanel.setLayout (new BorderLayout ());
        containerPanel.add (scrollPane, BorderLayout.CENTER);

        Dataset[] games = DBFactory.getInstance ().read (Game.class);
        ArrayList <String> gameNames = new ArrayList <String> (games.length);
        for (Dataset g : games)
            gameNames.add (((Game) g).getName ());
        Collections.sort (gameNames, new StringIgnoreCaseComparator ());
        JComboBox gamesList = new JComboBox (gameNames.toArray ());
        gamesList.setEditable (true);
        containerPanel.add (gamesList, BorderLayout.NORTH);

        this.add (containerPanel, BorderLayout.CENTER);

        gamesList.addActionListener (new ActionListener () {

            @Override
            public void actionPerformed (ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource ();
                String val = (String) cb.getSelectedItem ();
                Dataset[] games = DBFactory.getInstance ().read (Game.class);
                for (Dataset g : games)
                    if (((Game) g).getName ().equalsIgnoreCase (val)) {
                        AbstractEntity rootEntity = (AbstractEntity) ((Game) g).getGameEntity ();
                        treeModel = new ArmyListModel (rootEntity);
                        treeModel.addTreeModelListener (new PrivTreeModelHandler ());
                        tree.revalidate ();
                        tree.setModel (treeModel);
                        //createBuilderPane (treeModel);
                    }
            }
        });

    }

    private void createPopupMenu () {
        if (treeModel.getRoot () != null) {
            JMenuItem menuItem;

            JPopupMenu popup = new JPopupMenu ();

            menuItem = new JMenuItem (treeTableCommand.ADD_ITEM.getText ());
            menuItem.setActionCommand (treeTableCommand.ADD_ITEM.getCommand ());
            menuItem.addActionListener (this);
            popup.add (menuItem);
            menuItem = new JMenuItem (treeTableCommand.REMOVE_ITEM.getText ());
            menuItem.setActionCommand (treeTableCommand.REMOVE_ITEM.getCommand ());
            menuItem.addActionListener (this);
            popup.add (menuItem);

            MouseListener popupListener = new PopupListener (popup);
            tree.addMouseListener (popupListener);
        }
    }

    // TODO: Warum existiert das so in dieser Form?? Eigentlich sollte es doch
    // auch inline gehen?
    @Override
    public void actionPerformed (ActionEvent e) {
        if (e.getActionCommand ().equals (treeTableCommand.ADD_ITEM.getCommand ())) {
            Object o = tree.getLastSelectedPathComponent ();
            Object entity = treeModel.insertNewEntity (o);
            Object[] path = treeModel.getTreePathTo (entity);
            tree.startEditingAtPath (new TreePath (path));
        }
        else if (e.getActionCommand ().equals (treeTableCommand.REMOVE_ITEM.getCommand ())) {
            Object o = tree.getLastSelectedPathComponent ();
            treeModel.removeNodeFromParent (o);
        }
    }

    public void setSidePane () {
        if (entityEditPane == null) {
            entityEditPane = new EntityEditPane (entityModel);
            sidebar.add (entityEditPane);
        }
    }

    class PopupListener extends MouseAdapter {

        JPopupMenu popup;

        PopupListener (JPopupMenu popupMenu) {
            popup = popupMenu;
        }

        public void mousePressed (MouseEvent e) {
            maybeShowPopup (e);
        }

        public void mouseReleased (MouseEvent e) {
            if (e.getButton () == MouseEvent.BUTTON3)
                setSelection (e.getPoint ());
            maybeShowPopup (e);
        }

        private void setSelection (Point p) {
            int row = tree.getRowForLocation (p.x, p.y);
            if (row < 0)
                tree.removeSelectionInterval (0, tree.getRowCount ());
            else tree.setSelectionInterval (row, row);
        }

        private void maybeShowPopup (MouseEvent e) {
            if (e.isPopupTrigger ()) {
                popup.show (e.getComponent (), e.getX (), e.getY ());
            }
        }
    }

    private class PrivTreeSelectionHandler implements TreeSelectionListener {

        @Override
        public void valueChanged (TreeSelectionEvent e) {
            Tatoo.cmdMgr.execute (new CmdTreeSelectionChanged (tree.getSelectionModel (), e.getOldLeadSelectionPath (), tree.getSelectionPath ()));
            Object o = null;
            if (tree.getSelectionPath () != null)
                o = tree.getSelectionPath ().getLastPathComponent ();
            entityModel.setSource (o);
            setSidePane ();
        }

    }

    private class PrivTreeModelHandler implements TreeModelListener {

        @Override
        public void treeNodesChanged (TreeModelEvent e) {
            EntityModelEvent em = new EntityModelEvent (e.getSource (), null);
            entityModel.fireAttribChanged (em);
        }

        @Override
        public void treeNodesInserted (TreeModelEvent e) {
            // nothing to do here
        }

        @Override
        public void treeNodesRemoved (TreeModelEvent e) {
            // nothing to do here
        }

        @Override
        public void treeStructureChanged (TreeModelEvent e) {
            // nothing to do here
        }

    }

    private class StringIgnoreCaseComparator implements Comparator <String> {

        @Override
        public int compare (String o1, String o2) {
            return o1.compareToIgnoreCase (o2);
        }

    }

}

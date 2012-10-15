package tatoo.view.armyList;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import tatoo.model.ArmyListModel;
import tatoo.model.OneOfUpgradeModel;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.AbstractEntity.EntityType;
import tatoo.view.AbstractNodePanel;
import tatoo.view.NodePanelFactory;
import tatoo.view.TatooPanel;

@SuppressWarnings ("serial")
public class ArmyTreePanel extends JPanel implements MouseListener {

    ArmyListModelHandler modelHandler = new ArmyListModelHandler ();
    ArmyListModel        model        = null;
    JPanel               treePanel;
    ArmyTreePanel        thisPane;

    public ArmyTreePanel (ArmyListModel model, String target) {

        thisPane = this;
        treePanel = new TatooPanel ();
        treePanel.setLayout (new BorderLayout ());
        this.model = model;
        model.addTreeModelListener (modelHandler);

        buildPanel (this.model.getRoot (), treePanel, new NodePanelFactory (target));

        thisPane.add (treePanel);
    }

    /**
     * Stellt das JPanel für den Knoten zusammen. Durchläuft alle Unterknoten,
     * wenn welche existieren, und baut so das JPanel.
     * 
     * @param node
     *            Der Knoten für den das JPanel zusemmengestelltwerden soll.
     * @param panel
     *            Das Eltern-JPanel auf dem die Knoten dargestellt werden
     *            sollen.
     */
    public void buildPanel (Object node, JPanel panel, NodePanelFactory nodeFactory) {
        buildPanel (new ArrayList <OneOfUpgradeModel> (), null, node, panel, nodeFactory);
    }

    /**
     * Stellt das JPanel für den Knoten zusammen. Private rekursiv aufgerufene
     * Methode.
     * 
     * @param oneOfUpgradeModel
     *            TODO
     * @param parentPanel
     *            das ElternPanel auf dem die Knoten dargestellt werden sollen.
     * @param node
     *            Der darzustellende Knoten.
     * @param panel
     *            Das Panel auf dem der übergebene Knoten dargestellt wird.
     * @param nodeFactory
     *            Die Factory welche das Panel für den Knoten baut.
     */
    private void buildPanel (ArrayList <OneOfUpgradeModel> oneOfUpgradeModel, AbstractNodePanel parentPanel,
                    Object node, JPanel panel, NodePanelFactory nodeFactory) {
        // Jede Knotenart (AbstractEntity.EntityType) kann unterschiedliche
        // vorgefertigte JPanels haben.
        // Zunächst das entsprechende Panel für den konkreten Knoten holen ...
        AbstractNodePanel nodePane = (AbstractNodePanel) nodeFactory.getNodePanel (node);
        // ... und dieses Panel als MouseListener hinzufügen.
        nodePane.addMouseListener (this);
        // Im falle eines "OneOfUpgrade"s das entsprechende Model erzeugen

        // TODO: unschön! Das muss hier vermutlich alles noch mal überarbeitet
        // werden!
        // im Moment fällt mir keine elegante Lösung ein.
        for (OneOfUpgradeModel loopModel : oneOfUpgradeModel) {
            loopModel.addEntity (node, nodePane);
        }
        OneOfUpgradeModel myModel = null;
        if (model.getTypeOfNode (node) == EntityType.ONEOFUPGRADE) {
            myModel = new OneOfUpgradeModel ();
            oneOfUpgradeModel.add (myModel);
        }

        // Ein JPanel erstellen, welches vor dem JPanel für die eigentliche
        // Knotendarstellung auftaucht. Dieses zusammen mit dem JPanel für den
        // Knoten in einen übergeordneten Container legen.
        JPanel beforeChild = new TatooPanel ();
        JPanel containerPane = new TatooPanel ();
        containerPane.setLayout (new BorderLayout ());
        containerPane.add (beforeChild, BorderLayout.WEST);
        containerPane.add (nodePane, BorderLayout.CENTER);
        panel.add (containerPane, BorderLayout.CENTER);

        // Erzeugt das Panel für den Konkreten Knoten. Das Panel ist dafür
        // selbst zuständig.
        nodePane.createNodePanel (nodePane, model.getChildCount (node) > 0);

        // Holt sich das Panel auf dem die Kindsknoten dargestellt werden sollen
        JPanel childPanel = nodePane.getChildNodePanel ();
        Object childNode;
        // Geht jeden Kindsknoten durch und baut rekursiv deren Panel zusammen
        for (int i = 0; i < model.getChildCount (node) && nodePane.showChilds (); i++ ) {
            childNode = model.getChild (node, i);
            ((AbstractEntity) childNode).addEntityListener (nodePane.getModel ());
            buildPanel (oneOfUpgradeModel, nodePane, childNode, childPanel, nodeFactory);
        }
        oneOfUpgradeModel.remove (myModel);
        if (parentPanel != null)
            parentPanel.childInserted (nodePane, beforeChild, model.getChildCount (node) > 1);
    }

    @Override
    public void mouseClicked (MouseEvent e) {
        processMouseEvent (e);
    }

    @Override
    public void mouseEntered (MouseEvent e) {
        processMouseEvent (e);
    }

    @Override
    public void mouseExited (MouseEvent e) {
        processMouseEvent (e);
    }

    @Override
    public void mousePressed (MouseEvent e) {
        processMouseEvent (e);
    }

    @Override
    public void mouseReleased (MouseEvent e) {
        processMouseEvent (e);
    }

    private class ArmyListModelHandler implements TreeModelListener {

        @Override
        public void treeNodesChanged (TreeModelEvent e) {}

        @Override
        public void treeNodesInserted (TreeModelEvent e) {
            JPanel pane = new TatooPanel ();
            buildPanel (model.getRoot (), pane, new NodePanelFactory ("ArmyList"));
            thisPane.removeAll ();
            thisPane.add (pane);
            thisPane.revalidate ();
        }

        @Override
        public void treeNodesRemoved (TreeModelEvent e) {}

        @Override
        public void treeStructureChanged (TreeModelEvent e) {}

    }

}

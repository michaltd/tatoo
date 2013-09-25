package tatoo.model;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import tatoo.Tatoo;
import tatoo.commands.CmdInsertEntityChild;
import tatoo.commands.CmdRemoveEntityChild;
import tatoo.model.conditions.Condition.ConditionTypes;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.AbstractEntity.EntityType;
import tatoo.model.entities.ArmyListEntity;
import tatoo.model.entities.EntityListener;
import tatoo.resources.TextWrapper;

/**
 * Model für die Armeeliste. Das Model stellt eine triviale Implementierung des
 * Interfaces ArmyModel bereit.
 * 
 * @author mkortz
 */
public class ArmyListModel implements ArmyModel, EntityListener {

    /** Wurzelelement der Armeeliste */
    private AbstractEntity    game         = null;
    /** Liste von Listenern */
    private EventListenerList listenerList = new EventListenerList ();

    /**
     * Instantiiert einen neuen ArmyListEntity-Baum mit root als Wurzelknoten.
     * 
     * @param root
     * Die Wurzel des Baumes
     */
    public ArmyListModel () {
        
    }
    
    public ArmyListModel (AbstractEntity entity){
        setRoot (entity);
    }

    private void setEntityListener (AbstractEntity entity) {
        for (AbstractEntity ae : entity.getChilds ()) {
            ae.addEntityListener (this);
            setEntityListener (ae);
        }
    }

    /**
     * Löst ein TreeNodesChangedEvent aus.
     * 
     * @param e
     * Das Event welches an die Listener übergeben wird.
     */
    public void fireTreeNodesChanged (TreeModelEvent e) {
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged (e);
            }
        }
    }

    /**
     * Löst ein TreeNodesInsertedEvent aus.
     * 
     * @param e
     * Das Event welches an die Listener übergeben wird.
     */
    public void fireTreeNodesInserted (TreeModelEvent e) {
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted (e);
            }
        }
    }

    /**
     * Löst ein TreeNodesRemovedEvent aus.
     * 
     * @param e
     * Das Event welches an die Listener übergeben wird.
     */
    // TODO: noch mal überarbeiten, so dass nicht das Event übergeben wird,
    // sondern eine Referenz auf das auslösende Objekt.
    public void fireTreeNodesRemoved (TreeModelEvent e) {
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved (e);
            }
        }
    }

    /**
     * Löst ein TreeStructureChangedEvent aus.
     * 
     * @param e
     * Das Event welches an die Listener übergeben wird.
     */
    public void fireTreeStructureChanged (TreeModelEvent e) {
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged (e);
            }
        }
    }

    @Override
    public void addTreeModelListener (TreeModelListener l) {
        listenerList.add (TreeModelListener.class, l);
    }

    @Override
    public void removeTreeModelListener (TreeModelListener l) {
        listenerList.remove (TreeModelListener.class, l);
    }

    /**
     * Gibt den Kindsknoten an Position <code>index</code> des Knotens
     * <code>parent</code> zurück.
     * 
     * @param parent
     * Der Knoten dessen Kindsknoten zurückgegeben werden soll.
     * @param index
     * Der index des Kindsknotens, der zurückgegeben werden soll.
     * @return Den Kindknoten mit dem Index <code>index</code> als Objekt.
     */
    public Object getChild (Object parent, int index) {
        return ((AbstractEntity) parent).getEntityAt (index);
    }

    @Override
    public int getChildCount (Object parent) {
        return ((AbstractEntity) parent).getChildCount ();
    }

    @Override
    public int getIndexOfChild (Object parent, Object child) {
        if (parent == null || child == null)
            return -1;
        return ((AbstractEntity) parent).getIndexOf ((AbstractEntity) child);
    }

    @Override
    public Object getRoot () {
        return game;
    }

    public void setRoot (AbstractEntity e) {
        if (e != null) {
            game = e;
            game.addEntityListener (this);
            setEntityListener (game);
            AbstractEntity arr[] = {e};
            fireTreeStructureChanged (new TreeModelEvent (e, arr));
        }
    }

    public EntityType getTypeOfNode (Object node) {
        AbstractEntity ae = (AbstractEntity) node;
        return ae.getType ();
    }

    @Override
    public boolean isLeaf (Object node) {
        return ((AbstractEntity) node).isLeaf ();
    }

    @Override
    public void valueForPathChanged (TreePath path, Object newValue) {
        AbstractEntity changedEntity = (AbstractEntity) path.getLastPathComponent ();
        changedEntity.setName (newValue.toString ());

        int[] idx = {getIndexOfChild (((AbstractEntity) changedEntity).getParent (), changedEntity)};
        Object[] o = {changedEntity};

        TreeModelEvent e = new TreeModelEvent (this, path, idx, o);
        fireTreeNodesChanged (e);

    }

    /**
     * Gibt den Pfad zum dem übergebenen Knoten zurück.
     * 
     * @param o
     * Der Knoten dessen Pfad ermittelt werden soll.
     * @return Den Pfad zum Knoten als ein Array von Objects.
     */
    public Object[] getTreePathTo (Object o) {
        return getNodePath ((AbstractEntity) o, 0).toArray ();
    }

    /**
     * Gibt den Knotenpfad zurück. Ruft sich selbst rekursiv auf. Wird es von
     * aussen aufgerufen muss immer 0 als levelCount übergeben werden.
     * 
     * @param e
     * Das ArmyListEntity dessen Pfad zurückgegeben werden soll
     * @param levelCount
     * die Baumtiefe in der sich der gesuchte Knoten befindet. Wird von außen
     * immer mit 0 aufgerufen!
     * @return den Pfad des Baumes als ArrayList
     */
    // TODO: hmmm wieso existiert hier levelCount?? so viel schneller wird die
    // Methode dadurch nicht.
    private ArrayList <Object> getNodePath (AbstractEntity e, int levelCount) {
        ArrayList <Object> o;
        AbstractEntity ent = (AbstractEntity) e;
        if (ent.getParent () == null)
            o = new ArrayList <Object> (levelCount);
        else o = getNodePath (ent.getParent (), ++levelCount);
        o.add (e);
        return o;
    }

    /**
     * Fügt dem Entitybaum einen weiteren leeren Knoten hinzu
     * 
     * @param parent
     * Der Knoten dem ein Knoten angehängt werden soll.
     * @return gibt den angehängten Knoten zurück
     */
    // TODO: throw ClassCastException?
    public AbstractEntity insertNewEntity (Object parent) {
        // wenn das übergebene Objekt null ist muss eine neue Armeeliste
        // angelegt werden
        EntityType type;
        if (parent == null) {
            type = EntityType.ARMYLIST;
            parent = game;
        }
        else type = EntityType.UPGRADE;
        // create new ArmyListEntity with the given Name from language-file
        AbstractEntity newEntity = new ArmyListEntity (type, TextWrapper.getString ("ArmyListModel.0"));
        newEntity.addEntityListener (this);

        Tatoo.cmdMgr.execute (new CmdInsertEntityChild ((AbstractEntity) parent, newEntity));
        return newEntity;
    }

    @Override
    public AbstractEntity insertCopyOf (Object[] treePath) {

        AbstractEntity orignalRoot = game;
        // Wenn der Root in der Armeeliste noch keine Kinder hat, handelt es
        // sich um den Standardmäßig eingefügten Knoten, welcher
        // nur eingefügt wurde weil es sonst in der Anzeige zu Exceptions kommt.
        // Er muss also ersetzt
        // werden durch den ersten Knoten im TreePath als neue Wurzel.
        if (((AbstractEntity) getRoot ()).getChildCount () == 0 && treePath.length > 0) {
            // Kopie erzeugen:
            try {
                game = ((AbstractEntity) treePath[0]).cloneFor (null);
                game.addEntityListener (this);
            }
            catch (CloneNotSupportedException e) {
                e.printStackTrace ();
                game = orignalRoot;
                return null;
            }

        }
        if (game == null)
            return null;
        // das erste Object im Array MUSS der root-Knoten sein, sonst ist es
        // kein gültiger Pfad!
        // TODO: hier eventuell Exception schmeißen bei ungültigem Pfad.
        if (treePath[0].equals (getRoot ()) && treePath.length > 1) {
            boolean found = false;
            AbstractEntity treeNode = game;
            AbstractEntity parent = treeNode;
            // den Pfad vom Knoten eins bis Ende durchlaufen
            for (int i = 1; i < treePath.length - 1; i++ ) {
                // prüfen ob der Knoten schon enthalten ist
                // aber nur wenn es sich nicht um das Pfadblatt handelt.
                // wenn der Knoten nicht enthalten ist: anlegen
                found = false;
                for (int j = 0; j < treeNode.getChildCount () && !found; j++ ) {
                    if (treeNode.getEntityAt (j).equals ((AbstractEntity) treePath[i])) {
                        found = true;
                        parent = treeNode;
                        treeNode = treeNode.getEntityAt (j);
                    }
                }
                if ( !found) {
                    AbstractEntity newNode;
                    try {
                        newNode = ((AbstractEntity) treePath[i]).cloneFor (parent);
                    }
                    catch (CloneNotSupportedException e) {
                        e.printStackTrace ();
                        game = orignalRoot;
                        return null;
                    }
                    newNode.addEntityListener (this);
                    newNode.setParent (parent);
                    Tatoo.cmdMgr.add (new CmdInsertEntityChild (treeNode, newNode));
                    parent = treeNode;
                    treeNode = newNode;
                }
            }
            // den letzten Knoten auf jeden Fall hinzufügen
            // (hier darf nicht mehr auf equals getestet werden)
            AbstractEntity newNode;
            try {
                newNode = ((AbstractEntity) treePath[treePath.length - 1]).cloneFor (parent);
            }
            catch (CloneNotSupportedException e) {
                e.printStackTrace ();
                game = orignalRoot;
                return null;
            }
            newNode.addEntityListener (this);
            newNode.setParent (parent);
            Tatoo.cmdMgr.add (new CmdInsertEntityChild (treeNode, newNode));
            Tatoo.cmdMgr.execute ();
            return newNode;
        }
        return null;
    }

    /**
     * Entfernt den übergebenen Knoten aus dem Baum.
     * 
     * @param node
     * Der Knoten der entfernt werden soll
     */
    public void removeNodeFromParent (Object node) {
        AbstractEntity delEntity = (AbstractEntity) node;
        AbstractEntity parent = delEntity.getParent ();
        Tatoo.cmdMgr.execute (new CmdRemoveEntityChild (parent, delEntity));
    }

    @Override
    public void AttribChanged (AbstractEntity entity, ConditionTypes attrib) {

    }

    @Override
    public void ChildInserted (AbstractEntity entity, AbstractEntity child) {
        Object[] childArr = {child};
        int[] idx = {getIndexOfChild (entity, child)};
        TreeModelEvent e = new TreeModelEvent (this, getNodePath (entity, 0).toArray (), idx, childArr);
        fireTreeNodesInserted (e);

    }

    @Override
    public void ChildRemoved (AbstractEntity entity, AbstractEntity child, int index) {
        Object[] childArr = {child};
        int[] idx = {index};
        TreeModelEvent e = new TreeModelEvent (this, getNodePath (entity, 0).toArray (), idx, childArr);
        fireTreeNodesRemoved (e);
    }

}

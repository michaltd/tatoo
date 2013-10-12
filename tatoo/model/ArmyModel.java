package tatoo.model;

import javax.swing.tree.TreeModel;

import tatoo.model.entities.AbstractEntity;

/**
 * Model für die Armeeliste. Das ArmyModel erweitert das TreeModel im
 * wesentlichen um zwei Methoden zum Einfügen und Entfernen von Knoten.
 *
 * @author mkortz
 */
public interface ArmyModel extends TreeModel {

    /**
     * Gibt den Pfad zu dem per Parameter übergebenen Knoten-Objekt als Array
     * von Knoten-Objekten zurück. Das zurückgegebene Array enthält am Index 0
     * den Root-Knoten des Baumes, welcher den Knoten o enthält. An Index 1 ist
     * das Knoten-Objekt enthalten, welches auf dem Pfad zum Knoten o im Baum
     * Level 1 entspricht usw. bis der Knoten o erreicht ist.
     *
     * @param o
     * Der Knoten von welchem der Pfad zurück gegeben werden soll.
     * @return Array von Knoten-Objekten
     */
    public abstract Object[] getTreePathTo (Object o);

    /**
     * Fügt einen neuen Knoten in den Baum ein. Wird kein parent-Objekt als
     * Parameter übergeben, wird der neue Knoten direkt an den Root Knoten
     * angehängt. Ansonsten an das übergebene Parent-Objekt.
     *
     * @param parent
     * Der Parent-Knoten an den das neue Objekt angehängt werden soll
     * @return das neue Element
     */
    public abstract AbstractEntity insertNewEntity (Object parent);

    /**
     * Inserts a node in the tree and returns this node. Compare each node of
     * the treePath to the existing nodes in the Tree. If the nodes on the Path
     * don't exist they whill be inserted. However the last node in TreePath
     * (Leaf) will be inserted although an equal node exists. Comparing is made
     * with the {@link #equals(Object)} method of AbstractEntity. Note that this
     * Method DON'T compare the ID of the Datasets. If treePath[0] is not equal
     * to the root node of the tree of this model or the node could not be
     * inserted, nothing happens and the return value is null.
     *
     * @param treePath
     * Path to the node inserted
     * @return the node inserted
     */
    public abstract AbstractEntity insertCopyOf (Object[] treePath);

    /**
     * Entfernt einen Knoten aus dem Baum. Entfernt auch alle Kinder, falls der
     * Knoten Kinder hat.
     *
     * @param node
     * Der zu entfernende Knoten.
     */
    public abstract void removeNodeFromParent (Object node);

}
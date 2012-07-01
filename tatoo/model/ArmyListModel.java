package tatoo.model;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import tatoo.TextWrapper;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.Entity;


/**
 * Model für die Armeeliste. Das Model stellt eine triviale Implementierung des
 * Interfaces ArmyModel (TreeModel) bereit.
 * 
 * @author mkortz
 * 
 */
public class ArmyListModel implements ArmyModel {

	/** root Element of the Armylist */
  private AbstractEntity armyList = null;
  /** List of Listeners */
  private EventListenerList listenerList = new EventListenerList();

  /**
   * create an new Entity-tree whith root as root.
   * @param root the root of the tree.
   */
  public ArmyListModel(AbstractEntity root) {
    armyList = root;
  }

  /**
   * fire a TreeNodesChanged Event
   * @param e
   */
  //TODO: noch mal überarbeiten, so dass nicht das Event übergeben wird, 
  //sondern eine Referenz auf das auslösende Objekt.

  public void fireTreeNodesChanged(TreeModelEvent e) {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TreeModelListener.class) {
        ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
      }
    }
  }

  /**
   * fire a treeNodesInserted Event
   * @param e
   */
  //TODO: noch mal überarbeiten, so dass nicht das Event übergeben wird, 
  //sondern eine Referenz auf das auslösende Objekt.
  public void fireTreeNodesInserted(TreeModelEvent e) {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TreeModelListener.class) {
        ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
      }
    }
  }

  /**
   * fire a treeNodesRemoved Event
   * @param e
   */
  //TODO: noch mal überarbeiten, so dass nicht das Event übergeben wird, 
  //sondern eine Referenz auf das auslösende Objekt.
  public void fireTreeNodesRemoved(TreeModelEvent e) {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TreeModelListener.class) {
        ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
      }
    }
  }
  
  /**
   * fire a treeStructureChanged Event
   * @param e
   */
  //TODO: noch mal überarbeiten, so dass nicht das Event übergeben wird, 
  //sondern eine Referenz auf das auslösende Objekt.
  public void fireTreeStructureChanged(TreeModelEvent e) {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TreeModelListener.class) {
        ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
      }
    }
  }

  /**
   * add a Listener to the model
   */
  @Override
  public void addTreeModelListener(TreeModelListener l) {
    listenerList.add(TreeModelListener.class, l);
  }

  @Override
  public void removeTreeModelListener(TreeModelListener l) {
    listenerList.remove(TreeModelListener.class, l);
  }

  @Override
  public Object getChild(Object parent, int index) {
    return ((AbstractEntity) parent).getEntityAt(index);
  }

  @Override
  public int getChildCount(Object parent) {
    return ((AbstractEntity) parent).getChildCount();
  }

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    if (parent == null || child == null) return -1;
    return ((AbstractEntity) parent).getIndexOf((AbstractEntity) child);
  }

  @Override
  /**
   * get the root of the tree
   */
  public Object getRoot() {
    return armyList;
  }

  @Override
  public boolean isLeaf(Object node) {
    return ((AbstractEntity) node).isLeaf();
  }

  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {
    AbstractEntity changedEntity = (AbstractEntity) path.getLastPathComponent();
    changedEntity.setName(newValue.toString());

    int[] idx = { getIndexOfChild(((Entity) changedEntity).getParent(),
        changedEntity) };
    Object[] o = { changedEntity };

    TreeModelEvent e = new TreeModelEvent(this, path, idx, o);
    fireTreeNodesChanged(e);

  }


  public Object[] getTreePathTo(Object o) {
    return getNodePath((AbstractEntity) o, 0).toArray();
  }


  private ArrayList<Object> getNodePath(AbstractEntity e, int levelCount) {
    ArrayList<Object> o;
    Entity ent = (Entity) e;
    if (ent.getParent() == null) o = new ArrayList<Object>(levelCount);
    else o = getNodePath(ent.getParent(), levelCount++);
    o.add(e);
    return o;
  }

  //TODO: throw ClassCastException?
  public AbstractEntity insertNewEntityInto(Object parent) {

  	//create new Entity with the given Name from language-file
    AbstractEntity newEntity = new Entity(TextWrapper
        .getString("ArmyListModel.0"));
    //create an Array with the new Entity as Element
    Object[] child = { newEntity };

    AbstractEntity paren;
    //If no parent was passed as parameter get the root from tree 
    //and insert the new node as child
    //else insert child into passed node
    if (parent == null) paren = (AbstractEntity) getRoot();
    else paren = (AbstractEntity) parent;
    int[] idx = { paren.getChildCount() };
    paren.addEntity(newEntity);

    TreeModelEvent e = new TreeModelEvent(this, getNodePath(paren, 0).toArray(), idx, child);
    fireTreeNodesInserted(e);
    return newEntity;
  }
  
  @Override
	public AbstractEntity insertEntitiy(Object[] treePath) {
  	//das erste Object im Array MUSS der root-Knoten sein, sonst ist es kein
  	//gültiger Pfad!
		if (treePath[0].equals(getRoot()) && treePath.length > 1){
			boolean found = false;
			AbstractEntity treeNode = armyList;
			//den Pfad vom Knoten eins bis Ende durchlaufen
			for (int i = 1; i < treePath.length -1; i++){
				//prüfen ob der Knoten schon enthalten ist
				//aber nur wenn es sich nicht um das Pfadblatt handelt. 
				//wenn der Knoten nicht enthalten ist anlegen 
				found = false;
				for (int j = 0; j < treeNode.getChildCount() && !found; j++ ){
					if (treeNode.getEntityAt(j).equals((AbstractEntity)treePath[i])){
						found = true;
						treeNode = treeNode.getEntityAt(j);
					}
				}
				if (!found){
					AbstractEntity newNode = (AbstractEntity)treePath[i];
					treeNode.addEntity(newNode);
					int[] idx = { treeNode.getChildCount() };
	  			Object[] child = { newNode };
		    	TreeModelEvent e = new TreeModelEvent(this, getTreePathTo(treeNode), idx, child );
		      fireTreeNodesInserted(e);
					treeNode = newNode;
				}
			}
			//den letzten Knoten auf jeden Fall hinzufügen
			AbstractEntity newNode = (AbstractEntity)treePath[treePath.length -1];			
			treeNode.addEntity(newNode);
			int[] idx = { treeNode.getChildCount() };
			Object[] child = { newNode };
    	TreeModelEvent e = new TreeModelEvent(this, getTreePathTo(treeNode), idx, child );
      fireTreeNodesInserted(e);
			return newNode;
		}
  	return null;
  }

  public void removeNodeFromParent(Object node) {
    Entity delEntity = (Entity) node;
    AbstractEntity parent = delEntity.getParent();
    parent.removeEntity(delEntity);

    Object[] child = { delEntity };
    int[] idx = { parent.getChildCount() };

    TreeModelEvent e = new TreeModelEvent(this, getNodePath(parent, 0)
        .toArray(), idx, child);

    fireTreeNodesRemoved(e);
  }

  //TODO: wird das hier benötigt? ist doch eigentlich crap
  public void insertNewAndUpgradeInto(Object parent) {
        
  }
  //TODO: wird das hier benötigt? ist doch eigentlich crap
  public void insertNewOrUpgradeInto(Object parent) {
    // TODO Auto-generated method stub
    
  }

}

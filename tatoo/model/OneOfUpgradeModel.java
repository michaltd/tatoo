package tatoo.model;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

import tatoo.model.conditions.Condition.ConditionTypes;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.EntityListener;
import tatoo.view.AbstractNodePanel;

/**
 * Model für den Entity Typ AbstractEntity.EntityType.ONEOFUPGRADE.
 * 
 * @see {@link AbstractEntity#EntityType}
 * @author mkortz
 */
public class OneOfUpgradeModel implements EntityListener {

    HashMap <AbstractEntity, AbstractNodePanel> nodePanels = new HashMap <AbstractEntity, AbstractNodePanel> ();
    ArrayList <AbstractNodePanel>               panels     = new ArrayList <AbstractNodePanel> ();
    ArrayList <Integer>                         panelCount = new ArrayList <Integer> ();

    public void addEntity (Object node, AbstractNodePanel panel) {
        AbstractEntity tmpEntity = (AbstractEntity) node;
        boolean found = false;
        while (tmpEntity != null) {
            if (nodePanels.containsKey (tmpEntity)) {
                nodePanels.put ((AbstractEntity) node, nodePanels.get (tmpEntity));
                ((AbstractEntity) node).addEntityListener (this);
                found = true;
                break;
            }
            else tmpEntity = tmpEntity.getParent ();
        }
        if ( !found) {
            nodePanels.put ((AbstractEntity) node, panel);
            panels.add (panel);
            panelCount.add (new Integer (0));
            ((AbstractEntity) node).addEntityListener (this);
        }
    }

    // TODO: Das folgende ist bescheuert! Es handelt sich hier um einen
    // dreckigen Hack.
    // Das muss ich beizeiten mal durch ein Event
    // ablösen. Eventuell diese ganze Geschichte dann neu designen?
    // http://geek-and-poke.com/2012/02/the-geekpoke-dictionary-coder-normal.html
    @Override
    public void AttribChanged (AbstractEntity entity, ConditionTypes attrib) {
        int conditionValue = (Integer) entity.getAttribute (attrib).getValue ();
        JPanel targetPanel = nodePanels.get (entity);
        int idx = panels.indexOf (targetPanel);
        Integer count = panelCount.get (idx);
        if (conditionValue == 0)
            count-- ;
        else count++ ;
        panelCount.set (idx, count);

        for (AbstractNodePanel p : panels) {
            Container container = p.getParent ();
            for (Component c : container.getComponents ()) {
                if (p != targetPanel) {
                    c.setEnabled (conditionValue == 0 && count == 0);
                    // c.setVisible (conditionValue == 0 && count == 0);
                    for (Component co : ((Container) c).getComponents ())
                        if (co instanceof JRadioButton) {
                            ((JRadioButton) co).setEnabled (false);
                        }
                }
                else {
                    for (Component co : ((Container) c).getComponents ())
                        if (co instanceof JRadioButton) {
                            ((JRadioButton) co).setSelected (count > 0);
                            ((JRadioButton) co).setEnabled (false);
                        }
                }
            }
        }
    }

    @Override
    public void ChildInserted (AbstractEntity entity, AbstractEntity child) {
        
    }

    @Override
    public void ChildRemoved (AbstractEntity entity, AbstractEntity child, int index) {
        
    }
}

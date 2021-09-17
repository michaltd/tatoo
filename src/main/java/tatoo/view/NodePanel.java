package tatoo.view;

import tatoo.model.AbstractEntityModel;
import tatoo.model.ArmyListEntityModel;
import tatoo.model.EntityModelListener;

import javax.swing.*;

/**
 * Interface for Panels for a node of {@link Armylist}. In most cases you don't
 * want to use this Interface directly, use {@link AbstractNodePanel} instead.
 *
 * @author mkortz
 */
public interface NodePanel extends EntityModelListener
{
    /**
     * Specifies that components should be laid out left to right.
     */
    int HORIZONTAL_ALIGNMENT = BoxLayout.X_AXIS;

    /**
     * Specifies that components should be laid out top to bottom.
     */
    int VERTICAL_ALIGNMENT = BoxLayout.Y_AXIS;

    /**
     * create the main Panel with all its Components.
     *
     * @param parentPanel the parent Panel where to lay down the created Panel. Attention: the
     *                    parent Panel may be changed in the NodePanel.
     * @param hasChilds
     */
    void createNodePanel(JPanel parentPanel, boolean hasChilds);

    /**
     * return the Panel on which the Components of this Panel are laid-out
     *
     * @return The Panel.
     */
    JPanel getChildNodePanel();

    /**
     * return if childs of this Panel should be shown.
     */
    boolean showChilds();

    /**
     * set if childs of this Panel should be shown.
     */
    void showChilds(boolean showChilds);

    void setAlignment(int align);

    /**
     * configure wether empty Entitys should be shown or not.
     *
     * @param show show empty Entitys?
     */
    void showEmptyEntitys(boolean show);

    AbstractEntityModel getModel();

    void setModel(ArmyListEntityModel model);
}

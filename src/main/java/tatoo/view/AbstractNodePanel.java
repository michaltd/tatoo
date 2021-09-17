package tatoo.view;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import tatoo.model.ArmyListEntityModel;

import javax.swing.*;

/**
 * Grundlegende Implementation für alle Node Panel. Ein NodePanel ist ein JPanel
 * welches für einen bestimmten Knotentyp geschrieben ist. Es legt verschiedene
 * Eigenschaften für die Darstellung des Knotens fest.
 *
 * @author mkortz
 * @see tatoo.model.entities.AbstractEntity.EntityType
 */
@SuppressWarnings("serial")
public abstract class AbstractNodePanel extends TatooPanel implements NodePanel
{
    /**
     * Das Model dieses NodePanels
     */
    @Getter
    protected ArmyListEntityModel model;

    /**
     * Legt fest ob leere Entitys dargestellt werden sollen oder nicht.
     */
    protected boolean showEmptyEntities = true;

    /**
     * legt die ausrichtung des Panels fest.
     */
    private int alignment;

    /**
     * legt fest ob die Kindsknoten dargestellt werden sollen oder nicht.
     */
    @Getter
    @Setter
    @Accessors(fluent = true, chain = false) // chain = false has to be set to not conflict the interface
    protected boolean showChilds = true;

    /**
     * JPanel für den Inhalt des NodePanels
     */
    protected JPanel contentPanel = new TatooPanel();

    /**
     * Konstruktor für ein leeres Nodepanel. Ein Knoten muss später mittels
     * {@link #setNode(Object)} manuell hinzugefügt werden.
     */
    protected AbstractNodePanel()
    {
    }

    public AbstractNodePanel(Object node)
    {
        this();
        setNode(node);
    }

    /**
     * Setzt den Wert ob leere Entitys angezeigt werden sollen oder nicht.
     *
     * @param show Leere Entitys anzeigen.
     */
    public void showEmptyEntitys(boolean show)
    {
        showEmptyEntities = show;
    }

    /**
     * this method is for intern use only. Do not override it or call it. Use
     * method {@link #create(JPanel, JPanel, boolean)} instead.
     */
    @Override
    public final void createNodePanel(JPanel parentPanel, boolean hasChilds)
    {
        if (showEmptyEntities || hasChilds) {
            parentPanel.setLayout(new BoxLayout(parentPanel, alignment));
            create(parentPanel, contentPanel, hasChilds);
        }
    }

    /**
     * set the node of this Panel
     *
     * @param node the Node
     */
    private void setNode(Object node)
    {
        setModel(new ArmyListEntityModel(node));
    }

    public void setModel(ArmyListEntityModel model)
    {
        this.model = model;
        this.model.addEntityModelListener(this);
    }

    @Override
    public JPanel getChildNodePanel()
    {
        return contentPanel;
    }

    public void setChildNodePanel(JPanel panel)
    {
        contentPanel = panel;
    }

    public void setAlignment(int align)
    {
        contentPanel.setLayout(new BoxLayout(contentPanel, align));
        alignment = align;
    }

    @Override
    public void EntityInserted()
    {
    }

    @Override
    public void EntityRemoved()
    {
    }

    /**
     * Here you can create and design your Panel
     *
     * @param parentPanel the Panel on which the Components are laid-out
     * @param hasChilds   information about wether the node has Childs or not
     */
    public abstract void create(JPanel parentPanel, JPanel contentPanel, boolean hasChilds);

    public void childInserted(AbstractNodePanel contentPanel, JPanel childPanel, boolean hasChilds)
    {
    }
}

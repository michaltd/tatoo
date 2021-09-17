package tatoo.view.armyList;

import tatoo.model.entities.events.EntityModelEvent;
import tatoo.view.AbstractNodePanel;

import javax.swing.*;

public class SimpleAnyOfUpgradePanel extends AbstractNodePanel
{
    @Override
    public void create(JPanel parentPanel, JPanel contentPanel, boolean hasChilds)
    {
        parentPanel.add(contentPanel);
    }

    @Override
    public void AttribChanged(EntityModelEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void SourceChanged()
    {
        // TODO Auto-generated method stub
    }
}

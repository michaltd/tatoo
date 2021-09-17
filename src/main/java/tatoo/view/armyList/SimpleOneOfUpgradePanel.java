package tatoo.view.armyList;

import tatoo.model.entities.events.EntityModelEvent;
import tatoo.view.AbstractNodePanel;
import tatoo.view.CurlyBraceBorder;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel für einen OneOfUpgrade-Knoten.
 *
 * @author mkortz
 */
public class SimpleOneOfUpgradePanel extends AbstractNodePanel
{
    private final ArrayList<Container> contentPanels = new ArrayList<>();
    private final ArrayList<JRadioButton> radioButtons = new ArrayList<>();

    @Override
    public void create(JPanel parentPanel, JPanel contentPanel, boolean hasChilds)
    {
        Border outsideBorder = BorderFactory.createEmptyBorder(2, 0, 2, 0);
        Border insideBorder = BorderFactory.createMatteBorder(1, 1, 1, 0, Color.LIGHT_GRAY);
        CompoundBorder compBorder = BorderFactory.createCompoundBorder(outsideBorder, insideBorder);
        contentPanel.setBorder(compBorder);
        parentPanel.add(contentPanel);
    }

    @Override
    public void childInserted(AbstractNodePanel contentPanel, JPanel childPanel, boolean hasChilds)
    {
        contentPanels.add(contentPanel);
        childPanel.setLayout(new BorderLayout());
        JRadioButton radioButton = new JRadioButton();
        radioButton.setEnabled(false);
        childPanel.add(radioButton, BorderLayout.CENTER);
        radioButtons.add(radioButton);
        if (hasChilds) {
            childPanel.setBorder(
                new CurlyBraceBorder(
                    Color.LIGHT_GRAY,
                    10,
                    CurlyBraceBorder.Position.RIGHT,
                    CurlyBraceBorder.Position.LEFT
                )
            );
        }
    }

    @Override
    public void AttribChanged(EntityModelEvent e)
    {
    }

    @Override
    public void SourceChanged()
    {
    }
}

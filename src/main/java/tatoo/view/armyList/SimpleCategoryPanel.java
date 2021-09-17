package tatoo.view.armyList;

import lombok.Setter;
import tatoo.model.entities.events.EntityModelEvent;
import tatoo.view.AbstractNodePanel;
import tatoo.view.TatooPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SimpleCategoryPanel extends AbstractNodePanel
{
    @Setter
    private int width = 200;

    @Override
    public void create(JPanel parentPanel, JPanel contentPanel, boolean hasChilds)
    {
        JButton nameButton = new JButton();
        nameButton.setText(model.getName());
        nameButton.setFocusable(false);

        JPanel borderPanel = new TatooPanel();
        borderPanel.setLayout(new BorderLayout());
        borderPanel.setBorder(new EmptyBorder(0, 3, 0, 2));

        JPanel fillerPanel = new TatooPanel();
        fillerPanel.setLayout(new BorderLayout());

        fillerPanel.add(contentPanel, BorderLayout.NORTH);
        fillerPanel.add(Box.createGlue(), BorderLayout.CENTER);

        borderPanel.add(nameButton, BorderLayout.NORTH);
        borderPanel.add(fillerPanel, BorderLayout.CENTER);
        borderPanel.add(Box.createHorizontalStrut(width), BorderLayout.SOUTH);

        parentPanel.add(borderPanel);
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

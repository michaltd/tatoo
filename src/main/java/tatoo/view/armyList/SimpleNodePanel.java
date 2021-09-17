package tatoo.view.armyList;

import tatoo.model.entities.events.EntityModelEvent;
import tatoo.view.AbstractNodePanel;
import tatoo.view.TatooPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SimpleNodePanel extends AbstractNodePanel
{
    @Override
    public void create(JPanel parentPanel, JPanel contentPanel, boolean hasChilds)
    {
        JPanel containerPanel = new TatooPanel();
        containerPanel.setLayout(new BorderLayout());

        LineBorder border = new LineBorder(Color.LIGHT_GRAY);
        Border compount_innerBorder = BorderFactory.createCompoundBorder(new EmptyBorder(3, 0, 2, 0), border);
        Border compount = BorderFactory.createCompoundBorder(compount_innerBorder, new EmptyBorder(3, 3, 3, 3));
        containerPanel.setBorder(compount);

        JPanel headerPanel = new TatooPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JPanel namePanel = new TatooPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));

        JLabel nameLabel = new JLabel("<html><b><i>" + model.getName() + "</i></b></html>");
        nameLabel.setHorizontalAlignment(JLabel.LEFT);

        JLabel priceLabel = new JLabel(model.getPrice());

        namePanel.add(nameLabel);
        namePanel.add(Box.createGlue());
        namePanel.add(priceLabel);

        headerPanel.add(namePanel);

        containerPanel.add(headerPanel, BorderLayout.NORTH);
        containerPanel.add(contentPanel, BorderLayout.CENTER);

        parentPanel.add(containerPanel);
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

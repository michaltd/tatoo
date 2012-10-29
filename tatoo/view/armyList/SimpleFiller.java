package tatoo.view.armyList;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import tatoo.model.ArmyListEntityModel;
import tatoo.model.entities.events.EntityModelEvent;
import tatoo.view.AbstractNodePanel;
import tatoo.view.TatooPanel;

@SuppressWarnings ("serial")
public class SimpleFiller extends AbstractNodePanel {

    final JTextField valueField = new JTextField ();
    JLabel           priceLabel = new JLabel ();

    @Override
    public void create (JPanel parentPanel, JPanel contentPanel, boolean hasChilds) {

        model.addEntityModelListener (this);

        JPanel borderForPanel = new TatooPanel ();
        borderForPanel.setLayout (new BoxLayout (borderForPanel, BoxLayout.Y_AXIS));
        borderForPanel.setAlignmentX (0);
        parentPanel.setLayout (new BorderLayout ());

        JPanel titlePanel = new TatooPanel ();
        titlePanel.setLayout (new BorderLayout ());

        JLabel nameLabel = new JLabel ();
        nameLabel.setText (model.getName ());

        JPanel valuePanel = new TatooPanel ();
        valuePanel.setLayout (new BorderLayout ());

        final ArmyListEntityModel simpleFillerModel = model;

        JButton decrement = new JButton ("-");
        decrement.addActionListener (new ActionListener () {

            @Override
            public void actionPerformed (ActionEvent e) {
                simpleFillerModel.setCount (Integer.parseInt (simpleFillerModel.getCount ()) - 1);
            }
        });

        JPanel decrementPanel = new TatooPanel ();
        decrementPanel.setLayout (new BorderLayout ());
        decrementPanel.add (decrement, BorderLayout.CENTER);
        valuePanel.add (decrementPanel, BorderLayout.WEST);

        JPanel valueFieldPanel = new TatooPanel ();
        valueFieldPanel.setLayout (new BorderLayout ());
        valueFieldPanel.add (Box.createHorizontalStrut (50), BorderLayout.NORTH);

        valueField.setHorizontalAlignment (JTextField.CENTER);

        valueFieldPanel.add (valueField, BorderLayout.CENTER);
        valuePanel.add (valueFieldPanel, BorderLayout.CENTER);

        JButton increment = new JButton ("+");
        increment.addActionListener (new ActionListener () {

            @Override
            public void actionPerformed (ActionEvent e) {
                simpleFillerModel.setCount (Integer.parseInt (simpleFillerModel.getCount ()) + 1);
            }
        });

        JPanel incrementPanel = new TatooPanel ();
        incrementPanel.setLayout (new BorderLayout ());
        incrementPanel.add (increment, BorderLayout.CENTER);
        valuePanel.add (incrementPanel, BorderLayout.EAST);

        JPanel valueContainerPane = new TatooPanel ();
        valueContainerPane.setLayout (new BorderLayout ());
        valueContainerPane.add (valuePanel, BorderLayout.CENTER);

        priceLabel.setText (model.getPrice ());
        priceLabel.setHorizontalTextPosition (SwingConstants.RIGHT);
        JPanel pricePanel = new TatooPanel ();
        pricePanel.setLayout (new BorderLayout ());
        pricePanel.add (Box.createHorizontalStrut (25), BorderLayout.NORTH);
        pricePanel.add (priceLabel, BorderLayout.EAST);
        valueContainerPane.add (pricePanel, BorderLayout.EAST);

        titlePanel.add (nameLabel, BorderLayout.CENTER);
        titlePanel.add (valueContainerPane, BorderLayout.EAST);

        borderForPanel.add (titlePanel);
        borderForPanel.add (contentPanel);

        parentPanel.add (borderForPanel);

        setCount ();

    }

    private void setCount () {
        valueField.setText (model.getCount () + "/" + model.getMaxCount ());
        priceLabel.setText (model.getPrice ());
    }

    @Override
    public void AttribChanged (EntityModelEvent e) {
        setCount ();
    }

    @Override
    public void SourceChanged () {}

}

package tatoo.view.armyBuilder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import tatoo.model.ArmyBuilderEntityModel;
import tatoo.model.EntityModelListener;
import tatoo.model.entities.events.EntityModelEvent;

@SuppressWarnings ("serial")
public abstract class ArmyBuilderEditPanel extends JPanel implements EntityModelListener {

    protected int                    width         = 300;
    protected ArmyBuilderEntityModel model         = null;
    private boolean                  sourceChanged = false;

    public ArmyBuilderEditPanel (ArmyBuilderEntityModel entityModel) {
        super ();
        this.model = entityModel;
        model.addEntityModelListener (this);
        buildPanel ();
    }

    public void setWidth (int width) {
        this.width = width;
        clearPanel ();
        buildPanel ();
    }

    @Override
    public void SourceChanged () {
        sourceChanged = true;
        showValues ();
        sourceChanged = false;
    }

    public void AttribChanged (EntityModelEvent e) {
        // we ignore the event-Object here and refresh all attributes for
        // simplicity
        if (sourceChanged)
            showValues ();
    }

    void clearPanel () {
        this.removeAll ();
    }

    /**
     * Build the Panel.
     */
    public abstract void buildPanel ();

    abstract void showValues ();

    protected class groupPane extends JPanel {

        public groupPane (String title) {
            this (title, 70);
        }

        public groupPane (String title, int height) {
            TitledBorder b = new TitledBorder (new LineBorder (Color.LIGHT_GRAY), title);
            b.setTitleColor (Color.LIGHT_GRAY);
            b.setTitleFont (new Font ("Arial", Font.ITALIC, 12));
            setBorder (b);
            // setHeight( height );
        }

        private void setHeight (int height) {
            setPreferredSize (new Dimension (width, height));
            setMaximumSize (new Dimension (width, height));
            setMinimumSize (new Dimension (width, height));
        }
    }

    protected abstract class TextFieldChangeHandler implements DocumentListener {

        JTextField field;

        public TextFieldChangeHandler (JTextField field) {
            this.field = field;
        }

        @Override
        public void changedUpdate (DocumentEvent e) {
            setValue (field.getText ());
        }

        @Override
        public void insertUpdate (DocumentEvent e) {
            if ( !sourceChanged)
                setValue (field.getText ());
        }

        @Override
        public void removeUpdate (DocumentEvent e) {
            String val = field.getText ();
            if (val.length () > 0)
                setValue (val);
        }

        public abstract void setValue (String value);

    }

}
package tatoo.view;

import java.awt.Component;

import javax.swing.JPanel;


public class TatooPanel extends JPanel {

    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled( enabled );
        for (Component c : this.getComponents())
            c.setEnabled( enabled );
    }
    
}

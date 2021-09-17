package tatoo.view;

import javax.swing.*;
import java.awt.*;

public class TatooPanel extends JPanel
{
    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        for (Component c : this.getComponents())
            c.setEnabled(enabled);
    }
}

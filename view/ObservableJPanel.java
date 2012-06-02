package tatoo.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ObservableJPanel extends JPanel {

	private ArrayList<ActionListener> actionListener = new ArrayList<ActionListener>();
	
	public void addActionListener(ActionListener l) {
		actionListener.add(l);
	}
	
	public void removeActionListener(ActionListener l){
		actionListener.remove(l);
	}
	
	protected void fireActionPerformed(ActionEvent event){
		for (ActionListener al : actionListener){
			al.actionPerformed(event);
		}
	}
	
}

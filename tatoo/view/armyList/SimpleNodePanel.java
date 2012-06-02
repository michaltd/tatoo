package tatoo.view.armyList;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import tatoo.model.entities.EntityModelEvent;
import tatoo.view.AbstractNodePanel;

@SuppressWarnings("serial")
public class SimpleNodePanel extends AbstractNodePanel {

	@Override
	public void create(JPanel parentPanel, JPanel contentPanel ,boolean hasChilds) {
		
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BorderLayout());		

		LineBorder border =  new LineBorder(Color.lightGray);
		Border compount_innerBorder = BorderFactory.createCompoundBorder(
				new EmptyBorder(3, 0, 2, 0), border );
		Border compount = BorderFactory.createCompoundBorder(
				compount_innerBorder, new EmptyBorder(3,3,3,3) );
		containerPanel.setBorder(compount);
		
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel,BoxLayout.Y_AXIS));
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
	
		JLabel nameLabel = new JLabel("<html><b><i>"+model.getName()+"</i></b></html>");
		nameLabel.setHorizontalAlignment(JLabel.LEFT);
		
		JLabel priceLabel = new JLabel(new Integer(model.getPrice().getValue()).toString());
		
		namePanel.add(nameLabel);
		namePanel.add(Box.createGlue());
		namePanel.add(priceLabel);
		
		headerPanel.add(namePanel);
		
		containerPanel.add(headerPanel, BorderLayout.NORTH);
		containerPanel.add(contentPanel, BorderLayout.CENTER);
		
		parentPanel.add(containerPanel);

	}

	@Override
	public void AttribChanged(EntityModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SourceChanged() {
		// TODO Auto-generated method stub
		
	}


}

package tatoo.view.armyList;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import tatoo.model.entities.events.EntityModelEvent;
import tatoo.view.AbstractNodePanel;
import tatoo.view.TatooPanel;

@SuppressWarnings("serial")
public class SimpleCategoryPanel extends AbstractNodePanel {

	private int width = 200;
	
	@Override
	public void create(JPanel parentPanel, JPanel contentPanel ,boolean hasChilds) {
		
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
	
	public void setWidth(int width){
		this.width = width;
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

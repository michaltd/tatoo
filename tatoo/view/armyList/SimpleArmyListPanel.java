package tatoo.view.armyList;

import javax.swing.JPanel;

import tatoo.model.entities.events.EntityModelEvent;
import tatoo.view.AbstractNodePanel;

public class SimpleArmyListPanel extends AbstractNodePanel {
	
	@Override
	public void create(JPanel parentPanel, JPanel contentPanel ,boolean hasChilds) {
//		contentPanel.setLayout(new GridLayout());
		parentPanel.add(contentPanel);
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

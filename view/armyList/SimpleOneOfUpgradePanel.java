package tatoo.view.armyList;

import javax.swing.JPanel;

import tatoo.model.entities.EntityModelEvent;
import tatoo.view.AbstractNodePanel;

@SuppressWarnings("serial")
public class SimpleOneOfUpgradePanel extends AbstractNodePanel {

	@Override
	public void create(JPanel parentPanel, JPanel contentPanel, boolean hasChilds) {
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

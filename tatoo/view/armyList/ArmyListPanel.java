package tatoo.view.armyList;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import tatoo.ArmyListInstanceMainPanel;
import tatoo.ArmyListInstanceSidePanel;
import tatoo.model.ArmyListModel;
import tatoo.model.entities.Entity;
import tatoo.view.AbstractNodePanel;

@SuppressWarnings("serial")
public class ArmyListPanel extends JPanel{
	
	ArmyListModel sideMenuModel;
	ArmyListModel armyListModel;

	public ArmyListPanel(){
	  this(new ArmyListInstanceSidePanel().armeeliste);
	}

  public ArmyListPanel(Entity armeeliste){
		
		this.setLayout(new BorderLayout());
		//sideMenuModel = new ArmyListModel(new ArmyListInstanceSidePanel().armeeliste);
		sideMenuModel = new ArmyListModel(armeeliste);
		
  	ArmyTreePanel sideMenu = new ArmyTreePanel( sideMenuModel, "Sidemenu");
  	sideMenu.addMouseListener(new SideMenuListener());
  	
  	armyListModel = new ArmyListModel(new ArmyListInstanceMainPanel().armeeliste);
  	ArmyTreePanel armyList = new ArmyTreePanel( armyListModel, "ArmyList");
  	armyList.addMouseListener(new ArmyListListener());
		
		JScrollPane scrollPane = new JScrollPane(armyList);
		scrollPane.setBorder(null);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sideMenu, scrollPane);
		splitPane.setDividerLocation(215);
		
		this.add(splitPane);
	}
	
	private class SideMenuListener implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			armyListModel.insertEntitiy(sideMenuModel.getTreePathTo(((AbstractNodePanel)e.getSource()).getModel().getSource()));
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

	}
	
	private class ArmyListListener implements MouseListener{
		
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

	}

}


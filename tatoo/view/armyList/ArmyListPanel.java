package tatoo.view.armyList;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import tatoo.ArmyListInstanceSidePanel;
import tatoo.model.ArmyListModel;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.ArmyListEntity;
import tatoo.view.AbstractNodePanel;

@SuppressWarnings("serial")
public class ArmyListPanel extends JPanel{
	
	ArmyListModel sideMenuModel;
	ArmyListModel armyListModel;

//	public ArmyListPanel(){
//	  this(new ArmyListInstanceSidePanel().armeeliste);
//	}

  public ArmyListPanel(AbstractEntity armeeliste){
		
		this.setLayout(new BorderLayout());
		//sideMenuModel = new ArmyListModel(new ArmyListInstanceSidePanel().armeeliste);
		sideMenuModel = new ArmyListModel(armeeliste);
		
  	ArmyTreePanel sideMenu = new ArmyTreePanel( sideMenuModel, "Sidemenu");
  	sideMenu.addMouseListener(new SideMenuListener());
  	
  	armyListModel = new ArmyListModel(new ArmyListEntity(AbstractEntity.EntityType.ROOT, "Armeeliste"));
  	ArmyTreePanel armyList = new ArmyTreePanel( armyListModel, "ArmyList");
		
		JScrollPane scrollPane = new JScrollPane(armyList);
		scrollPane.setBorder(null);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sideMenu, scrollPane);
		splitPane.setDividerLocation(215);
		
		this.add(splitPane);
	}
	
	private class SideMenuListener implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {
		  // das NodePanel das das Event ausgelöst hat aus dem event holen.
		  AbstractNodePanel sourcePanel = (AbstractNodePanel)e.getSource();
		  // Das NodePanel hat ein Model und daraus das ArmyListEntity holen
		  Object entity = sourcePanel.getModel().getSource();
		  // aus dem sideMenuModel den Pfad für dieses ArmyListEntity bestimmen...
		  Object[] treePath = sideMenuModel.getTreePathTo(entity);
		  // ... und an das armyListModel anhängen. 
			armyListModel.insertCopyOf(treePath);
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

}


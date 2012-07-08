package tatoo.view.armyBuilder;

import javax.swing.JButton;

import tatoo.model.EntityModel;


@SuppressWarnings("serial")
public class UpgradeEditPanel extends ArmyListEditPanel {

  public UpgradeEditPanel(EntityModel entityModel) {
    super(entityModel);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void buildPanel() {
    add(new JButton("Upgrade"));

  }

  @Override
  void showValues() {
  // TODO Auto-generated method stub
  }

}

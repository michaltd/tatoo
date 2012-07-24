package tatoo.view.armyBuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import tatoo.model.EntityModel;
import tatoo.model.conditions.SimpleNumber;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.ArmyListEntity;
import tatoo.model.entities.attributesetters.AttribSetter;
import tatoo.resources.TextWrapper;
import tatoo.view.ObservableJPanel;

/**
 * JPanel for building Criterias. This Panel is normaly Instantiatet in another
 * JPanel as Popup.
 * 
 * @author mkortz
 *
 */
@SuppressWarnings("serial")
public class ConditionBuilder extends ObservableJPanel {
	
	JTextField dependsOnTextField;
	JTextField fieldTextField;
	AttribSetter attribSetter;
	EntityModel model;
	
	/**
	 * Standard-Constructor. The only thing this Constructor does is calling {@link #buildPane()}
	 * @param as The AttribSetter for the desired Attribute 
	 * @param model TODO
	 */
  public ConditionBuilder(AttribSetter as, EntityModel model ){
  	attribSetter = as;
  	this.model = model;
    buildPane();
  }
  
  /**
   * build the CriteriaBuilder Panel
   */
  public void buildPane(){
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    //Create Borders
    Border outsideBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
    Border insideBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    CompoundBorder outCompBorder = BorderFactory.createCompoundBorder(outsideBorder, insideBorder);
    Border spaceBorder = BorderFactory.createEmptyBorder(3, 3, 3, 3);
    CompoundBorder compBorder = BorderFactory.createCompoundBorder(outCompBorder, spaceBorder);
    this.setBorder(compBorder);
    //Create the rest of Components
    JLabel dependsOnLabel = new JLabel(TextWrapper.getString("CriteriaBuilder.0"));
    add(dependsOnLabel);
    dependsOnTextField = new JTextField();
    add(dependsOnTextField);
    
    Vector<AbstractEntity> items = new Vector<AbstractEntity>();
//    AbstractEntity ae =(AbstractEntity)model.getSource();
    
    JComboBox dependOnComboBox = new JComboBox(items);
    add(dependOnComboBox);
    
    JLabel fieldLabel = new JLabel(TextWrapper.getString("CriteriaBuilder.1"));
    add(fieldLabel);
    fieldTextField = new JTextField();
    add(fieldTextField);
    //Create Buttons
    JPanel buttonPane = new JPanel();
    JButton okButton = new JButton(TextWrapper.getString("General.0"));
    okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (checkInput()){
					setCondition();
					fireActionPerformed(arg0);
				}
			}});
		buttonPane.add(okButton);
		JButton abortButton = new JButton(TextWrapper.getString("General.1"));
		abortButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fireActionPerformed(arg0);
			}});
		buttonPane.add(abortButton);
    add(buttonPane);
  }
  
  /**
   * check the JFields. Is input correct?
   * @return
   */
  private boolean checkInput(){
  	return true;
  }
  
  /**
   * set the Attribute this ConditionBuilder is for.
   */
  private void setCondition(){
  	if (checkInput())
  		attribSetter.setAttrib(model,new SimpleNumber(10));  	
  }

}

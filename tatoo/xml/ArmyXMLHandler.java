package tatoo.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tatoo.Tatoo;
import tatoo.model.ArmyListModel;
import tatoo.model.conditions.Condition;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.ArmyListEntity;

/**
 *
 * @author friend8
 */
public class ArmyXMLHandler extends AbstractXMLHandler
{
    /**
     * The current selected army list
     */
    private ArmyListModel armyList;

    /**
     * Set the used xml schema and the root node
     */
    public ArmyXMLHandler()
    {
        super("army.xsd", "army");
    }

    /**
     * Create XML from the current selected army list
     *
     * @param list
     *
     * @return The prepared XML
     */
    @Override
    public String write(Object list)
    {
        this.armyList = (ArmyListModel) list;
        AbstractEntity armyListRoot = (AbstractEntity) this.armyList.getRoot();
        this.getWriter().addChild("name", armyListRoot.getName());
        this.getWriter().addChild("creator", "Neithan"); // @TODO: replace with an entered value
        this.getWriter().addChild("createDateTime", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()));

        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("tatooVersion", Tatoo.VERSION.toString());
        attributes.put("gameID", "");
        attributes.put("gameVersion", "0");
        attributes.put("gameEdition", "0");
        attributes.put("armyID", "");
        attributes.put("armyVersion", "0");
        attributes.put("armyEdition", "0");
        this.getWriter().addRootNodeAttributes(attributes);

        Element data = this.getWriter().addChild("data");

        for (int i = 0; i < this.armyList.getChildCount(armyListRoot); i++)
        {
            ArmyListEntity child = (ArmyListEntity) this.armyList.getChild(armyListRoot, i);
            this.write(data, child);
        }

        ByteArrayOutputStream baos = this.getWriter().writeToOutputStream();

        return new String(baos.toByteArray());
//        return XMLWriter.prepareXML(new String(baos.toByteArray()));
    }

    /**
     * Fetch each single entity in the army list tree and add it to the XML tree
     *
     * @param parent The parent object in the XML tree
     * @param obj    The parent object in the army list
     */
    private void write(Element parent, ArmyListEntity obj)
    {
        Element childElement = this.getWriter().addChild(parent, "child");
        this.getWriter().addChild(childElement, "name", obj.getName());
        this.getWriter().addChild(childElement, "nodeType", obj.getType().name());
        this.getWriter().addChild(childElement, "count", obj.getAttribute(Condition.ConditionTypes.COUNT).getValue().toString());
        this.getWriter().addChild(childElement, "points", obj.getAttribute(Condition.ConditionTypes.PRICE).getValue().toString());
        this.getWriter().addChild(childElement, "minCount", obj.getAttribute(Condition.ConditionTypes.MIN_COUNT).getValue().toString());
        this.getWriter().addChild(childElement, "maxCount", obj.getAttribute(Condition.ConditionTypes.MAX_COUNT).getValue().toString());

        if (this.armyList.getChildCount(obj) > 0)
        {
            Element childListElement = this.getWriter().addChild(childElement, "childList");
            List childs = obj.getChilds();
            for (int i = 0; i < obj.getChildCount(); i++)
            {
                ArmyListEntity child = (ArmyListEntity) childs.get(i);
                this.write(childListElement, child);
            }
        }
    }

    /**
     * Parse the given XML into an ArmyListModel
     *
     * @param xml
     */
    @Override
    public void parse(String xml)
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        this.getParser().parse(bais);
        Document document = this.getParser().getDocument();
    }

    /**
     * Get the ArmyListModel object
     *
     * @return An ArmyListModel object
     */
    public ArmyListModel getArmyList()
    {
        return armyList;
    }
}
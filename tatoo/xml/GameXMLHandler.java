/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tatoo.xml;

/**
 *
 * @author Neithan
 */
public class GameXMLHandler extends AbstractXMLHandler
{
    public GameXMLHandler()
    {
        super("game.xsd", "game");
    }

    @Override
    public String write(Object list)
    {
        return "";
    }

    @Override
    public void parse(String xml)
    {
    }

}

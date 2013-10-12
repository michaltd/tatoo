package tatoo.xml;

import java.io.InputStream;
import myXML.XMLParser;
import myXML.XMLWriter;
import tatoo.db.DBFactory;
import tatoo.soap.TatooWebservice;

/**
 * Abstract XML handler class
 *
 * @author friend8
 */
public abstract class AbstractXMLHandler
{
    /**
     * The XML schema as InputStream
     */
    private InputStream schema;

    /**
     * The XMLWriter
     */
    private XMLWriter writer;

    /**
     * The XMLParser created with the help of the XML schema
     */
    private XMLParser parser;

    /**
     * The dbFactory used in tatoo
     */
    private DBFactory dbFactory;

    /**
     * Creates a new XML handler
     *
     * @param schema      The XML schema to validate against
     * @param rootElement The name of the XML root element
     */
    public AbstractXMLHandler(String schema, String rootElement)
    {
        this.schema = this.getClass().getResourceAsStream("/tatoo/xml/schema/" + schema);
        this.parser = new XMLParser(this.schema);
        this.writer = new XMLWriter(rootElement);

        this.dbFactory = DBFactory.getInstance();
    }

    /**
     * Get the XMLWriter object
     *
     * @return XMLWriter object
     */
    public XMLWriter getWriter()
    {
        return this.writer;
    }

    /**
     * Get the XMLParser object
     *
     * @return XMLParser object
     */
    public XMLParser getParser()
    {
        return this.parser;
    }

    /**
     * Get the dbFactory object
     *
     * @return DBFactory object
     */
    public DBFactory getDbFactory()
    {
        return this.dbFactory;
    }

    /**
     * Write all given elements from the list object as XML
     *
     * @param list
     * @return The generated XML
     */
    public abstract String write(Object list);

    /**
     * Parse a given XML to the corresponding list object
     *
     * @param xml
     */
    public abstract void parse(String xml);
}
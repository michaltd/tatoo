package tatoo.xml;

import antafes.myXML.XMLParser;
import antafes.myXML.XMLWriter;
import lombok.AccessLevel;
import lombok.Getter;
import tatoo.db.DBFactory;

import java.io.InputStream;

/**
 * Abstract XML handler class
 *
 * @author friend8
 */
@Getter
public abstract class AbstractXMLHandler
{
    /**
     * The XML schema as InputStream
     */
    @Getter(AccessLevel.NONE)
    private final InputStream schema;

    /**
     * The XMLWriter
     */
    private final XMLWriter writer;

    /**
     * The XMLParser created with the help of the XML schema
     */
    private final XMLParser parser;

    /**
     * The dbFactory used in tatoo
     */
    private final DBFactory dbFactory;

    /**
     * Creates a new XML handler
     *
     * @param schema      The XML schema to validate against
     * @param rootElement The name of the XML root element
     */
    public AbstractXMLHandler(String schema, String rootElement)
    {
        this.schema = this.getClass().getResourceAsStream("xml/schema/" + schema);
        this.parser = new XMLParser(this.schema);
        this.writer = new XMLWriter(rootElement);

        this.dbFactory = DBFactory.getInstance();
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
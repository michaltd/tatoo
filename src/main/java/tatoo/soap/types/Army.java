package tatoo.soap.types;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigInteger;

/**
 * <p>Java class for army complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="army">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="edition" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="armyID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="creator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "army", propOrder = {
    "name",
    "version",
    "edition",
    "armyID",
    "creator"
})
@Getter
@Setter
public class Army
{
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected BigInteger version;
    @XmlElement(required = true)
    protected BigInteger edition;
    @XmlElement(required = true)
    protected String armyID;
    @XmlElement(required = true)
    protected String creator;
}

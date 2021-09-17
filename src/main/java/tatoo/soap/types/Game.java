package tatoo.soap.types;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for game complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="game">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="edition" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="gameID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="creator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "game", propOrder = {
    "name",
    "version",
    "edition",
    "gameID",
    "creator"
})
@Getter
@Setter
public class Game
{
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected BigInteger version;
    @XmlElement(required = true)
    protected BigInteger edition;
    @XmlElement(required = true)
    protected String gameID;
    @XmlElement(required = true)
    protected String creator;
}

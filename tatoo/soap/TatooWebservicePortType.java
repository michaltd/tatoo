
package tatoo.soap;

import tatoo.soap.types.ObjectFactory;
import tatoo.soap.types.ArmyList;
import tatoo.soap.types.GameList;
import java.math.BigInteger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 *
 */
@WebService(name = "TatooWebservicePortType", targetNamespace = "http://wafriv.de/tatoo_webservice/wsdl.php")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface TatooWebservicePortType {


    /**
     *
     * @param configuration
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:getTatooConfiguration")
    @WebResult(name = "configurationValue", partName = "configurationValue")
    public String getTatooConfiguration(
        @WebParam(name = "configuration", partName = "configuration")
        String configuration);

    /**
     *
     * @param createDateTime
     * @param edition
     * @param gameData
     * @param gameID
     * @param name
     * @param creator
     * @param version
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:gameUpload")
    @WebResult(name = "response", partName = "response")
    public String gameUpload(
        @WebParam(name = "name", partName = "name")
        String name,
        @WebParam(name = "gameID", partName = "gameID")
        String gameID,
        @WebParam(name = "version", partName = "version")
        BigInteger version,
        @WebParam(name = "edition", partName = "edition")
        BigInteger edition,
        @WebParam(name = "creator", partName = "creator")
        String creator,
        @WebParam(name = "createDateTime", partName = "createDateTime")
        XMLGregorianCalendar createDateTime,
        @WebParam(name = "gameData", partName = "gameData")
        String gameData);

    /**
     *
     * @param createDateTime
     * @param edition
     * @param gameVersion
     * @param gameID
     * @param armyData
     * @param name
     * @param gameEdition
     * @param armyID
     * @param creator
     * @param version
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:armyUpload")
    @WebResult(name = "response", partName = "response")
    public String armyUpload(
        @WebParam(name = "name", partName = "name")
        String name,
        @WebParam(name = "gameID", partName = "gameID")
        String gameID,
        @WebParam(name = "gameVersion", partName = "gameVersion")
        BigInteger gameVersion,
        @WebParam(name = "gameEdition", partName = "gameEdition")
        BigInteger gameEdition,
        @WebParam(name = "armyID", partName = "armyID")
        String armyID,
        @WebParam(name = "version", partName = "version")
        BigInteger version,
        @WebParam(name = "edition", partName = "edition")
        BigInteger edition,
        @WebParam(name = "creator", partName = "creator")
        String creator,
        @WebParam(name = "createDateTime", partName = "createDateTime")
        XMLGregorianCalendar createDateTime,
        @WebParam(name = "armyData", partName = "armyData")
        String armyData);

    /**
     *
     * @param versions
     * @return
     *     returns tatoo.soap.GameList
     */
    @WebMethod(action = "urn:getGameList")
    @WebResult(name = "gameList", partName = "gameList")
    public GameList getGameList(
        @WebParam(name = "versions", partName = "versions")
        boolean versions);

    /**
     *
     * @param versions
     * @param gameVersion
     * @param gameID
     * @param gameEdition
     * @return
     *     returns tatoo.soap.ArmyList
     */
    @WebMethod(action = "urn:getArmyList")
    @WebResult(name = "armyList", partName = "armyList")
    public ArmyList getArmyList(
        @WebParam(name = "gameID", partName = "gameID")
        String gameID,
        @WebParam(name = "gameVersion", partName = "gameVersion")
        BigInteger gameVersion,
        @WebParam(name = "gameEdition", partName = "gameEdition")
        BigInteger gameEdition,
        @WebParam(name = "versions", partName = "versions")
        boolean versions);

    /**
     *
     * @param edition
     * @param gameID
     * @param version
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:getGame")
    @WebResult(name = "gameData", partName = "gameData")
    public String getGame(
        @WebParam(name = "gameID", partName = "gameID")
        String gameID,
        @WebParam(name = "version", partName = "version")
        BigInteger version,
        @WebParam(name = "edition", partName = "edition")
        BigInteger edition);

    /**
     *
     * @param edition
     * @param armyID
     * @param version
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:getArmy")
    @WebResult(name = "armyData", partName = "armyData")
    public String getArmy(
        @WebParam(name = "armyID", partName = "armyID")
        String armyID,
        @WebParam(name = "version", partName = "version")
        BigInteger version,
        @WebParam(name = "edition", partName = "edition")
        BigInteger edition);

}

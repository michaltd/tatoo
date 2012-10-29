package tatoo.model;

/**
 * Model für Entities welche in der Armylist angezeigt werden sollen. Kapselt
 * die Entitys, so dass nicht direkt darauf zugegriffen werden kann.
 * 
 * @see tatoo.view.armyList
 * @author mkortz
 */
public interface EntityModel {

    /**
     * Gibt den Namen des Entitys zurück.
     * 
     * @return Der Name des Entitys.
     */
    public String getName ();

    /**
     * Gibt den Preis des Entitys zurück.
     * 
     * @return Der Preis des Entitys.
     */
    public String getPrice ();

    /**
     * Gibt den Preis des Entitys und aller Unterentities zurück.
     * 
     * @return Den vollen Preis des Entitys samt Inhalt.
     */
    public int getTotalPrice ();

    /**
     * Gibt Anzahl zurück, die im ArmyListEntity derzeit eingestellt ist.
     * 
     * @return Die Anzahl des Entitys.
     */
    public String getCount ();

    /**
     * Gibt die Mindestanzahl des Entitys zurück.
     * 
     * @return die Mindestanzahl des Entitys.
     */
    public String getMinCount ();

    /**
     * Gibt die Maximale Anzahl des Entitys zurück.
     * 
     * @return die Maximale Anzahl des Entitys.
     */
    public String getMaxCount ();

}

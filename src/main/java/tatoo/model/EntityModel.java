package tatoo.model;

/**
 * Model für Entities welche in der Armylist angezeigt werden sollen. Kapselt
 * die Entitys, so dass nicht direkt darauf zugegriffen werden kann.
 *
 * @author mkortz
 * @see tatoo.view.armyList
 */
public interface EntityModel
{
    /**
     * Gibt den Namen des Entitys zurück.
     *
     * @return Der Name des Entitys.
     */
    String getName();

    /**
     * Gibt den Preis des Entitys zurück.
     *
     * @return Der Preis des Entitys.
     */
    String getPrice();

    /**
     * Gibt den Preis des Entitys und aller Unterentities zurück.
     *
     * @return Den vollen Preis des Entitys samt Inhalt.
     */
    int getTotalPrice();

    /**
     * Gibt Anzahl zurück, die im ArmyListEntity derzeit eingestellt ist.
     *
     * @return Die Anzahl des Entitys.
     */
    String getCount();

    /**
     * Gibt die Mindestanzahl des Entitys zurück.
     *
     * @return die Mindestanzahl des Entitys.
     */
    String getMinCount();

    /**
     * Gibt die Maximale Anzahl des Entitys zurück.
     *
     * @return die Maximale Anzahl des Entitys.
     */
    String getMaxCount();
}

package tatoo.xml;

import tatoo.VersionNumber;

/**
 *
 * @author friend8
 */
public abstract class Migration implements Comparable<Migration>
{
    /**
     * Überführt eine Datenstruktur in eine höhere Version.
     */
    public abstract void up();

    /**
     * Überführt eine Datenstruktur in eine niedrigere Version.
     */
    public abstract void down();

    /**
     * Gibt die Versionsnummer der Migration zurück.
     *
     * @return die Versionsnummer der Migration
     */
    public abstract VersionNumber getVersion();
}
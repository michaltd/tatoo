package tatoo;

import tatoo.db.Dataset;

/**
 * Eine einfache Implementation einer Versionsnummer. Die Versionsnummer wird
 * standardmäßig in dem Format dd.dddd.dd gespeichert wobei der Buchstabe d
 * einer Zahl entspricht. Verschiedene Versionsnummern sind untereinander
 * Vergleichbar.
 *
 * @author mkortz
 */
public class VersionNumber extends Dataset implements Comparable<VersionNumber>
{
    private int version = 0;
    private final String format = "\\d\\d\\.\\d\\d\\d\\d\\.\\d\\d";

    /**
     * Do NOT use!
     * <p>
     * leerer Konstruktor ist für das Instanziieren aus dem Datenbankframework.
     * NICHT löschen!
     */
    public VersionNumber()
    {
    }

    /**
     * Konstruktor welcher eine Versionsnummer als String übergeben bekommt. Die
     * Versionsnummer muss dem voreingestellten Format dd.dddd.dd entsprechen.
     * Wobei der Buchstabe d einer Zahl entpsricht (z.B. 12.3456.78)
     *
     * @param versionAsString Die Versionsnummer als String.
     */
    public VersionNumber(String versionAsString)
    {
        version = versionToInt(versionAsString);
    }

    /**
     * Initialisiert eine neue Versionsnummer mit einem long Wert. Der long Wert
     * muss gleich viel oder weniger Zahlen beinhalten wie es das Format der
     * Version vorgibt. Bei zu wenig Zahlen wird von links mit nullen (0)
     * aufgefüllt.
     *
     * @param versionNumber
     */
    public VersionNumber(int versionNumber)
    {
        // versionsnummer zuweisen
        version = versionNumber;
        // von Links mit 0 auffüllen
        // version = versionToInt(this.toString());
    }

    /**
     * Wandelt einen Versionsstring im Format dd.dddd.dd in einen long Wert um.
     *
     * @param versionAsString Der Versionsstring
     * @return den long Wert des Versionsstrings
     */
    private int versionToInt(String versionAsString)
    {
        if (!versionAsString.matches(format)) {
            throw new NumberFormatException("Versionstring does not match excepted format: " + format);
        }

        return Integer.parseInt(versionAsString.replaceAll("\\.", ""));
    }

    /**
     * Gibt die Versionsnummer als String zurück.
     */
    @Override
    public String toString()
    {
        if (version == 0) {
            throw new VersionnumberNotInitializedException();
        }

        char[] charArr = ((Integer) version).toString().toCharArray();
        char[] resArr = new char[8];
        byte offset = (byte) (resArr.length - charArr.length);

        for (int i = 0; i < resArr.length; i++) {
            if (i < offset) {
                resArr[i] = '0';
            } else {
                resArr[i] = charArr[i - offset];
            }
        }

        return "" + resArr[0] + resArr[1] + "." + resArr[2] + resArr[3] + resArr[4] + resArr[5]
            + "." + resArr[6] + resArr[7];
    }

    /**
     * Gibt die Versionsnummer als int Wert zurück. Der Wert entspricht der
     * Versionsnummer ohne trennende Punkte.
     *
     * @return die Versionsnummer
     */
    public int intValue()
    {
        if (version == 0) {
            throw new VersionnumberNotInitializedException();
        }

        return version;
    }

    @Override
    public int compareTo(VersionNumber o)
    {
        return Integer.compare(this.intValue(), o.intValue());
    }

    /**
     * Gibt die Majorversionsnummer als int zurück.
     *
     * @return Majorversion als int
     */
    public int getMajorVersion()
    {
        return (intValue() - (intValue() % 100000)) / 100000;
    }

    /**
     * Gibt die Minorversionsnummer als int zurück.
     *
     * @return Minorversion als int
     */
    public int getMinorVersion()
    {
        return (intValue() % 100000) / 100;
    }
}
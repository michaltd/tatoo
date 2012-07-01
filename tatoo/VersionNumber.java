
package tatoo;

import tatoo.db.Dataset;

/**
 * Eine einfache Implementation einer Versionsnummer. Die Versionsnummer 
 * wird standardmäßig in dem Format dd.dddd.dd gespeichert wobei der Buchstabe d einer Zahl entspricht. 
 * Das Format ist anpassbar. Versionsnummern sind untereinander Vergleichbar. Die Versionsnummer kann auch 
 * als long zurückgegeben werden. Der Wert entspricht der Versionsnummer ohne die Trennpunkte.
 * @author mkortz
 *
 */
public class VersionNumber extends Dataset implements Comparable<VersionNumber>{

	private int version = 0;
	private String format = "\\d\\d\\.\\d\\d\\d\\d\\.\\d\\d";
	
	/**
	 * Konstruktor welcher eine Versionsnummer als String übergeben bekommt. Die Versionsnummer muss dem voreingestellten Format
	 * dd.dddd.dd entsprechen. Wobei der Buchstabe d einer Zahl entpsricht (z.B. 12.3456.78)
	 * @param versionAsString Die Versionsnummer als String.
	 * @throws NumberFormatException
	 */
	public VersionNumber(String versionAsString){
		version = versionToInt(versionAsString);
	}
	
	/**
	 * Initialisiert eine neue Versionsnummer mit einem long Wert. Der long Wert muss gleich viel oder weniger
	 * Zahlen beinhalten wie es das Format der Version vorgibt. Bei zu wenig Zahlen wird von links mit nullen (0) aufgefüllt.  
	 * @param versionNumber
	 */
	public VersionNumber(int versionNumber){
	  // versionsnummer zuweisen
		version = versionNumber;
		// von Links mit 0 auffüllen
		version = versionToInt(this.toString());
	}
	
	// privater leerer Konstruktor. Ist für das Instanziieren aus dem Datenbankframework. NICHT löschen.
	public VersionNumber(){};
	
	/**
	 * Wandelt einen Versionsstring im Format dd.dddd.dd in einen long Wert um.
	 * @param versionAsString Der Versionsstring
	 * @return den long Wert des Versionsstrings
	 * @throws NumberFormatException
	 */
	private int versionToInt(String versionAsString){
	  if (!versionAsString.matches(format))
      throw new NumberFormatException("Versionstring does not match excepted format: " + format);
    return new Integer(versionAsString.replaceAll("\\.", "")).intValue();
	}
	
	/**
	 * Gibt die Versionsnummer als String zurück. Der String hat das Format dd.dddd.dd wobei d einer Zahl entspricht.
	 */
	@Override
	public String toString(){
	  if (version == 0)
        throw new tatoo.Exceptions.VersionnumberNotInitializedException();
		char[] charArr = ((Integer)version).toString().toCharArray();
		char[] resArr = new char[8];
		byte offset = (byte) (resArr.length - charArr.length);
		
		for (int i = 0; i < resArr.length; i++)
			if (i < offset)
				resArr[i]= '0';
			else
				resArr[i]=charArr[i-offset];
		return new String("" + resArr[0] + resArr[1] + "." + resArr[2] + resArr[3] + resArr[4] + resArr[5] + "." + resArr[6] + resArr[7]);
	}
	
	/**
	 * Gibt die Versionsnummer als long-Wert zurück. Der Wert entspricht der Versionsnummer ohne trennende Punkte.
	 * @return die Versionsnummer als long.
	 */
	public int intValue(){
	  if (version == 0)
      throw new tatoo.Exceptions.VersionnumberNotInitializedException();
		return version;
	}

	/**
	 * Vergleicht zwei Versionsnummern Numerisch.
	 * @return Gibt 0 Zurück wenn beide Versionsnummern gleich sind. Den Wert kleiner 0 wenn diese Versionsnummer kleiner ist als 
	 * die als Parameter übergebene Versionsnummer und einen Wert größer 0 wenn diese Versionsnummer größer als die übergebene ist.
   */
	@Override
	public int compareTo(VersionNumber o) {
		return new Integer(this.intValue()).compareTo(new Integer(o.intValue()));
	}
	
	public int getMajorVersion(){
	  return new Integer((intValue() - (intValue() % 100000)) / 100000).intValue();
	}
	
	public int getMinorVersion(){
	  return new Integer((intValue() % 100000)/100).intValue();
	}
	
}
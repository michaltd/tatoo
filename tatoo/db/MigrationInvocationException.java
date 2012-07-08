package tatoo.db;

/**
 * Wird erzeugt wenn eine Migration fehlschlägt.
 * @author mkortz
 *
 */
public class MigrationInvocationException extends RuntimeException {

  public MigrationInvocationException(String message) 
  {
    super(message);
  }
  
  

}

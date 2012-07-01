package tatoo.db.sql;

public class DBSchemaDefinition {
  
  public enum SchemaType           
  {
    CLASS, PROPERTY, SET;
  }
  
  protected SchemaType definitonType;
  protected String name;
  
  public String getName(){
    return name;
  }

  public SchemaType getSchemaType() {
    return definitonType;
  }

}

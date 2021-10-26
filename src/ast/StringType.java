package ast;

public class StringType extends Type {
  public StringType() {
    super("string");
  }

  public String genC() {
    return "string";
  }
}

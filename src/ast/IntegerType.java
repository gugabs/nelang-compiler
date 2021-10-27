package ast;

public class IntegerType extends Type {
  public IntegerType() {
    super("integer");
  }

  public String genC() {
    return "int";
  }
}

package ast;

public class IntegerType extends Type {
  public IntegerType() {
    super("int");
  }

  public String genC() {
    return "int";
  }
}

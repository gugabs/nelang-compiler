package ast;

import java.util.Map;

public class StringExpr extends Expr {

  private String value;

  public StringExpr(String value) {
    this.value = value;
  }

  @Override
  public void genC() {
    System.out.print("\"" + value + "\"");
  }

  public String getValue() {
    return value;
  }

  @Override
  public Type getType() {
    return Type.stringType;
  }

  public String eval(Map<String, Object> memory) {
    return this.value;
  }
}

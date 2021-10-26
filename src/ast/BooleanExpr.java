package ast;

import java.util.Map;

public class BooleanExpr extends Expr {

  public BooleanExpr(boolean value) {
    this.value = value;
  }

  @Override
  public void genC() {
    System.out.print(value ? "1" : "0");
  }

  @Override
  public Type getType() {
    return Type.booleanType;
  }

  public static BooleanExpr True = new BooleanExpr(true);
  public static BooleanExpr False = new BooleanExpr(false);

  public Object eval(Map<String, Object> memory) {
    return this.value;
  }

  private boolean value;
}

package ast;

import java.util.Map;

public class Numero extends Expr {
  private int number;

  public Numero(int number) {
    super();
    this.number = number;
  }

  public int getValue() {
    return number;
  }

  @Override
  public Object eval(Map<String, Object> memory) {
    return this.number;
  }

  @Override
  public void genC() {
    System.out.print(number);
  }

  public Type getType() {
    return Type.integerType;
  }

}

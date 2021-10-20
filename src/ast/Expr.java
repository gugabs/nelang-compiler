package ast;

import java.util.Map;

public abstract class Expr {
  public abstract int eval(Map<String, Integer> memory);

  public abstract void genC();
}

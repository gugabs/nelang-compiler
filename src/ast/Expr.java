package ast;

import java.util.Map;

public abstract class Expr {  
  public abstract Object eval(Map<String, Object> memory);
  public abstract void genC();
  public abstract Type getType();
}

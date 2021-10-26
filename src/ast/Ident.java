package ast;

import java.util.Map;

public class Ident extends Expr {

  private String name;
  private Type type;

  public Ident(String name) {
    this.name = name;
  }

  public Ident(String name, Type type) {
    this.name = name;
    this.type = type;
  }

  public Object eval(Map<String, Object> memory) {
    return memory.get(this.name);
  }

  public void genC() {
    System.out.print(name);
  }

  public String getName() {
    return name;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }
}

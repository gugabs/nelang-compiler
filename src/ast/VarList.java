package ast;

import java.util.Map;

public class VarList extends Stat {

  private Ident id;

  public VarList(Ident id) {
    this.id = id;
  }

  public void eval(Map<String, Object> memory) {
    if (memory.containsKey(id.getName()))
      throw new RuntimeException("Error: duplicated identifier.");
  }

  public void genC() {
    System.out.print(id.getType().getName() + ' ');
    id.genC();
    System.out.println(';');
  }
}

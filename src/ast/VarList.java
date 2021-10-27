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
    String size = id.getType() == Type.stringType ? "[255]" : " ";
    
    System.out.print(id.getType().genC() + " ");
    id.genC();
    System.out.println(size + ';');
  }
}

package ast;

import java.util.Map;

public class Program {
  private StatList statList;
  private VarList varList;

  public Program(StatList statList, VarList varList) {
    super();
    this.statList = statList;
    this.varList = varList;
  }

  public void eval(Map<String, Integer> memory) {
    this.varList.eval(memory);
    this.statList.eval(memory);
  }

  public void genC() {

    System.out.println("#include <stdio.h>");
    System.out.println("int main() {");
    varList.genC();
    statList.genC();
    System.out.println("return 0;\n}");
  }
}

package ast;

import java.util.Map;

public class Program {
  private StatList statList;

  public Program(StatList statList) {
    super();
    this.statList = statList;
  }

  public void eval(Map<String, Object> memory) {
    this.statList.eval(memory);
  }

  public void genC() {

    System.out.println("#include <stdio.h>");
    System.out.println("int main() {");
    statList.genC();
    System.out.println("return 0;\n}");
  }
}

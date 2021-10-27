package ast;

import java.util.HashMap;
import java.util.Map;

public class Program {
  private StatList statList;

  public Program(StatList statList) {
    super();
    this.statList = statList;
  }

  public void eval() {
    Map<String, Object> memory = new HashMap<>();
    this.statList.eval(memory);
  }

  public void run() {
    this.eval();
  }

  public void genC() {

    System.out.println("#include <stdio.h>\n#include <string.h>");
    System.out.println("int main() {");
    statList.genC();
    System.out.println("return 0;\n}");
  }
}

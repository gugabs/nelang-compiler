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

    System.out.println("#include <stdio.h>\n#include <stdbool.h>\n#include <string.h>");

    System.out.println("char buffer[255];");

    System.out.println(
        "char* bool2string(bool val);\r\n" + "char* confirmString(char* val);\r\n" + "char* int2string(int val);");

    System.out.println("int main() {");
    statList.genC();
    System.out.println("return 0;\n}");

    System.out.println("char* bool2string(bool val) {\r\n" + "  if (val) {\r\n" + "      strcpy(buffer, \"true\");\r\n"
        + "  } else {\r\n" + "      strcpy(buffer, \"false\");\r\n" + "  }\r\n" + "  return buffer;\r\n" + "}\r\n"
        + "\r\n" + "char* confirmString(char* val) {\r\n" + "  return val;    \r\n" + "}\r\n" + "\r\n"
        + "char* int2string(int val) {\r\n" + "  sprintf(buffer, \"%d\", val);\r\n" + "  return buffer;\r\n" + "}");
  }
}

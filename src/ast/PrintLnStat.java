package ast;

import java.util.Map;

public class PrintLnStat extends Stat {

  private Expr expr;

  public PrintLnStat(Expr expr) {
    super();
    this.expr = expr;
  }

  public void eval(Map<String, Object> memory) {
    if (expr != null)
      System.out.println(expr.eval(memory));
  }

  @Override
  public void genC() {
    System.out.print("printf(\"%d\\n\", ");
    expr.genC();
    System.out.println(");");
  }

}

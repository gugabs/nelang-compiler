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
    String typeSpecifier;

    if (this.expr.getType() == Type.integerType || this.expr.getType() == Type.booleanType) {
      typeSpecifier = "%d";

      System.out.print("printf(\"" + typeSpecifier + "\\n\", ");
      expr.genC();
      System.out.println(");");
    } else {
      if (expr instanceof StringExpr) {
        System.out.print("printf(");
        expr.genC();
        System.out.println(");");
      } else {
        expr.genC();
        typeSpecifier = "%s";
        System.out.println("printf(\"" + typeSpecifier + "\\n\", buffer);"); 
      }
    }
  }

}

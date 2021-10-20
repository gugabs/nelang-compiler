package ast;

import java.util.Map;

import lexer.Symbol;

public class UnaryExpr extends Expr {
  private Expr expr;
  private Symbol op;

  public UnaryExpr(Expr expr, Symbol op) {
    this.expr = expr;
    this.op = op;
  }

  @Override
  public int eval(Map<String, Integer> memory) {
    int eval = 0;

    if (op == Symbol.NOT) {
      eval = expr.eval(memory); // Não poderia ser !expr.eval(memory)
      eval = eval == 0 ? 1 : 0;
    } else if (op == Symbol.PLUS) {
      eval = +expr.eval(memory);
    } else if (op == Symbol.MINUS) {
      eval = -expr.eval(memory);
    } else {
      throw new RuntimeException("Error: cannot run unary expression");
    }

    return eval;
  }

  @Override
  public void genC() {
    System.out.print(this.op);
    expr.genC();
  }

}

package ast;

import java.util.List;
import java.util.Map;
import java.util.Iterator;

import lexer.Symbol;

public class CompositeExpr extends Expr {
  private Expr left;
  private List<Symbol> op;
  private List<Expr> right;

  public CompositeExpr(Expr left) {
    super();
    this.left = left;
  }

  public CompositeExpr(Expr left, List<Symbol> op, List<Expr> right) {
    super();
    this.left = left;
    this.op = op;
    this.right = right;
  }

  @Override
  public int eval(Map<String, Integer> memory) {
    int leftResult = left.eval(memory);

    if (op != null) {
      Iterator<Symbol> opIterator = op.iterator();
      Iterator<Expr> exprIterator = right.iterator();

      while (exprIterator.hasNext()) {
        Symbol currOp = opIterator.next();
        Expr currExpr = exprIterator.next();

        if (currOp == Symbol.PLUS) {
          leftResult = leftResult + currExpr.eval(memory);
        } else if (currOp == Symbol.MINUS) {
          leftResult = leftResult - currExpr.eval(memory);
        } else if (currOp == Symbol.MULT) {
          leftResult = leftResult * currExpr.eval(memory);
        } else if (currOp == Symbol.DIV) {
          leftResult = leftResult / currExpr.eval(memory);
        } else if (currOp == Symbol.MOD) {
          leftResult = leftResult % currExpr.eval(memory);
        } else if (currOp == Symbol.GREATER) {
          leftResult = leftResult > currExpr.eval(memory) ? 1 : 0;
        } else if (currOp == Symbol.GREATER_E) {
          leftResult = leftResult >= currExpr.eval(memory) ? 1 : 0;
        } else if (currOp == Symbol.LESS) {
          leftResult = leftResult < currExpr.eval(memory) ? 1 : 0;
        } else if (currOp == Symbol.LESS_E) {
          leftResult = leftResult <= currExpr.eval(memory) ? 1 : 0;
        } else if (currOp == Symbol.DIFF) {
          leftResult = leftResult != currExpr.eval(memory) ? 1 : 0;
        } else if (currOp == Symbol.EQUAL) {
          leftResult = leftResult == currExpr.eval(memory) ? 1 : 0;
        } else if (currOp == Symbol.AND) {
          leftResult = leftResult != 0 && currExpr.eval(memory) != 0 ? 1 : 0;
        } else if (currOp == Symbol.OR) {
          leftResult = leftResult != 0 || currExpr.eval(memory) != 0 ? 1 : 0;
        } else {
          throw new RuntimeException("Error: cannot run composite evaluation");
        }
        currExpr.eval(memory);
      }
    }

    return leftResult;
  }

  @Override
  public void genC() {
    System.out.print("(");
    left.genC();
    if (op != null) {
      Iterator<Symbol> opIterator = op.iterator();
      Iterator<Expr> exprIterator = right.iterator();
      while (exprIterator.hasNext()) {
        System.out.print(" ");
        opIterator.next().genC();
        System.out.print(" ");
        exprIterator.next().genC();
      }
    }
    System.out.print(")");
  }

}

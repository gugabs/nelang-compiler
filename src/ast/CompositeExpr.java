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
  public Object eval(Map<String, Object> memory) {
    Object leftResult = left.eval(memory);

    if (op != null) {
      Iterator<Symbol> opIterator = op.iterator();
      Iterator<Expr> exprIterator = right.iterator();

      while (exprIterator.hasNext()) {
        Symbol currOp = opIterator.next();
        Expr currExpr = exprIterator.next();

        if (currOp == Symbol.PLUS) {
          leftResult = (int) leftResult + (int) currExpr.eval(memory);
        } else if (currOp == Symbol.MINUS) {
          leftResult = (int) leftResult - (int) currExpr.eval(memory);
        } else if (currOp == Symbol.MULT) {
          leftResult = (int) leftResult * (int) currExpr.eval(memory);
        } else if (currOp == Symbol.DIV) {
          leftResult = (int) leftResult / (int) currExpr.eval(memory);
        } else if (currOp == Symbol.MOD) {
          leftResult = (int) leftResult % (int) currExpr.eval(memory);
        } else if (currOp == Symbol.GREATER) {
          if (leftResult instanceof Boolean) {
            int oper1 = (boolean) leftResult ? 1 : 0;
            int oper2 = (boolean) currExpr.eval(memory) ? 1 : 0;

            leftResult = oper1 > oper2 ? true : false;
          } else {
            leftResult = (int) leftResult > (int) currExpr.eval(memory) ? 1 : 0;
          }
        } else if (currOp == Symbol.GREATER_E) {
          if (leftResult instanceof Boolean) {
            int oper1 = (boolean) leftResult ? 1 : 0;
            int oper2 = (boolean) currExpr.eval(memory) ? 1 : 0;

            leftResult = oper1 >= oper2 ? true : false;
          } else {
            leftResult = (int) leftResult >= (int) currExpr.eval(memory) ? 1 : 0;
          }
        } else if (currOp == Symbol.LESS) {
          if (leftResult instanceof Boolean) {
            int oper1 = (boolean) leftResult ? 1 : 0;
            int oper2 = (boolean) currExpr.eval(memory) ? 1 : 0;

            leftResult = oper1 < oper2 ? true : false;
          } else {
            leftResult = (int) leftResult < (int) currExpr.eval(memory) ? 1 : 0;
          }
        } else if (currOp == Symbol.LESS_E) {
          if (leftResult instanceof Boolean) {
            int oper1 = (boolean) leftResult ? 1 : 0;
            int oper2 = (boolean) currExpr.eval(memory) ? 1 : 0;

            leftResult = oper1 <= oper2 ? true : false;
          } else {
            leftResult = (int) leftResult <= (int) currExpr.eval(memory) ? 1 : 0;
          }
        } else if (currOp == Symbol.DIFF) {
          if (leftResult instanceof Boolean) {
            int oper1 = (boolean) leftResult ? 1 : 0;
            int oper2 = (boolean) currExpr.eval(memory) ? 1 : 0;

            leftResult = oper1 != oper2 ? true : false;
          } else {
            leftResult = (int) leftResult != (int) currExpr.eval(memory) ? 1 : 0;
          }
        } else if (currOp == Symbol.EQUAL) {
          if (leftResult instanceof Boolean) {
            int oper1 = (boolean) leftResult ? 1 : 0;
            int oper2 = (boolean) currExpr.eval(memory) ? 1 : 0;

            leftResult = oper1 == oper2 ? true : false;
          } else {
            leftResult = (int) leftResult == (int) currExpr.eval(memory) ? 1 : 0;
          }
        } else if (currOp == Symbol.AND) {
          if (leftResult instanceof Boolean) {
            boolean oper1 = (boolean) leftResult;
            boolean oper2 = (boolean) currExpr.eval(memory);

            leftResult = oper1 && oper2 ? true : false;
          } else {
            leftResult = (int) leftResult != 0 && (int) currExpr.eval(memory) != 0 ? 1 : 0;
          }
        } else if (currOp == Symbol.OR) {
          if (leftResult instanceof Boolean) {
            boolean oper1 = (boolean) leftResult;
            boolean oper2 = (boolean) currExpr.eval(memory);

            leftResult = oper1 || oper2 ? true : false;
          } else {
            leftResult = (int) leftResult != 0 || (int) currExpr.eval(memory) != 0 ? 1 : 0;
          }
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

  public Type getType() {
    Symbol firstOp = op.get(0);

    if (firstOp == Symbol.AND || firstOp == Symbol.OR || firstOp == Symbol.NOT || firstOp == Symbol.LESS
        || firstOp == Symbol.LESS_E || firstOp == Symbol.GREATER || firstOp == Symbol.GREATER_E
        || firstOp == Symbol.EQUAL || firstOp == Symbol.DIFF) {
      return Type.booleanType;
    } else {
      return Type.integerType;
    }
  }
}

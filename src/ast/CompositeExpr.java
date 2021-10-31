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
          } else if (leftResult instanceof String) {
            String str1 = leftResult.toString();
            String str2 = currExpr.eval(memory).toString();

            return str1.compareToIgnoreCase(str2) > 0 ? true : false;
          } else {
            leftResult = (int) leftResult > (int) currExpr.eval(memory) ? 1 : 0;
          }
        } else if (currOp == Symbol.GREATER_E) {
          if (leftResult instanceof Boolean) {
            int oper1 = (boolean) leftResult ? 1 : 0;
            int oper2 = (boolean) currExpr.eval(memory) ? 1 : 0;

            leftResult = oper1 >= oper2 ? true : false;
          } else if (leftResult instanceof String) {
            String str1 = leftResult.toString();
            String str2 = currExpr.eval(memory).toString();

            return str1.compareToIgnoreCase(str2) >= 0 ? true : false;
          } else {
            leftResult = (int) leftResult >= (int) currExpr.eval(memory) ? 1 : 0;
          }
        } else if (currOp == Symbol.LESS) {
          if (leftResult instanceof Boolean) {
            int oper1 = (boolean) leftResult ? 1 : 0;
            int oper2 = (boolean) currExpr.eval(memory) ? 1 : 0;

            leftResult = oper1 < oper2 ? true : false;
          } else if (leftResult instanceof String) {
            String str1 = leftResult.toString();
            String str2 = currExpr.eval(memory).toString();

            return str1.compareToIgnoreCase(str2) < 0 ? true : false;
          } else {
            leftResult = (int) leftResult < (int) currExpr.eval(memory) ? 1 : 0;
          }
        } else if (currOp == Symbol.LESS_E) {
          if (leftResult instanceof Boolean) {
            int oper1 = (boolean) leftResult ? 1 : 0;
            int oper2 = (boolean) currExpr.eval(memory) ? 1 : 0;

            leftResult = oper1 <= oper2 ? true : false;
          } else if (leftResult instanceof String) {
            String str1 = leftResult.toString();
            String str2 = currExpr.eval(memory).toString();

            return str1.compareToIgnoreCase(str2) <= 0 ? true : false;
          } else {
            leftResult = (int) leftResult <= (int) currExpr.eval(memory) ? 1 : 0;
          }
        } else if (currOp == Symbol.DIFF) {
          if (leftResult instanceof Boolean) {
            int oper1 = (boolean) leftResult ? 1 : 0;
            int oper2 = (boolean) currExpr.eval(memory) ? 1 : 0;

            leftResult = oper1 != oper2 ? true : false;
          } else if (leftResult instanceof String) {
            leftResult = leftResult.equals(currExpr.eval(memory)) ? false : true;
          } else {
            leftResult = (int) leftResult != (int) currExpr.eval(memory) ? 1 : 0;
          }
        } else if (currOp == Symbol.EQUAL) {
          if (leftResult instanceof Boolean) {
            int oper1 = (boolean) leftResult ? 1 : 0;
            int oper2 = (boolean) currExpr.eval(memory) ? 1 : 0;

            leftResult = oper1 == oper2 ? true : false;
          } else if (leftResult instanceof String) {
            leftResult = leftResult.equals(currExpr.eval(memory)) ? true : false;
          } else {
            leftResult = (int) leftResult == (int) currExpr.eval(memory) ? 1 : 0;
          }
        } else if (currOp == Symbol.AND) {
          if (leftResult instanceof Boolean) {
            boolean oper1 = (boolean) leftResult;
            boolean oper2 = (boolean) currExpr.eval(memory);

            leftResult = oper1 && oper2 ? true : false;
          } else if (currExpr.eval(memory) instanceof Boolean) {
            leftResult = (int) leftResult != 0 && (boolean) currExpr.eval(memory) != false ? true : false;
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
        } else if (currOp == Symbol.CONCAT) {
          leftResult += currExpr.eval(memory).toString();
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
    if (op == null) {
      left.genC();
    } else {
      Iterator<Symbol> opIterator = op.iterator();
      Iterator<Expr> exprIterator = right.iterator();

      Symbol currOper = opIterator.next();

      if (currOper != Symbol.CONCAT) {
        if (left.getType() == Type.stringType) {
          Expr currExpr = exprIterator.next();

          if (currOper == Symbol.LESS) {
            System.out.print("strcmp(");
            left.genC();
            System.out.print(", ");
            currExpr.genC();
            System.out.print(") < 0");
          } else if (currOper == Symbol.LESS_E) {
            System.out.print("strcmp(");
            left.genC();
            System.out.print(", ");
            currExpr.genC();
            System.out.print(") <= 0");
          } else if (currOper == Symbol.GREATER) {
            System.out.print("strcmp(");
            left.genC();
            System.out.print(", ");
            currExpr.genC();
            System.out.print(") > 0");
          } else if (currOper == Symbol.GREATER_E) {
            System.out.print("strcmp(");
            left.genC();
            System.out.print(", ");
            currExpr.genC();
            System.out.print(") >= 0");
          } else if (currOper == Symbol.EQUAL) {
            System.out.print("strcmp(");
            left.genC();
            System.out.print(", ");
            currExpr.genC();
            System.out.print(") == 0");
          } else if (currOper == Symbol.DIFF) {
            System.out.print("strcmp(");
            left.genC();
            System.out.print(", ");
            currExpr.genC();
            System.out.print(") != 0");
          }
        } else {
          left.genC();
        }
      } else {

        if (left instanceof CompositeExpr)
          left.genC();
        else {
          System.out.println("strcpy(buffer, \"\\0\");");
          System.out.print("strcat(buffer, ");
          
          if (left.getType() == Type.integerType)
            System.out.print("int2string(");
          else if (left.getType() == Type.booleanType)
            System.out.print("bool2string(");
          else
            System.out.print("confirmString(");

          left.genC();

          System.out.print(")");
          System.out.println(");");
        }
      }

      if (currOper == Symbol.CONCAT) {
        while (opIterator.hasNext() && currOper == Symbol.CONCAT) {
          if (exprIterator.hasNext()) {

            Expr currExpr = exprIterator.next();

            System.out.print("strcat(buffer, ");

            if (currExpr.getType() == Type.integerType)
              System.out.print("int2string(");
            else if (currExpr.getType() == Type.booleanType)
              System.out.print("bool2string(");
            else
              System.out.print("confirmString(");

            currExpr.genC();

            System.out.print(")");
            System.out.println(");");
          }

          currOper = opIterator.next();
        }

        Expr currExpr = exprIterator.next();

        System.out.print("strcat(buffer, ");

        if (currExpr.getType() == Type.integerType)
          System.out.print("int2string(");
        else if (currExpr.getType() == Type.booleanType)
          System.out.print("bool2string(");
        else
          System.out.print("confirmString(");

        currExpr.genC();

        System.out.print(")");
        System.out.println(");");
      } else {
        while (exprIterator.hasNext()) {
          System.out.print(" ");
          currOper.genC();
          System.out.print(" ");
          exprIterator.next().genC();

          if (opIterator.hasNext())
            currOper = opIterator.next();
        }
      }
    }

  }

  public Type getType() {
    Symbol firstOp = op.get(0);

    if (firstOp == Symbol.AND || firstOp == Symbol.OR || firstOp == Symbol.NOT || firstOp == Symbol.LESS
        || firstOp == Symbol.LESS_E || firstOp == Symbol.GREATER || firstOp == Symbol.GREATER_E
        || firstOp == Symbol.EQUAL || firstOp == Symbol.DIFF) {
      return Type.booleanType;
    } else if (firstOp == Symbol.CONCAT) {
      return Type.stringType;
    } else {
      return Type.integerType;
    }
  }
}

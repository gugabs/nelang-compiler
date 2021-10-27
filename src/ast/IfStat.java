package ast;

import java.util.Map;

public class IfStat extends Stat {

  private Expr left;
  private StatList statList;
  private StatList statElse;

  public IfStat(Expr left, StatList statList) {
    super();
    this.left = left;
    this.statList = statList;
  }

  public IfStat(Expr left, StatList statList, StatList statElse) {
    super();
    this.left = left;
    this.statList = statList;
    this.statElse = statElse;
  }

  @Override
  public void eval(Map<String, Object> memory) {
    int leftEval;

    if (left.eval(memory) instanceof Boolean)
      leftEval = (boolean) left.eval(memory) ? 1 : 0;
    else
      leftEval = (int) left.eval(memory);

    if (leftEval != 0)
      statList.eval(memory);
    else if (statElse != null)
      statElse.eval(memory);
  }

  @Override
  public void genC() {
    System.out.print("if (");
    left.genC();
    System.out.println(") {");
    statList.genC();
    if (statElse == null) {
      System.out.println("}");
    } else {
      System.out.println("} else {");
      statElse.genC();
      System.out.println("}");
    }

  }
}

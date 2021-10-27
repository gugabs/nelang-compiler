package lexer;

public enum Symbol {
	  EOF("eof"),
	  ID("Identifier"),
	  NUMBER("Number"),
	  PLUS("+"),
	  MINUS("-"),
	  MULT("*"),
	  DIV("/"),
	  MOD("%"),
	  LESS("<"),
	  LESS_E("<="),
	  GREATER(">"),
	  GREATER_E(">="),
	  DIFF("!="),
	  EQUAL("=="),
	  AND("&&"),
	  OR("||"),
	  ASSIGN("="),
	  LEFT_P("("),
	  RIGHT_P(")"),
	  LEFT_B("{"),
	  RIGHT_B("}"),
	  VAR("var"),
	  INT("int"),
	  STRING("string"),
	  BOOLEAN("boolean"),
	  IF("if"),
	  ELSE("else"),
	  NOT("!"),
	  ENDIF("endif"),
	  WHILE("while"),
	  FOR("for"),
	  IN("in"),
	  PRINT("print"),
	  PRINTLN("println"),
	  COMMA(","),
	  SEMICOLON(";"),
	  RANGE(".."),
	  READ("read"),
	  WRITE("write"),
	  TRUE("true"),
    FALSE("false"),
    QUOTATION_M("\""),
    CONCAT("++");

	  Symbol(String name) {
	    this.name = name;
	  }

	  @Override
	  public String toString() {
	    return name;
	  }

	  public String name;

	  public void genC() {
	    System.out.print(name);
	  }
}

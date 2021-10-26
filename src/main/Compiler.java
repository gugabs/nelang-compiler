package main;

import java.lang.Character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Hashtable;
import java.util.List;

import ast.AssignStat;
import ast.BooleanExpr;
import ast.CompositeExpr;
import ast.Expr;
import ast.Ident;
import ast.IfStat;
import ast.Numero;
import ast.PrintLnStat;
import ast.PrintStat;
import ast.Stat;
import ast.StatList;
import ast.StringExpr;
import ast.Type;
import ast.UnaryExpr;
import ast.VarList;
import ast.WhileStat;
import ast.Program;
import ast.ForStat;
import lexer.Symbol;

public class Compiler {
  public Symbol token;
  private int tokenPos;

  private char[] input;

  static private Hashtable<String, Symbol> keywordsTable;

  static {
    keywordsTable = new Hashtable<String, Symbol>();

    keywordsTable.put("var", Symbol.VAR);
    keywordsTable.put("int", Symbol.INT);
    keywordsTable.put("string", Symbol.STRING);
    keywordsTable.put("boolean", Symbol.BOOLEAN);
    keywordsTable.put("if", Symbol.IF);
    keywordsTable.put("else", Symbol.ELSE);
    keywordsTable.put("not", Symbol.NOT);
    keywordsTable.put("and", Symbol.AND);
    keywordsTable.put("or", Symbol.OR);
    keywordsTable.put("for", Symbol.FOR);
    keywordsTable.put("in", Symbol.IN);
    keywordsTable.put("while", Symbol.WHILE);
    keywordsTable.put("print", Symbol.PRINT);
    keywordsTable.put("println", Symbol.PRINTLN);
    keywordsTable.put("true", Symbol.TRUE);
    keywordsTable.put("false", Symbol.FALSE);
  }

  static private Map<String, Ident> identObjects = new HashMap<>();

  private int numberValue;
  private String variableName;

  public Compiler(char[] expr) {
    this.tokenPos = 0;
    this.input = expr;
  }

  public Program compile() {
    Program p = program();
    if (token != Symbol.EOF)
      throw new RuntimeException("Fim do arquivo n�o esperado");
    return p;
  }

  private Program program() {
    this.nextToken();

    List<Stat> statList = new ArrayList<Stat>();

    while (token == Symbol.VAR || token == Symbol.ID || token == Symbol.FOR || token == Symbol.PRINT
        || token == Symbol.PRINTLN || token == Symbol.WHILE || token == Symbol.IF) {
      statList.add(this.stat());
    }

    StatList sl = new StatList(statList);

    this.nextToken();
    return new Program(sl);
  }

  private Ident varList() {
    if (this.token != Symbol.VAR)
      throw new RuntimeException("Error: expected 'var'");

    this.nextToken();

    if (this.token != Symbol.INT && this.token != Symbol.STRING && this.token != Symbol.BOOLEAN)
      throw new RuntimeException("Error: expected a type");

    Type type = getType();

    this.nextToken();

    if (this.token != Symbol.ID)
      throw new RuntimeException("Error: expected an identifier");

    Ident id = new Ident(variableName, type);

    this.nextToken();

    if (this.token != Symbol.SEMICOLON)
      throw new RuntimeException("Error: expected ';'");

    this.nextToken();

    identObjects.put(id.getName(), id);

    return id;
  }

  private void nextToken() {
    if (this.tokenPos < this.input.length - 1 && input[tokenPos] == '/' && input[tokenPos + 1] == '/') {
      while (this.tokenPos < this.input.length - 1 && input[tokenPos] != '\0' && input[tokenPos] != '\n')
        tokenPos++;
      nextToken();
    } else if (this.tokenPos < this.input.length - 1 && input[tokenPos] == '/' && input[tokenPos + 1] == '*') {
      while (this.tokenPos < this.input.length - 1 && input[tokenPos] != '*' && input[tokenPos + 1] != '/')
        tokenPos++;
      nextToken();
    }

    while (this.tokenPos < this.input.length && (this.input[this.tokenPos] == ' ' || this.input[this.tokenPos] == '\r'
        || this.input[this.tokenPos] == '\n' || this.input[this.tokenPos] == '\t'))
      this.tokenPos++;

    if (this.tokenPos >= input.length) {
      token = Symbol.EOF;
      return;
    }

    char currCharacter = Character.toLowerCase(this.input[this.tokenPos]);

    if (Character.isLetter(currCharacter)) {
      this.getWord();
    } else if (Character.isDigit(currCharacter)) {
      this.getNumber();
    } else {
      switch (currCharacter) {
      case '+':
        token = Symbol.PLUS;
        break;

      case '-':
        token = Symbol.MINUS;
        break;

      case '*':
        token = Symbol.MULT;
        break;

      case '/':
        token = Symbol.DIV;
        break;

      case '%':
        token = Symbol.MOD;
        break;

      case '=':
        if (this.input[tokenPos + 1] == '=') {
          token = Symbol.EQUAL;
          this.tokenPos++;
        } else {
          token = Symbol.ASSIGN;
        }
        break;

      case '>':
        if (this.input[tokenPos + 1] == '=') {
          token = Symbol.GREATER_E;
          this.tokenPos++;
        } else {
          token = Symbol.GREATER;
        }
        break;

      case '<':
        if (this.input[tokenPos + 1] == '=') {
          token = Symbol.LESS_E;
          this.tokenPos++;
        } else {
          token = Symbol.LESS;
        }

        break;

      case '!':
        if (this.input[tokenPos + 1] == '=') {
          token = Symbol.DIFF;
          this.tokenPos++;
        } else {
          token = Symbol.NOT;
        }

        break;

      case '&':
        if (this.input[tokenPos + 1] == '&') {
          token = Symbol.AND;
          this.tokenPos++;
        }
        break;

      case '|':
        if (this.input[tokenPos + 1] == '|') {
          token = Symbol.OR;
          this.tokenPos++;
        }
        break;

      case '(':
        token = Symbol.LEFT_P;
        break;

      case ')':
        token = Symbol.RIGHT_P;
        break;

      case '{':
        token = Symbol.LEFT_B;
        break;

      case '}':
        token = Symbol.RIGHT_B;
        break;

      case ',':
        token = Symbol.COMMA;
        break;

      case ';':
        token = Symbol.SEMICOLON;
        break;

      case '"':
        token = Symbol.QUOTATION_M;
        break;

      case '.':
        if (this.input[tokenPos + 1] == '.') {
          token = Symbol.RANGE;
          this.tokenPos++;
        }
        break;

      case '\0':
        token = Symbol.EOF;
        break;
      }

      this.tokenPos++;
    }

  }

  private void getWord() {
    StringBuffer identifier = new StringBuffer();

    while (tokenPos < this.input.length && this.input[tokenPos] != ' ' && Character.isLetter(this.input[this.tokenPos])
        || Character.isDigit(this.input[tokenPos])) {
      identifier.append(this.input[tokenPos]);
      this.tokenPos++;
    }

    Symbol value = keywordsTable.get(identifier.toString().toLowerCase());
    this.variableName = identifier.toString();

    if (value == null)
      this.token = Symbol.ID;
    else
      this.token = value;
  }

  private void getNumber() {
    StringBuffer number = new StringBuffer();

    while (tokenPos < this.input.length && this.input[tokenPos] != ' '
        && Character.isDigit(this.input[this.tokenPos])) {
      number.append(this.input[tokenPos]);
      this.tokenPos++;
    }

    this.token = Symbol.NUMBER;
    this.numberValue = Integer.valueOf(number.toString());
  }

  private Type getType() {
    Type currType;

    switch (this.token) {
    case INT:
      currType = Type.integerType;
      break;
    case BOOLEAN:
      currType = Type.booleanType;
      break;
    case STRING:
      currType = Type.stringType;
      break;
    default:
      throw new RuntimeException("Error: ...");
    }

    return currType;
  }

  private Stat stat() {
    switch (this.token) {
    case VAR:
      Ident id = this.varList();
      return new VarList(id);
    case ID:
      return this.assignStat();
    case FOR:
      return this.forStat();
    case WHILE:
      return this.whileStat();
    case PRINT:
      return this.printStat();
    case PRINTLN:
      return this.printlnStat();
    case IF:
      return this.ifStat();
    default:
      throw new RuntimeException("Error: expected a statement.");
    }
  }

  private StatList statList() {
    if (this.token != Symbol.LEFT_B)
      throw new RuntimeException("Error: expected '{'.");

    this.nextToken();

    List<Stat> statList = new ArrayList<Stat>();

    while (this.token == Symbol.VAR || this.token == Symbol.ID || this.token == Symbol.FOR || this.token == Symbol.WHILE
        || this.token == Symbol.PRINT || this.token == Symbol.PRINTLN || this.token == Symbol.IF) {
      statList.add(this.stat());
    }

    if (this.token != Symbol.RIGHT_B)
      throw new RuntimeException("Error: expected '}'.");

    this.nextToken();
    return new StatList(statList);
  }

  private AssignStat assignStat() {

    Ident id = identObjects.get(this.variableName);

    this.nextToken();
    if (this.token != Symbol.ASSIGN)
      throw new RuntimeException("Error: expected '='.");

    this.nextToken();

    Expr e = this.expr();

    if (id.getType() != e.getType())
      throw new RuntimeException("Error: expected same type");

    if (this.token != Symbol.SEMICOLON)
      throw new RuntimeException("Error: expected ';'.");

    this.nextToken();
    return new AssignStat(id, e);
  }

  private IfStat ifStat() {
    this.nextToken();

    Expr e = this.expr();

    if (e.getType() != Type.booleanType)
      throw new RuntimeException("Error: Expected a boolean expression");

    StatList sl = this.statList();

    if (this.token == Symbol.ELSE) {
      this.nextToken();
      StatList listElse = this.statList();
      return new IfStat(e, sl, listElse);
    }

    return new IfStat(e, sl);
  }

  private ForStat forStat() {
    this.nextToken();

    if (this.token == Symbol.ID) {
      Ident id = new Ident(variableName, Type.integerType);
      identObjects.put(variableName, id);

      this.nextToken();

      if (this.token != Symbol.IN)
        throw new RuntimeException("Error: expected 'in'.");

      this.nextToken();

      Expr left = this.expr();

      if (this.token != Symbol.RANGE)
        throw new RuntimeException("Error: expected '..'.");

      this.nextToken();

      Expr right = this.expr();
      StatList forList = this.statList();

      ForStat fs = new ForStat(id, left, right, forList);

      identObjects.remove(id.getName());

      return fs;

    } else {
      throw new RuntimeException("Error: expected an identifier.");
    }
  }

  private WhileStat whileStat() {
    this.nextToken();

    Expr e = this.expr();

    if (e.getType() != Type.booleanType)
      throw new RuntimeException("Error: Expected a boolean expression");

    StatList sl = this.statList();

    return new WhileStat(e, sl);
  }

  private PrintStat printStat() {
    this.nextToken();

    Expr e = this.expr();

    if (this.token != Symbol.SEMICOLON)
      throw new RuntimeException("Error: expected ';'.");

    this.nextToken();

    return new PrintStat(e);
  }

  private PrintLnStat printlnStat() {
    this.nextToken();

    Expr e = this.expr();

    if (this.token != Symbol.SEMICOLON)
      throw new RuntimeException("Error: expected ';'.");

    this.nextToken();

    return new PrintLnStat(e);
  }

  private Expr expr() {
    Expr left = this.andExpr();

    List<Symbol> opList = new ArrayList<Symbol>();
    List<Expr> rightList = new ArrayList<Expr>();

    if (this.token == Symbol.OR) {
      opList.add(this.token);

      this.nextToken();

      Expr right = this.andExpr();

      if (left.getType() != Type.booleanType || right.getType() != Type.booleanType)
        throw new RuntimeException("Error: expression of boolean type expected");

      rightList.add(right);

      left = new CompositeExpr(left, opList, rightList);
    }

    return left;
  }

  private Expr andExpr() {
    Expr left = this.relExpr();

    List<Symbol> opList = new ArrayList<Symbol>();
    List<Expr> rightList = new ArrayList<Expr>();

    if (this.token == Symbol.AND) {
      opList.add(this.token);

      this.nextToken();

      Expr right = this.relExpr();

      if (left.getType() != Type.booleanType || right.getType() != Type.booleanType)
        throw new RuntimeException("Error: expression of boolean type expected");

      rightList.add(right);

      left = new CompositeExpr(left, opList, rightList);
    }

    return left;
  }

  private Expr relExpr() {
    Expr left = this.addExpr();

    List<Symbol> opList = new ArrayList<Symbol>();
    List<Expr> rightList = new ArrayList<Expr>();

    if (this.token == Symbol.GREATER || this.token == Symbol.GREATER_E || this.token == Symbol.LESS
        || this.token == Symbol.LESS_E || this.token == Symbol.EQUAL || this.token == Symbol.DIFF) {
      opList.add(this.token);

      this.nextToken();

      Expr right = this.addExpr();

      if (left.getType() != right.getType())
        throw new RuntimeException("Error: type error in expression");

      rightList.add(right);

      left = new CompositeExpr(left, opList, rightList);
    }

    return left;
  }

  private Expr addExpr() {
    Expr left = this.multExpr();

    List<Symbol> opList = new ArrayList<Symbol>();
    List<Expr> rightList = new ArrayList<Expr>();

    if (this.token == Symbol.PLUS || this.token == Symbol.MINUS) {
      while (this.token == Symbol.PLUS || this.token == Symbol.MINUS) {
        opList.add(this.token);

        this.nextToken();

        Expr right = this.multExpr();

        if (left.getType() != Type.integerType || right.getType() != Type.integerType)
          throw new RuntimeException("Error: expression of integer type expected");

        rightList.add(right);
      }

      left = new CompositeExpr(left, opList, rightList);
    }

    return left;
  }

  private Expr multExpr() {
    Expr left = this.simpleExpr();

    List<Symbol> opList = new ArrayList<Symbol>();
    List<Expr> rightList = new ArrayList<Expr>();

    if (this.token == Symbol.MULT || this.token == Symbol.DIV || this.token == Symbol.MOD) {
      while (this.token == Symbol.MULT || this.token == Symbol.DIV || this.token == Symbol.MOD) {
        opList.add(this.token);

        this.nextToken();

        Expr right = this.simpleExpr();

        if (left.getType() != Type.integerType || right.getType() != Type.integerType)
          throw new RuntimeException("Error: expression of integer type expected");

        rightList.add(right);
      }

      left = new CompositeExpr(left, opList, rightList);
    }

    return left;
  }

  private Expr simpleExpr() {
    Expr e = null;

    if (this.token == Symbol.NUMBER) {
      e = new Numero(numberValue);
      this.nextToken();
    } else if (this.token == Symbol.TRUE) {
      this.nextToken();
      return BooleanExpr.True;
    } else if (this.token == Symbol.FALSE) {
      this.nextToken();
      return BooleanExpr.False;
    } else if (this.token == Symbol.ID) {
      e = identObjects.get(variableName);
      this.nextToken();
    } else if (this.token == Symbol.MINUS || this.token == Symbol.PLUS) {
      Symbol op = this.token;
      this.nextToken();

      e = new UnaryExpr(simpleExpr(), op);

      if (e.getType() != Type.integerType)
        throw new RuntimeException("Error: expression of integer type expected.");
    } else if (this.token == Symbol.NOT) {
      Symbol op = this.token;
      this.nextToken();
      e = new UnaryExpr(simpleExpr(), op);

      if (e.getType() != Type.booleanType)
        throw new RuntimeException("Error: expression of boolean type expected.");
    } else if (this.token == Symbol.QUOTATION_M) {
      StringBuffer value = new StringBuffer();

      while (tokenPos < this.input.length && Character.isLetter(this.input[this.tokenPos])
          || Character.isDigit(this.input[tokenPos])) {
        value.append(this.input[tokenPos]);
        this.tokenPos++;
      }

      this.nextToken();

      if (this.token != Symbol.QUOTATION_M)
        throw new RuntimeException("Error: expected '\"'");

      this.nextToken();

      return new StringExpr(value.toString());
    } else if (this.token == Symbol.LEFT_P) {
      this.nextToken();
      e = expr();
      if (this.token != Symbol.RIGHT_P)
        throw new RuntimeException("Error: expected ')'.");
      this.nextToken();
    } else {
      throw new RuntimeException("Error: expected a simpleExpr.");
    }

    return e;
  }

}
package main;

import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

import ast.Program;

public class Main {

  public static void main(String[] args) {
    char[] expr = readFile(args[1]);

    Compiler compiler = new Compiler(expr);
    Program ast = compiler.compile();
    Map<String, Object> memory = new HashMap<>();

    if (args.length == 0)
      throw new RuntimeException("Error: input length is zero");
    switch (args[0]) {
    case "-gen" -> {
      ast.genC();
    }
    case "-run" -> {
      ast.eval(memory);
    }
    default -> {
      throw new RuntimeException("Error: cannot read input arguments");
    }
    }
  }

  private static char[] readFile(String filePath) {
    StringBuffer input = new StringBuffer();

    try {
      File programInput = new File(filePath);
      Scanner reader = new Scanner(programInput);
      while (reader.hasNextLine()) {
        String data = reader.nextLine();
        input.append(data);
      }
      reader.close();
    } catch (FileNotFoundException e) {
      System.out.println("Error: file not found.");
      e.printStackTrace();
    }

    return input.toString().toCharArray();
  }
}

package src.mua;

import java.util.Scanner;

public class Main {
    public static Scanner scan = new Scanner(System.in);
    public static NameSpace nameSpace = new NameSpace();

    public static void main(String[] args) {
        Interpreter begin = new Interpreter();
        begin.readCommand(scan, nameSpace);
    }
}

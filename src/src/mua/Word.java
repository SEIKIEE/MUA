package src.mua;

import java.util.HashMap;

public class Word {
    private static HashMap<String, Integer> reversedWord = new HashMap<>();

    public Word() {
        String[] operation = {
                "make", "thing", "erase", "isname", "print", "read",
                "add", "sub", "mul", "div", "mod",
                "eq", "gt", "lt",
                "and", "or", "not",
                "isnumber", "isword", "isbool", "isempty",
                "readlist", "islist",
                "repeat",
                "word", "sentence", "list", "join", "first", "last", "butfirst", "butlast",
                "random", "floor", "sqrt",
                "wait", "save", "load", "erall", "poall",
                "run"
        };
        for (int i = 0; i < operation.length; i++) {
            reversedWord.put(operation[i], i);
        }
        operation = null;
    }

    public static boolean isReversedWord(String s) {
        return reversedWord.containsKey(s);
    }

    public static boolean isWord(String s) {
        if (s.startsWith("\"") == true) {
            return true;
        } else return false;
    }

    public static boolean isNumber(String s) {
        String reg = "^-?[0-9]+(.[0-9]+)?$";
        return s.matches(reg);
    }

    public static boolean isBoolean(String s) {
        if (s.equals("true") || s.equals("false"))
            return true;
        else return false;
    }

    public static boolean isBindValue(String s) {
        if (s.startsWith(":"))
            return true;
        else return false;
    }

    public static boolean isList(String s) {
        if (s.startsWith("[")) {
            return true;
        } else return false;
    }


    public static boolean isExpression(String s) {
        if (s.startsWith("("))
            return true;
        else return false;
    }

    public static boolean isOp(String s) {
        String match = "[\\+\\-\\*\\/\\(\\)\\%]";
        return s.matches(match);
    }
}

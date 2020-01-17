package src.mua;

import javax.management.ValueExp;
import java.io.*;
import java.util.*;

public class Operator {
    //make <name> <value> : 将value绑定到name上，绑定后的名字位于命名空间。此⽂文档中的基本操
    //作的名字不不能重新命名
    public static void make(Value key, Value value, NameSpace nameSpace) throws Exception {
        nameSpace.bindValue(key.value, value);
    }

    //thing <name> :返回word所绑定的值
    public static Value thing(Value key, NameSpace nameSpace) throws Exception {
        if (key.type != 2)
            throw new Exception("thing的name不正确");
        else if (!nameSpace.isName(key.value)) {
            throw new Exception("名字不存在");
        } else return nameSpace.getValueOf(key.value);
    }

    //erase <name> :清除word所绑定的值
    public static void erase(String key, NameSpace nameSpace) throws Exception {
        if (!nameSpace.isName(key))
            throw new Exception("名字不存在");
        else nameSpace.unbindValue(key);
    }

    //isname <word> :返回word是否是⼀一个名字，true/false
    public static Value isname(Value key, NameSpace nameSpace) throws Exception {
        if (key.type != 2) {
            throw new Exception("isname的name不是word");
        }
        return new Value(3, String.valueOf(nameSpace.isName(key.value)));
    }

    //print <value>
    public static void print(Value key) throws Exception {
        System.out.printf(key.value);
//        if (key.type != 4) {
//            System.out.printf(key.value);
//            return;
//        }
//        Scanner scanner = new Scanner(key.value);
//        Value p = null;
//        int i = 0;
//        while ((p = readNext(scanner)) != null) {
//            if (p.type != 4) {
//                if (i == 0)
//                    System.out.print(p.value);
//                else
//                    System.out.print(" " + p.value);
//            } else {
//                if (i == 0)
//                    System.out.print("[");
//                else
//                    System.out.print(" [");
//                print(new Value(4, p.value));
//                System.out.print("]");
//            }
//            i++;
//        }
    }

    //read :返回⼀一个从标准输⼊入读取的数字或字
    public static Value read(Scanner scan) {
        String string = scan.next();
        if (Word.isBoolean(string))
            return new Value(3, string);
        else if (Word.isNumber(string))
            return new Value(1, string);
        else
            return new Value(2, string);
    }

    //readlist: 返回一个从标准输入读取的一行，构成一个表，行中每个以空格分隔部分是list的一个元素，元素的类型为字
    public static Value readlist(Scanner scan) {
        scan.nextLine();
        String result = scan.nextLine();
        return new Value(4, result.substring(1, result.length() - 1));
    }

    public static Value add(Value a, Value b, int type) throws Exception {
        if (a.type != b.type) {
            throw new Exception("运算类型不相同");
        }
        String result;
        if (a.type == 1 && b.type == 1) {
            double p = Double.valueOf(a.value);
            double q = Double.valueOf(b.value);
            switch (type) {
                case 1:
                    result = String.valueOf(p + q);
                    break;
                case 2:
                    result = String.valueOf(p - q);
                    break;
                case 3:
                    result = String.valueOf(p * q);
                    break;
                case 4:
                    result = String.valueOf(p / q);
                    break;
                case 5:
                    result = String.valueOf(p % q);
                    break;
                default:
                    throw new Exception("没有这个运算");
            }
            return new Value(1, result);
        } else throw new Exception("不可运算");
    }

    public static Value and(Value a, Value b) throws Exception {
        if ((!a.value.equals("true") && !a.value.equals("false")) || (!b.value.equals("true") && !b.value.equals("false")))
            throw new Exception("不可运算");
        if (a.value.equals(b.value))
            return a;
        else return new Value(3, "false");
    }

    public static Value or(Value a, Value b) throws Exception {
        if ((!a.value.equals("true") && !a.value.equals("false")) || (!b.value.equals("true") && !b.value.equals("false")))
            throw new Exception("不可运算");
        if (!a.value.equals(b.value))
            return new Value(3, "true");
        else if (a.equals("false"))
            return new Value(3, "false");
        else return new Value(3, "true");
    }

    public static Value not(Value a) throws Exception {
        if (a.value.equals("true"))
            return new Value(3, "false");
        else if (a.value.equals("false"))
            return new Value(3, "true");
        else
            throw new Exception("不可运算");
    }

    public static Value eq(Value a, Value b) throws Exception {
        String result;
        if (a.type != b.type) {
            return new Value(3, "false");
        }
        if (a.type == 1) {
            result = (Double.parseDouble(a.value) == Double.parseDouble(b.value)) ? "true" : "false";
            return new Value(3, result);
        } else if (a.type == 4) {
            Value p = null, q = null;
            Scanner scanner1 = new Scanner(a.value);
            Scanner scanner2 = new Scanner(b.value);
            while (true) {
                p = Interpreter.readNextParameter(scanner1, new NameSpace());
                q = Interpreter.readNextParameter(scanner2, new NameSpace());
                if (p != null && q != null) {
                    if (!p.value.equals(q.value))
                        return new Value(3, "false");
                } else if ((p != null && q == null) || (p == null && q != null)) {
                    return new Value(3, "false");
                } else {
                    return new Value(3, "true");
                }
            }
        } else {
            result = (a.value.equals(b.value) == true) ? "true" : "false";
            return new Value(3, result);
        }
    }

    public static Value gt(Value a, Value b) throws Exception {
        String result;
        if (a.type == 1 || b.type == 1) {
            result = (Double.parseDouble(a.value) > Double.parseDouble(b.value)) ? "true" : "false";
        } else {
            result = (a.value.compareTo(b.value) > 0) ? "true" : "false";

        }
        return new Value(3, result);
    }

    public static Value lt(Value a, Value b) throws Exception {
        String result;
        if (a.type == 1 || b.type == 1) {
            result = (Double.parseDouble(a.value) < Double.parseDouble(b.value)) ? "true" : "false";
        } else {
            result = (a.value.compareTo(b.value) < 0) ? "true" : "false";

        }
        return new Value(3, result);
    }

    //判断类型的函数：isnumber isword islist isbool isempty => 调用Word里写好的函数
    public static Value isnumber(Value temp) {
        return new Value(3, temp.type == 1 ? "true" : "false");
    }

    public static Value isword(Value temp) {
        return new Value(3, temp.type == 2 ? "true" : "false");

    }

    public static Value isbool(Value temp) {
        return new Value(3, temp.type == 3 ? "true" : "false");
    }

    public static Value islist(Value temp) {
        return new Value(3, temp.type == 4 ? "true" : "false");
    }

    public static Value isempty(Value temp) throws Exception {
        if (temp.type == 2 || temp.type == 4)
            return new Value(3, temp.value.equals("") ? "true" : "false");
        throw new Exception("类型不对");
    }

    //获得一个list 返回list的字面量
    public static String getList(String list, Scanner scan) {
        if (countChar(list, '[') == countChar(list, ']')) {
            if (list.split(" |\\[|\\]").length == 0) {
                return "";
            }
            return list.substring(1, list.length() - 1);
        }
        list += " " + scan.next();
        while (countChar(list, '[') != countChar(list, ']')) {
            list = list + " " + scan.next();
        }
        if (list.split(" |\\[|\\]").length == 0) {
            return "";
        }
        return list.substring(1, list.length() - 1);
    }

    private static int countChar(String s, char t) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == t) {
                count++;
            }
        }
        return count;
    }

    public static void output(NameSpace nameSpace, Value v) {
        nameSpace.bindValue("-output", v);
    }

    public static void stop(Scanner scan) {
        while (scan.hasNext())
            scan.nextLine();
    }

    public static void export(NameSpace nameSpace, Value v) {
        src.mua.Main.nameSpace.bindValue(v.value, nameSpace.getValueOf(v.value));
    }

    public static void doif(Value v1, Value v2, Value v3, NameSpace nameSpace) throws Exception {
        if (v1.type != 3) throw new Exception("if的条件不是bool");
        if (v1.value.equals("true")) {
            Scanner scan = new Scanner(v2.value);
            Interpreter.readCommand(scan, nameSpace);
        } else {
            Scanner scan = new Scanner(v3.value);
            Interpreter.readCommand(scan, nameSpace);
        }
    }

    public static Value function(String command, NameSpace nameSpace, Scanner scan) throws Exception {
        Value v1, v2;
        Value function = nameSpace.getValueOf(command);   //首先获得本地的函数
        if (function == null) function = Main.nameSpace.getValueOf(command);  //没有本地就获得全局的函数
        String functionCommand = function.value;
        Scanner newScanner = new Scanner(functionCommand);
        NameSpace newNameSpace = new NameSpace();   //局部变量
        v1 = Interpreter.readNextParameter(newScanner, nameSpace);  //参数表
        v2 = Interpreter.readNextParameter(newScanner, nameSpace);  //操作表
        if (v1 != null && isempty(v1).value.equals("false")) {
            //如果参数表不为null，就需要参数绑定
            String[] parameterList = v1.value.split(" |\t");
            for (int i = 0; i < parameterList.length; i++) {
                //参数绑定：参数的值来源于总体
                Value v = Interpreter.readNextParameter(scan, nameSpace);
                newNameSpace.bindValue(parameterList[i], v);
            }
        }
        if (v2 != null && isempty(v2).value.equals("false")) {
            //如果操作表不为null，就写到要读取的地方
            newScanner = new Scanner(v2.value);
            Interpreter.readCommand(newScanner, newNameSpace);
        }
        return newNameSpace.getValueOf("-output");
    }

    //获得一个不含有外面两个括号的表达式，如果表达式中间有空格，直接不要这个空格了。
    public static String getExpression(String expression, Scanner scan) {
        if (countChar(expression, '(') == countChar(expression, ')'))
            return expression.substring(1, expression.length() - 1);
        expression += " " + scan.next();
        while (countChar(expression, '(') != countChar(expression, ')')) {
            expression += scan.next();
        }
        return expression.substring(1, expression.length() - 1);
    }

    //分析字符串的结构，在应该插入空格的地方插入空格
    public static String adjustExpression(String expression, NameSpace nameSpace) throws Exception {
        //照理说表达式里面是只能有num的，所以有word就表示它是函数
        ArrayList<Value> operations = new ArrayList<>();
        String match = "[\\+\\-\\*\\/\\(\\)\\%\\ ]";
        String operands = "";
        for (int i = 0; i < expression.length(); ) {
            int j = i;
            for (; j < expression.length(); j++) {
                if (!expression.substring(j, j + 1).matches(match)) {
                    operands += expression.substring(j, j + 1);
                } else {
                    if (operands.trim().length() != 0) {
                        if (Word.isNumber(operands)) {
                            //数字
                            operations.add(new Value(1, operands));
                        } else {
//                            if (Word.isBindValue(operands)) {   //处理：
//                                Scanner scanner = new Scanner(operands);
//                                Value r = Interpreter.readNextParameter(scanner, nameSpace);
//                                operations.add(r);
//                            } else {  //处理函数
//
//                            }
                            operations.add(new Value(2, operands));
                        }
                    }
                    if (!expression.substring(j, j + 1).equals(" "))
                        operations.add(new Value(2, expression.substring(j, j + 1)));
                    operands = "";
                    //如果遇到了运算符什么的就退出
                    break;
                }
            }
            i = j + 1;
        }
        if (operands.trim().length() != 0) {
            if (operands.matches(match))
                operations.add(new Value(1, operands));
            else {
                if (Word.isNumber(operands)) {
                    //数字
                    operations.add(new Value(1, operands));
                } else {
                    //函数
                    //可以是：,也可以是函数
//                    Scanner scanner = new Scanner(operands);
//                    Value r = Interpreter.readNextParameter(scanner, nameSpace);
//                    operations.add(r);
                    operations.add(new Value(2, operands));
                }
            }
        }
        String result = "";
        //分割成一个后面空一个空格的形式
        for (Value v : operations) {
            if (result.equals(""))
                result += v.value;
            else
                result += " " + v.value;
        }
        //test
//        System.out.println(result);

        //开一个scanner
        String returnResult = "";
        Scanner scanner = new Scanner(result);
//        Print(scanner);

        while (true) {
            Value v = Interpreter.readNextParameter(scanner, nameSpace);
            if (v == null)
                break;
//            System.out.println(v.value);
            returnResult += v.value;
        }
//        System.out.println(returnResult.trim());
        return returnResult.trim();
    }

    private static void Print(Scanner scanner) {
        while (scanner.hasNext()) {
            System.out.println(scanner.next());
        }
    }

    public static Value dealExpression(String command, NameSpace nameSpace) throws Exception {
        class op {
            String value;
            int priority;

            op(String v, int p) {
                value = v;
                if (p == 0)
                    priority = p;
                else {
                    if (v.equals("+") || v.equals("-"))
                        priority = 1;
                    else if (v.equals("*") || v.equals("%") || v.equals("/"))
                        priority = 2;
                    else
                        priority = -1;
                }
            }
        }
        Stack<op> operators = new Stack<>();    //操作符
        Queue<op> expression = new LinkedList<>();   //后缀表达式
        StringBuffer s = new StringBuffer("");
        Queue<op> result = new LinkedList<op>();
        String match = "[\\+\\-\\*\\/\\(\\)\\%]";
        for (int i = 0; i < command.length(); i++) {
            if (!command.substring(i, i + 1).matches(match)) {
                s.append(command.substring(i, i + 1));
                continue;
            }
            if (s.length() != 0) {
                Scanner newScanner = new Scanner(String.valueOf(s));
                Value v = Interpreter.readNextParameter(newScanner, nameSpace);
                //直接输出操作数
                result.add(new op(v.value, 0));
            }
            result.add(new op(command.substring(i, i + 1), -1));
            //将操作符放入栈中
            s.delete(0, s.length());
        }
        for (op i : result) {
            System.out.println(i.value);
        }
        if (s.length() != 0) {
            if (s.toString().matches(match)) {
                result.add(new op(s.toString(), 0));
            } else {
                Scanner newScanner = new Scanner(String.valueOf(s));
                Value v = Interpreter.readNextParameter(newScanner, nameSpace);
                //直接输出操作数
                result.add(new op(v.value, 0));
            }
        }
        //now get a expression only contains numbers
        for (op a : result) {
            if (a.priority == 0)
                //如果是运算数，直接加在队列后面
                expression.add(a);
            else {
                //运算符
                if (a.value.equals(")")) {
                    //右括号弹出到遇到左括号
                    op i = operators.peek();
                    while (!operators.empty() && !i.value.equals("(")) {
                        expression.add(i);
                        operators.pop();
                        i = operators.peek();
                    }
                    operators.pop();
                } else {
                    if (!a.value.equals("(") && !operators.empty()) {
                        op i = operators.peek();
                        while (!operators.empty() && a.priority < i.priority) {
                            expression.add(i);
                            operators.pop();
                            i = operators.peek();
                        }
                    }
                    operators.push(a);
                }
            }
        }
        while (!operators.empty()) {
            expression.add(operators.pop());
        }
        for (op e : expression) {
            if (e.priority == 0)
                operators.add(e);
            else {
                op op2 = operators.pop();
                op op1 = operators.pop();
                switch (e.value) {
                    case "+":
                        operators.add(new op(add(new Value(1, op1.value), new Value(1, op2.value), 1).value, 0));
                        break;
                    case "-":
                        operators.add(new op(add(new Value(1, op1.value), new Value(1, op2.value), 2).value, 0));
                        break;
                    case "*":
                        operators.add(new op(add(new Value(1, op1.value), new Value(1, op2.value), 3).value, 0));
                        break;
                    case "/":
                        operators.add(new op(add(new Value(1, op1.value), new Value(1, op2.value), 4).value, 0));
                        break;
                    case "%":
                        operators.add(new op(add(new Value(1, op1.value), new Value(1, op2.value), 5).value, 0));
                        break;
                }
            }
        }
        return new Value(1, operators.pop().value);
    }

    //word <word> <word|number|bool>
    public static Value word(Value a, Value b) {
        return new Value(2, a.value + b.value);
    }

    public static Value sentence(Value a, Value b) {
        return new Value(4, a.value + " " + b.value);
    }

    public static Value list(Value a, Value b) {
        String p = null, q = null;
        if (a.type == 4)
            p = "[" + a.value + "]";
        else
            p = a.value;
        if (b.type == 4)
            q = "[" + b.value + "]";
        else
            q = b.value;
        return new Value(4, p + " " + q);
    }

    public static Value join(Value a, Value b) {
        String p = null;
        if (b.type == 4)
            p = "[" + b.value + "]";
        else p = b.value;
        if (!a.value.equals(""))
            return new Value(4, a.value + " " + p);
        else return new Value(4, p);
    }

    public static Value first(Value a, NameSpace nameSpace) throws Exception {
        if (a.type != 4)
            return new Value(2, a.value.substring(0, 1));
        else {
            return readNext(new Scanner(a.value));
        }
    }

    public static Value last(Value a, NameSpace nameSpace) throws Exception {
        if (a.type == 2)
            return new Value(2, a.value.substring(a.value.length() - 1));
        else {
            Scanner scanner = new Scanner(a.value);
            Value p, q = null;
            while ((p = readNext(scanner)) != null) {
                q = p;
            }
            return q;
        }
    }

    private static Value readNext(Scanner scan) {
        if (!scan.hasNext())
            return null;
        String parameter = scan.next();
        if (parameter.startsWith("[")) {
            return new Value(4, Operator.getList(parameter, scan));
        } else if (Word.isNumber(parameter))
            return new Value(1, parameter);
        else return new Value(3, parameter);
    }

    public static Value butfirst(Value a, NameSpace nameSpace) throws Exception {
        if (a.type == 2)
            return new Value(2, a.value.substring(1));
        else {
            Scanner scanner = new Scanner(a.value);
            Value result = new Value(4, "");
            Value p;
            p = readNext(scanner);
            while ((p = readNext(scanner)) != null) {
                if (p.type != 4) {
                    if (result.value.equals(""))
                        result.value += p.value;
                    else result.value += " " + p.value;
                } else {
                    if (result.value.equals(""))
                        result.value += "[" + p.value + "]";
                    else result.value += " [" + p.value + "]";
                }
            }
            return result;
        }
    }

    public static Value butlast(Value a, NameSpace nameSpace) throws Exception {
        if (a.type == 1)
            return new Value(2, a.value.substring(0, a.value.length() - 1));
        else {
            Scanner scanner = new Scanner(a.value);
            Value result = new Value(4, "");
            Value p, q = null;
            while ((p = readNext(scanner)) != null) {
                if (q != null) {
                    if (q.type != 4) {
                        if (result.value.equals(""))
                            result.value += q.value;
                        else result.value += " " + q.value;
                    } else {
                        if (result.value.equals(""))
                            result.value += "[" + q.value + "]";
                        else result.value += " [" + q.value + "]";
                    }
                }
                q = new Value(p);
            }
            return result;
        }
    }

    public static Value random(Value a) {
        return new Value(1, String.valueOf(new Random().nextInt(Integer.valueOf(a.value))));
    }

    public static Value Int(Value a) {
        return new Value(1, String.valueOf(Math.floor(Double.valueOf(a.value))));
    }

    public static Value Sqrt(Value a) {
        return new Value(1, String.valueOf(Math.sqrt(Double.valueOf(a.value))));
    }

    public static void erall(NameSpace nameSpace) {
        nameSpace.erall();
    }

    public static void poall(NameSpace nameSpace) {
        nameSpace.poall();

    }

    public static void save(String fileName, NameSpace nameSpace) {
        File file = new File(fileName);
        try {
            file.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            HashMap<String, Value> hashMap = nameSpace.getValueMap();
            String line = "";
            for (String name : hashMap.keySet()) {
                line += "make \"" + name + " ";
                if (hashMap.get(name).type == 2)
                    line += "\"" + hashMap.get(name).value + "\n";
                else if (hashMap.get(name).type == 4)
                    line += "[" + hashMap.get(name).value + "]\n";
                else
                    line += hashMap.get(name).value + "\n";
                out.write(line);
                line = "";
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load(String fileName, NameSpace nameSpace) {
        File file = new File(fileName);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            String content = new String(fileContent);
            Scanner scanner = new Scanner(content);
            Interpreter.readCommand(scanner, nameSpace);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

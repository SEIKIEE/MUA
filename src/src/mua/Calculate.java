package src.mua;

import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class Calculate {
    private static HashMap<String, Integer> opPrior = new HashMap<>();

    //主函数，接受一个表达式，返回表达式的运算结果
    public static String calculate(String expression) throws Exception {
        //预格式化，解决--，++的问题
        expression = preFormate(expression);
        Stack<String> postfix = new Stack<>();
        Stack<String> result = new Stack<>();
        //优先级压栈
        opPrior.put("=", 0);
        opPrior.put("(", 1);
        opPrior.put(")", 1);
        opPrior.put("+", 2);
        opPrior.put("-", 2);
        opPrior.put("*", 3);
        opPrior.put("/", 3);
        opPrior.put("%", 3);

        //中缀表达式变成后缀表达式
        postfix = infix2Postfix(expression);

        Collections.reverse(postfix);//后缀表达式倒置进行计算
        String p1, p2, op;
        try {
            while (!postfix.isEmpty()) {
                op = postfix.pop();
                if (Word.isNumber(op))
                    result.push(op);//若是数字则压栈
                else {
                    p2 = result.pop();//若不是数字则弹出两个进行计算，将结果压回栈中
                    p1 = result.pop();
                    String tempResult = compute(p1, p2, op); //运算
                    result.push(tempResult);
                }
            }
            if (result.size() != 1)//如果最终栈里不只有一个值，那么说明表达式有错
                throw new Exception("有错误");
        } catch (Exception ex) {
            throw ex;
        }
        return result.pop();
    }

    public static String compute(String value1, String value2, String operator) throws Exception {
        switch (operator) {
            case "+":
                return Operator.add(new Value(1, value1), new Value(1, value2), 1).value;
            case "-":
                return Operator.add(new Value(1, value1), new Value(1, value2), 2).value;
            case "*":
                return Operator.add(new Value(1, value1), new Value(1, value2), 3).value;
            case "/":
                return Operator.add(new Value(1, value1), new Value(1, value2), 4).value;
            case "%":
                return Operator.add(new Value(1, value1), new Value(1, value2), 5).value;
            default:
                throw new Exception("没有这个运算");
        }
    }

    //将表达式转换为后缀表达式栈
    public static Stack<String> infix2Postfix(String expression) throws Exception {
        char[] allChars = expression.toCharArray();

        Stack<String> operators = new Stack<>();
        operators.push("=");    //栈底元素
        Stack<String> result = new Stack<>();
        int currentPos = 0;
        boolean flag = false;   //负数

        while (currentPos < allChars.length) {
            //如果是运算符
            if (Word.isOp(String.valueOf(allChars[currentPos]))) {
                char tempChar = allChars[currentPos];

                //如果碰到右边的括号则进行弹出操作
                if (tempChar == ')') {
                    while (!operators.peek().equals("(") && !operators.equals("="))
                        result.push(operators.pop());
                    if (operators.peek().equals("("))
                        operators.pop();
                    else
                        throw new Exception("The ( and ) don't match");//若发现没有左括号则抛出异常
                } else if (tempChar == '(')//遇见左括号则直接压入
                    operators.push(String.valueOf(tempChar));
                else {
                    //处理作为正负号的+和-
                    if (tempChar == '-' || tempChar == '+') {
                        if (currentPos == 0 || allChars[currentPos - 1] == '(') {
                            if (tempChar == '-')
                                flag = !flag;
                            currentPos++;
                            continue;
                        }
                    }
                    //能压则压，能弹出的弹出
                    while (!isPrior(String.valueOf(tempChar), operators.peek()))
                        result.push(operators.pop());
                    operators.push(String.valueOf(tempChar));
                }
                currentPos++;
            } else {
                int tempCurrentPos = currentPos;
                String tempNum = "";
                while (tempCurrentPos < allChars.length && !Word.isOp(String.valueOf(allChars[tempCurrentPos])))
                    tempNum += allChars[tempCurrentPos++];

                if (flag) {
                    tempNum = "-" + tempNum;
                    flag = false;
                }
                if (Word.isNumber(tempNum))
                    result.push(tempNum);
                else
                    throw new Exception(tempNum + " is Invalid");
                currentPos = tempCurrentPos;
            }
        }

        while (!operators.peek().equals("="))
            result.push(String.valueOf(operators.pop()));

        return result;
    }

    //优先级判断
    public static boolean isPrior(String charOp, String charPeek) {
        return opPrior.get(charOp) > opPrior.get(charPeek);
    }

    //处理++ -+ +- -- -(
    public static String preFormate(String expression) {
        int pp = expression.indexOf("++");
        int ps = expression.indexOf("+-");
        int sp = expression.indexOf("-+");
        int ss = expression.indexOf("--");
        while (pp != -1 || ps != -1 || sp != -1 || ss != -1) {
            if (pp != -1)
                expression = expression.substring(0, pp) + "+" + expression.substring(pp + 2);
            else if (ps != -1)
                expression = expression.substring(0, ps) + "-" + expression.substring(ps + 2);
            else if (sp != -1)
                expression = expression.substring(0, sp) + "-" + expression.substring(sp + 2);
            else
                expression = expression.substring(0, ss) + "+" + expression.substring(ss + 2);
            pp = expression.indexOf("++");
            ps = expression.indexOf("+-");
            sp = expression.indexOf("-+");
            ss = expression.indexOf("--");
        }

        int n = expression.indexOf("-(");
        while (n != -1) {
            expression = expression.substring(0, n) + "-1*(" + expression.substring(n + 2);
            n = expression.indexOf("-(");
        }
        return expression;
    }
}
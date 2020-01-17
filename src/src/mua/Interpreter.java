package src.mua;

import java.util.Scanner;

public class Interpreter {
    public static void readCommand(Scanner scan, NameSpace nameSpace) {
        while (scan.hasNext()) {
            String command = scan.next();
            try {
                Value v1, v2, v3;
                switch (command) {
                    case "make":
                        //make <word> <value>
                        v1 = readNextParameter(scan, nameSpace);
                        v2 = readNextParameter(scan, nameSpace);
                        Operator.make(v1, v2, nameSpace);
                        break;
                    case "erase":
                        //erase <word>
                        v1 = readNextParameter(scan, nameSpace);
                        Operator.erase(v1.value, nameSpace);
                        break;
                    case "print":
                        //print <value>
                        v1 = readNextParameter(scan, nameSpace);
                        Operator.print(v1);
                        System.out.print("\n");
                        break;
                    case "repeat":
                        v1 = readNextParameter(scan, nameSpace);
                        v2 = readNextParameter(scan, nameSpace);
                        for (int i = 0; i < Integer.valueOf(v1.value); i++) {
                            Scanner newScan = new Scanner(v2.value);
                            readCommand(newScan, nameSpace);
                        }
                        break;
                    case "output":
                        //output <value>
                        v1 = readNextParameter(scan, nameSpace);
                        Operator.output(nameSpace, v1);
                        break;
                    case "stop":
                        Operator.stop(scan);
                        break;
                    case "export":
                        v1 = readNextParameter(scan, nameSpace);
                        Operator.export(nameSpace, v1);
                        break;
                    case "if":
                        //if <bool> <list1> <list2> :如果bool为真，则执行list1，否则执行list2。list均可 以为空表
                        v1 = readNextParameter(scan, nameSpace);    //bool
                        v2 = readNextParameter(scan, nameSpace);    //list1
                        v3 = readNextParameter(scan, nameSpace);    //list2
                        Operator.doif(v1, v2, v3, nameSpace);
                        break;
                    case "wait":
                        v1 = readNextParameter(scan, nameSpace);
                        Thread.sleep(Integer.valueOf(v1.value));
                        break;
                    case "save":
                        v1 = readNextParameter(scan, nameSpace);
                        Operator.save(v1.value, nameSpace);
                        break;
                    case "erall":
                        Operator.erall(nameSpace);
                        break;
                    case "poall":
                        Operator.poall(nameSpace);
                        break;
                    case "run":
                        v1 = readNextParameter(scan, nameSpace);
                        Scanner newScanner = new Scanner(v1.value);
                        Interpreter.readCommand(newScanner, nameSpace);
                        break;
                    case "load":
                        v1 = readNextParameter(scan, nameSpace);
                        Operator.load(v1.value, nameSpace);
                        break;
                    default:
                        //调用函数
                        Operator.function(command, nameSpace, scan);
                        break;
                }
            } catch (Exception e) {
//                e.printStackTrace();
//                return;
            }
        }
    }

    public static Value readNextParameter(Scanner scan, NameSpace nameSpace) throws Exception {
        src.mua.Word Word = new src.mua.Word();
        if (!scan.hasNext())
            return null;
        String parameter = scan.next();
        //judge parameter
        if (Word.isNumber(parameter))
            //数字
            return new Value(1, parameter);
        else if (Word.isWord(parameter))
            //word
            return new Value(2, parameter.substring(1));
        else if (Word.isBoolean(parameter))
            //布尔值
            return new Value(3, parameter);
        else if (Word.isList(parameter))
            //list
            return new Value(4, Operator.getList(parameter, scan));
        else if (Word.isBindValue(parameter)) {
            //:绑定
            if (nameSpace.isName(parameter.substring(1)))
                return nameSpace.getValueOf(parameter.substring(1));
            else throw new Exception("无这个变量");
        } else if (Word.isExpression(parameter)) {
            //处理加减乘除
            String exp = Operator.getExpression(parameter, scan);
            exp = Operator.adjustExpression(exp, nameSpace);
            //exp是中间不能有空格的表达式
            String result = Calculate.calculate(exp);
            return new Value(1, result);
        } else if (Word.isOp(parameter)) {
            return new Value(2, parameter);
        } else if (Word.isReversedWord(parameter)) {
            //命令
            Value v1, v2;
            switch (parameter) {
                case "thing":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.thing(v1, nameSpace);
                case "read":
                    return Operator.read(scan);
                case "isname":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.isname(v1, nameSpace);
                case "add":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.add(v1, v2, 1);
                case "sub":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.add(v1, v2, 2);
                case "mul":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.add(v1, v2, 3);
                case "div":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.add(v1, v2, 4);
                case "mod":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.add(v1, v2, 5);
                case "eq":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.eq(v1, v2);
                case "gt":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.gt(v1, v2);
                case "lt":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.lt(v1, v2);
                case "and":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.and(v1, v2);
                case "or":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.or(v1, v2);
                case "not":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.not(v1);
                case "readlist":
                    return Operator.readlist(scan);
                case "isnumber":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.isnumber(v1);
                case "isword":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.isword(v1);
                case "isbool":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.isbool(v1);
                case "islist":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.islist(v1);
                case "isempty":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.isempty(v1);
                case "word":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.word(v1, v2);
                case "sentence":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.sentence(v1, v2);
                case "list":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.list(v1, v2);
                case "join":
                    v1 = readNextParameter(scan, nameSpace);
                    v2 = readNextParameter(scan, nameSpace);
                    return Operator.join(v1, v2);
                case "first":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.first(v1, nameSpace);
                case "last":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.last(v1, nameSpace);
                case "butfirst":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.butfirst(v1, nameSpace);
                case "butlast":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.butlast(v1, nameSpace);
                case "random":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.random(v1);
                case "floor":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.Int(v1);
                case "sqrt":
                    v1 = readNextParameter(scan, nameSpace);
                    return Operator.Sqrt(v1);
                default:
                    throw new Exception("这是啥");
            }
        } else {
            return Operator.function(parameter, nameSpace, scan);
        }
    }

}



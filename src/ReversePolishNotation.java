import java.util.*;

class ReversePolishNotation {
    String[] splitString;
    private final Stack<Operator> operatorStack = new Stack<>();
    private final Queue<String> resultQueue = new LinkedList<>();

    ReversePolishNotation(String expression) throws Exception {
        preprocessing(expression);
        getReversePolish();
    }

    private void preprocessing(String expression) {
        //用于分割字符串的分隔符，加减乘除号及括号等符号都要视作分隔符
        String SEPARATOR = Symbol.OPERATOR + "|" + "[()]";
        //"#"为了防止最后有运算符在栈中未弹出
        String infixNotation = expression + "#";
        //将"-"前全部加上"0"，统一负号与减号
        infixNotation = infixNotation.replace("(-", "(0-");
        if (expression.charAt(0) == '-') {
            infixNotation = "0" + infixNotation;
        }
        splitString = infixNotation.split("(?<=" + SEPARATOR + ")|(?=" + SEPARATOR + ")");
    }

    private void getReversePolish() throws Exception {
        //level在后面用于控制^的结合性
        int level = 4;
        for (String element : splitString) {
            //将读到的字符串封装为类，并获取其类型
            Symbol symbol = new Symbol(element);
            String type = symbol.getType();
            //进行逆波兰转换
            switch (type) {
                //若为数，直接输出
                case "number":
                    resultQueue.offer(symbol.getContent());
                    break;
                //若为左括号，直接入栈，遇到右括号之前绝不出栈
                case "leftBracket":
                    Operator operator = new Operator(symbol.getContent());
                    operatorStack.push(operator);
                    break;
                //遇到右括号时，弹出到左括号（左括号也要弹出）
                case "rightBracket":
                    while (!operatorStack.peek().getName().equals("(")) {
                        resultQueue.offer(operatorStack.pop().getName());
                    }
                    operatorStack.pop();
                    break;
                //当遇到运算符时，要分一些情况讨论
                case "operator":
                    operator = new Operator(symbol.getContent());
                    //这里用于控制"^"的结合性，靠右的"^"优先级更高
                    if (operator.getName().equals("^")) {
                        operator.setPriority(level);
                        level++;
                    }
                    if (!operatorStack.empty()) {
                        //算符栈中要保证从下到上优先级递减（且不可有相等）
                        if (operatorStack.peek().getPriority() >= operator.getPriority()) {
                            //这里使用了短路与，只通过左边即可得出结论时不再判断右边，避免了空栈异常
                            while (!operatorStack.empty() && operatorStack.peek().getPriority() >= operator.getPriority()) {
                                resultQueue.offer(operatorStack.pop().getName());
                            }
                        }
                    }
                    //栈为空时读到的运算符直接入栈，比较优先级时最后也要入栈
                    operatorStack.push(operator);
                    break;
                default:
                    throw new Exception();
            }
        }
        System.out.println(resultQueue);
    }

    public double getResult() throws Exception {
        Stack<Double> numberStack = new Stack<>();
        while (!resultQueue.isEmpty()) {
            Symbol element = new Symbol(resultQueue.poll());
            switch (element.getType()) {
                case "number":
                    switch (element.getContent()) {
                        case "π":
                            numberStack.push(Math.PI);
                            break;
                        case "e":
                            numberStack.push(Math.E);
                            break;
                        default:
                            numberStack.push(Double.valueOf(element.getContent()));
                            break;
                    }
                    break;
                case "operator":
                    double firstNum = numberStack.pop();
                    switch (element.getContent()) {
                        case "+":
                            numberStack.push(firstNum + numberStack.pop());
                            break;
                        case "-":
                            numberStack.push(-firstNum + numberStack.pop());
                            break;
                        case "*":
                            numberStack.push(firstNum * numberStack.pop());
                            break;
                        case "/":
                            numberStack.push(numberStack.pop() / firstNum);
                            break;
                        case "%":
                            numberStack.push(numberStack.pop() % firstNum);
                            break;
                        case "^":
                            numberStack.push(Math.pow(numberStack.pop(), firstNum));
                            break;
                        case "abs":
                            numberStack.push(Math.abs(firstNum));
                            break;
                        case "sin":
                            numberStack.push(Math.sin(firstNum));
                            break;
                        case "cos":
                            numberStack.push(Math.cos(firstNum));
                            break;
                        case "ln":
                            numberStack.push(Math.log(firstNum));
                            break;
                        case "!":
                            numberStack.push(calculateFactorial(firstNum));
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    throw new Exception();
            }
        }
        //最后仍在栈中的即为结果
        return numberStack.peek();
    }

    private double calculateFactorial(double number) {
        double result = 1;
        for (int i = 1; i <= number; i++) {
            result *= i;
        }
        return result;
    }
}

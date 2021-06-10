class Operator {
    private int priority = 0;
    private final String name;

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    Operator(String operator) throws Exception {
        this.name = operator;
        setPriority();
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    private void setPriority() throws Exception {
        switch (this.name) {
            //左括号遇到右括号前要一直留在栈里，故优先级设为最低
            case "(":
            case ")":
                this.priority = 0;
                break;
            //'＃'用于标记字符串结尾，防止最后有运算符在栈内未弹出
            case "#":
                this.priority = 1;
                break;
            case "+":
            case "-":
                this.priority = 2;
                break;
            case "*":
            case "/":
            case "%":
                this.priority = 3;
                break;
            case "^":
                this.priority = 4;
                break;
            //函数可以被看作单目运算符
            case "abs":
            case "sin":
            case "ln":
            case "cos":
            case "!":
                this.priority = 2147483646;
                break;
            default:
                throw new Exception();
        }
    }
}

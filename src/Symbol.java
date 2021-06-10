class Symbol {
    private String content;
    private String type;
    public static final String FUNCTION = "^sin$|^cos$|^abs$|^ln$";
    public static final String NUMBER = "\\d\\d*\\.?\\d*|[e]|[π]";
    public static final String OPERATOR = "[+\\-*/%^!#]";

    Symbol(String content) throws Exception {
        this.content = content;
        setType();
    }

    public void setContent(String content) throws Exception {
        this.content = content;
        setType();
    }

    public String getContent() {
        return this.content;
    }

    private void setType() throws Exception {
        if (content.matches(NUMBER)) {
            this.type = "number";
            return;
        }
        if (content.matches(OPERATOR)) {
            this.type = "operator";
            return;
        }
        if (content.matches(FUNCTION)) {
            this.type = "operator";
            return;
        }
        String LEFT = "\\(";
        if (content.matches(LEFT)) {
            this.type = "leftBracket";
            return;
        }
        String RIGHT = "\\)";
        if (content.matches(RIGHT)) {
            this.type = "rightBracket";
            return;
        }
        //均不匹配则应当抛出异常
        throw new Exception();
    }

    public String getType() {
        return this.type;
    }
}

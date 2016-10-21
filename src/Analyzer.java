import java.io.IOException;

/**
 * Created by Mr.Zero on 2016/10/20.
 */
public class Analyzer {

    private IOHelper ioHelper;

    private final static String[] keywords = {
            "int", "unsigned", "short", "char", "long", "float", "double", "if", "else", "do", "while", "continue",
            "for", "switch", "case", "default", "break", "struct", "typedef", "const", "static", "return", "void"
    };

    /**
     * 目前所在的状态机编号
     */
    private int state = 0;

    char [] buff = new char[101];
    int buff_index = 0;

    /**
     * 存储多读的那个字符，用于下一个token的第一个字符
     */
    private char lastChar = ' ';
    public Analyzer() {
        ioHelper = new IOHelper();
    }

    /**
     * 核心方法
     */
    public void process() throws IOException {
        while(true) {
            int c = readChar();
            if (c == -1){           //-1 means EOF
                break;
            }
            else {
                char ch = (char)c;
                switch (state) {
                    case -1: {      //这个状态表示目前的token已经确定是个错误的token了，比如123abc，该状态等待出现空白符或操作符跳出该错误状态回到0状态
                        if (isLetter(ch) || isDigit(ch)){
                            state = -1;
                            buff[buff_index++] = ch;
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }
                    break;

                    case 0: {           //start state
                        if (!isWhiteSpace(ch)) {
                            if (isLetter(ch)){
                                state = 23;
                                buff[buff_index++] = ch;
                            }
                            else if (ch>='0'&&ch<='9') {
                                state = 24;
                                buff[buff_index++] = ch;
                            }
                            else if (ch == '!') {
                                state = 33;
                                buff[buff_index++] = ch;
                            }
                            else if (ch == '+') {
                                state = 25;
                                buff[buff_index++] = ch;
                            }
                            else if (ch == '-') {
                                state = 26;
                                buff[buff_index++] =ch;
                            }
                            else if (ch == '*') {
                                state = 27;
                                buff[buff_index++] = ch;
                            }
                            else if (ch == '/') {
                                state = 28;
                                buff[buff_index++] = ch;
                            }
                            else if (ch == '&') {
                                state = 29;
                                buff[buff_index++] = ch;
                            }
                            else if (ch == '|') {
                                state = 31;
                                buff[buff_index++] = ch;
                            }
                            else if (ch == '=') {
                                state = 34;
                                buff[buff_index++] = ch;
                            }
                            else if (ch == '>') {
                                state = 49;
                                buff[buff_index++] = ch;
                            }
                            else if (ch == '<') {
                                state = 50;
                                buff[buff_index++] = ch;
                            }
                            else if (isOtherOperator(ch)){
                                state = 42;     //42代表分号，但是在此处代表了所有其他的operator，因为输出结果都是枚举类的OPERATOR，所以无所谓
                                buff[buff_index++] = ch;
                                outputToken();
                            }
                            else if (ch == '\"') {
                                state = 55;
                            }
                            else if (ch == '\'') {
                                state = 56;
                            }
                            else {
                                state = -1;
                                buff[buff_index++] = ch;
                                outputToken();
                            }
                        }
                        else {
                            //ch is blank character, do nothing
                        }
                    }
                    break;

                    case 23: {          //id
                        if (isLetter(ch)){
                            state = 23;
                            buff[buff_index++] = ch;
                        }
                        else if (isDigit(ch)) {
                            state = 23;
                            buff[buff_index++] = ch;
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }
                    break;

                    case 24: {          //digit
                        if (isDigit(ch)) {
                            state = 24;
                            buff[buff_index++] = ch;
                        }
                        else if (ch == '.') {
                            state = 100;        //小数点以后的状态
                            buff[buff_index++] = ch;
                        }
                        else if (isLetter(ch)){
                            state = -1;
                            buff[buff_index++] = ch;
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }
                    break;

                    case 100: {         //last char is .
                        if (isDigit(ch)) {
                            state = 101;        //小数点以后已经有数字了
                            buff[buff_index++] = ch;
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }
                    break;

                    case 25: {          // +
                        if (ch == '+') {
                            state = 40;
                            buff[buff_index++] = ch;
                            outputToken();
                        }
                        else if (ch == '=') {
                            state = 36;
                            buff[buff_index++] = ch;
                            outputToken();
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }
                    break;

                    case 26: {          // -
                        if (ch == '-') {
                            state = 41;
                            buff[buff_index++] = ch;
                            outputToken();
                        }
                        else if (ch == '=') {
                            state = 37;
                            buff[buff_index++] = ch;
                            outputToken();
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }
                    break;

                    case 27: {          // *
                        if (ch == '=') {
                            state = 38;
                            buff[buff_index++] = ch;
                            outputToken();
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }
                    break;

                    case 28: {          // /
                        if (ch == '=') {
                            state = 39;
                            buff[buff_index++] = ch;
                            outputToken();
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }

                    case 29: {          // &
                        if (ch == '&'){
                            state = 30;
                            buff[buff_index++] = ch;
                            outputToken();
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }
                    break;

                    case 31: {          // |
                        if (ch == '|'){
                            state = 32;
                            buff[buff_index++] = ch;
                            outputToken();
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }
                    break;

                    case 33: {          // !
                        if (ch == '='){
                            state = 54;
                            buff[buff_index++] = ch;
                            outputToken();
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }
                    break;

                    case 34: {          // =
                        if (ch == '='){
                            state = 35;
                            buff[buff_index++] = ch;
                            outputToken();
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }
                    break;

                    case 49: {          // >
                        if (ch == '='){
                            state = 51;
                            buff[buff_index++] = ch;
                            outputToken();
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }
                    break;

                    case 50: {          // <
                        if (ch == '='){
                            state = 52;
                            buff[buff_index++] = ch;
                            outputToken();
                        }
                        else {
                            lastChar = ch;
                            outputToken();
                        }
                    }
                    break;

                    case 55: {      // "
                        if (ch != '\"') {
                            state = 55;
                            if ((ch!='\r') && (ch!='\n')) {
                                buff[buff_index++] = ch;
                            }
                            else {
                                state = -1;
                                outputToken();
                            }
                        }
                        else {
                            outputToken();
                        }
                    }
                    break;

                    case 56: {      // '
                        if (ch != '\'') {
                            state = 56;
                            if ((ch!='\r') && (ch!='\n')){
                                buff[buff_index++] = ch;
                            }
                            else {
                                state = -1;
                                outputToken();
                            }
                        }
                        else {
                            outputToken();
                        }
                    }
                    break;

                }
            }
        }
        ioHelper.closeIO();
    }

    private int readChar() throws IOException {
        if (lastChar != ' ') {
            int c = (int)lastChar;
            lastChar = ' ';
            return c;
        }
        else {
            return ioHelper.read();
        }
    }

    private void outputToken() {
        buff[buff_index] = '\0';
        String value = new String(buff, 0, buff_index);
        buff_index = 0;
        CodeEnum code;
        if (state == 23) {
            code = CodeEnum.ID;
            if (isKeyword(value)){
                code = CodeEnum.KEYWORD;
            }
        }
        else if (state >=1 && state <=22) {
            code = CodeEnum.KEYWORD;
        }
        else if (state == 24 || state == 100) {
            code = CodeEnum.DIGIT;
        }
        else if ((state >=25 && state <=54) || state == 57) {
            code = CodeEnum.OPERATOR;
        }
        else if (state == 55) {
            code = CodeEnum.STRING;
        }
        else if (state == 56) {
            code = CodeEnum.CHARACTER;
        }
        else {
            code = CodeEnum.ERROR;
        }
        Token token = new Token(code, value);
        ioHelper.write(token);
        state = 0;
    }

    private boolean isOtherOperator(char c) {
        if ((c==';') || (c=='(') || (c==')') || (c=='[') || (c==']') || (c=='{') || (c=='}') || (c=='%') || (c==':')){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isKeyword(String s) {
        for (String keyword : keywords) {
            if (s.equals(keyword)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLetter(char c) {
        return ((c>='a' && c<='z') || (c>='A' && c<='Z') || (c == '_'));
    }

    private boolean isDigit(char c) {
        return ((c >= '0') && (c <= '9'));
    }

    private boolean isWhiteSpace(char c) {
        return (c == ' ') || (c == '\t') || (c=='\r') ||(c == '\n');
    }

    public static void main(String [] args) {
        Analyzer analyzer = new Analyzer();
        try {
            analyzer.process();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("===============IO Error!!!===================");
        }
    }
}

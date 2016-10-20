/**
 * Created by Mr.Zero on 2016/10/20.
 */
public class Token {
    /**
     * 种别码
     */
    private CodeEnum code;

    private String value;

    public Token(CodeEnum code, String value) {
        this.code = code;
        this.value = value;
    }

    public CodeEnum getCode() {
        return code;
    }

    public void setCode(CodeEnum code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString () {
        return "< "+code+", "+value+" >\n";
    }
}

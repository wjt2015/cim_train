package wjt.model;

public class ApiResult {

    public int code;
    public String msg;
    public Object retObj;

    public ApiResult(int code, String msg, Object retObj) {
        this.code = code;
        this.msg = msg;
        this.retObj = retObj;
    }
}

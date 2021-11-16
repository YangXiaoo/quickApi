package quickcore.common.tools;

import quickcore.common.constants.CONSTANT_DEFINE;
import quickcore.common.constants.MODEL_CODE;

public class WSModel {
    private String token = "";
    private String requestMethod;
    private Object req;
    private Object rsp;
    private String code;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Object getReq() {
        return req;
    }

    public void setReq(Object req) {
        this.req = req;
    }

    public Object getRsp() {
        return rsp;
    }

    public void setRsp(Object rsp) {
        this.rsp = rsp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void success(Object rsp) {
        this.code = MODEL_CODE.SUCCESS;
        this.rsp = rsp;
    }

    public void error(Object rsp) {
        this.code = MODEL_CODE.ERROR;
        this.rsp = rsp;
    }
}

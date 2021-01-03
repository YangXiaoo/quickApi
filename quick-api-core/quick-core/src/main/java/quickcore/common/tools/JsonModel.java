package quickcore.common.tools;

import quickcore.common.constants.JSON_MODEL_CODE;

/**
 * 定义返回格式
 * @author yangxiao
 */
public class JsonModel {
    private String code = "";
    private String message = "";
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 设置成功消息和数据
     * @param message 成功消息
     * @param data 数据
     * @return void
     * @author yangxiao
     * @date 2020/11/29 14:12
     */
    public void success(String message, Object data) {
        this.code = JSON_MODEL_CODE.SUCCESS;
        this.message = message;
        this.data = data;
    }

    /**
     * 设置失败信息
     * <p>
     *     失败后不发送数据
     * </p>
     * @param message 失败信息
     * @return void
     * @author yangxiao
     * @date 2020/11/29 14:24
     */
    public void error(String message) {
        this.code = JSON_MODEL_CODE.ERROR;
        this.message = message;
    }

    /**
     * 设置警告消息和数据
     * @param message 警告消息
     * @param data 数据
     * @return void
     * @author yangxiao
     * @date 2020/11/29 14:12
     */
    public void warning(String message, Object data) {
        this.code = JSON_MODEL_CODE.WARNING;
        this.message = message;
        this.data = data;
    }
}

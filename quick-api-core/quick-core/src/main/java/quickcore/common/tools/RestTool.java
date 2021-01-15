package quickcore.common.tools;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * restful请求
 * @author yangxiao
 * @date 2021/1/3 21:05
 */
public class RestTool {
    /**
     * 发送POST请求
     * @param url 请求地址
     * @param params 参数
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/1/3 21:07
     */
    public static JsonModel sendPostRequest(String url, Map<String, Object> params){
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);
        RestTemplate client = new RestTemplate();
        return client.postForObject(url, request, JsonModel.class);

    }
}

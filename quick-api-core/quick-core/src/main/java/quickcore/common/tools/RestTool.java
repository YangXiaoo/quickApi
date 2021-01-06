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
        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON_UTF8);//设置参数类型和编码
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);
        RestTemplate client = new RestTemplate();
        return client.postForObject(url, request, JsonModel.class);
        //MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(type);
        //HttpMethod method = HttpMethod.POST;
        //// 将请求头部和参数合成一个请求
        //HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(params, headers);
        ////HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        //// 执行HTTP请求，将返回的结构使用JsonModel类格式化
        //ResponseEntity<JsonModel> response = client.exchange(url, method, requestEntity, JsonModel.class);
        //
        //return response.getBody();
    }
}

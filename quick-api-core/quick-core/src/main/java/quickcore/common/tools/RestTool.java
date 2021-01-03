package quickcore.common.tools;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
    public static JsonModel sendPostRequest(String url, MultiValueMap<String, Object> params){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_JSON);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
        ResponseEntity<JsonModel> response = client.exchange(url, method, requestEntity, JsonModel.class);

        return response.getBody();
    }
}

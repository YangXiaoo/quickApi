package quickcore.socket;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import quickcore.common.tools.JsonModel;
import quickcore.common.tools.WSModel;
import quickcore.common.utils.RequestUtil;
import quickcore.common.utils.StringUtils;
import quickcore.core.logic.QuickApiLogic;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;

@ServerEndpoint(value = "/ws")
@Component
@RestController
public class   WebSocketService {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    private static WebApplicationContext applicationContext;
    private static QuickApiLogic quickApiLogic;

    @Autowired
    public void setQuickApiLogic (QuickApiLogic quickApiLogic){
        WebSocketService.quickApiLogic = quickApiLogic;
    }
    @Autowired
    public void setApplicationContext(WebApplicationContext webApplicationContext) {
        WebSocketService.applicationContext = webApplicationContext;
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("[QuickApi] id: " + session.getId() + ", 连接成功");
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("[QuickApi] id: " + session.getId() + ", 连接关闭");
    }

    /**
     * 处理数据
     * <p>
     *     前端传入的信息为json转字符串，格式为:
     *     {
     *         requestMethod: '',   // 请求的方法，字符串
     *         requestData: {}      // 传入的数据，Object
     *     }
     * </p>
     * @param session Session
     * @param data 传入的数据
     * @return void
     * @author yangxiao
     * @date 2021/8/4 22:11
     */
    @OnMessage
    public void onMsg(Session session, String data) throws IOException {
        logger.info("[QuickApi] id: " + session.getId() + ", send data: " + data);
        Object ret = null;
        Map<String, Object> map =  JSONObject.parseObject(data, new TypeReference<Map<String, Object>>(){});
        String requestMethod = (String) map.get("requestMethod");
        String token = (String) map.get("token");

        WSModel wsModel = new WSModel();
        wsModel.setToken(token);
        wsModel.setReq(data);

        try {
            if (StringUtils.equals("getLocalApiMethod", requestMethod)) {
                JsonModel jsonModel = quickApiLogic.getLocalApiMethod(applicationContext);
                wsModel.success(jsonModel.getData());
            } else if (StringUtils.equals("getLocalDeleteApiMethodList", requestMethod)) {

            } else if (StringUtils.equals("callApi", requestMethod)) {
                Map<String, Object> requestData = (Map) map.get("requestData");
                String path = (String) requestData.get("path");
                String contentType = (String) requestData.get("contentType");
                JSONObject headerJson = (JSONObject) requestData.get("headerJson");
                JSONObject queryData = (JSONObject) requestData.get("queryData");
                String type = (String) requestData.get("type");
                Object callResult = RequestUtil.callApi(path, contentType, headerJson.toJSONString(),
                        queryData.toJSONString(), type);

                wsModel.success(callResult);
            }
        } catch (Exception e) {
            logger.error("[QuickApi] on message error: " + e.getLocalizedMessage());
            wsModel.error(e);
        }

        session.getBasicRemote().sendText(JSONObject.toJSONString(wsModel));
    }
}

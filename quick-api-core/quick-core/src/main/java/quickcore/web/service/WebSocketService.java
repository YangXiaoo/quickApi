package quickcore.web.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;
import quickcore.common.tools.JsonModel;
import quickcore.core.utils.StringUtils;
import quickcore.web.logic.QuickApiLogic;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;

@ServerEndpoint(value = "/ws")
@Component
public class WebSocketService {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    //@Autowired
    private static QuickApiLogic quickApiLogic; //关键点2

    @Autowired  //关键点3
    public void setQuickApiLogic (QuickApiLogic quickApiLogic){
        WebSocketService.quickApiLogic = quickApiLogic;
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("连接成功");
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("连接关闭");
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
        Object ret = null;
        Map<String, Object> map =  JSONObject.parseObject(data, new TypeReference<Map<String, Object>>(){});
        String requestMethod = (String) map.get("requestMethod");
        if (StringUtils.equals("getLocalApiMethod", requestMethod)) {
            JsonModel jsonModel = quickApiLogic.loadQApi();
            ret = jsonModel.getData();
        } else if (StringUtils.equals("getLocalDeleteApiMethodList", requestMethod)) {

        }

        session.getBasicRemote().sendText(JSONObject.toJSONString(ret));
    }
}

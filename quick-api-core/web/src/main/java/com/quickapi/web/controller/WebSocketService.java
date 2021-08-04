//package com.quickapi.web.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//
//@ServerEndpoint("/ws")
//@Component
//public class WebSocketService {
//    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);
//
//    @OnOpen
//    public void onOpen(Session session) {
//        logger.info("连接成功");
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        logger.info("连接关闭");
//    }
//
//    @OnMessage
//    public void onMsg(Session session, String message) throws IOException {
//        message = "key: " + message + ", value: {name:'yangxiao'}";
//        logger.info(message);
//        session.getBasicRemote().sendText(message);
//    }
//}

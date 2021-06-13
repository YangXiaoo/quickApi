package com.quickapi.server.web.service.impl;

import com.quickapi.server.common.utils.RequestUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UtilServiceImpl {
    /**
     * 前端请求本地接口
     * @param path 请求路径
     * @param contentType 请求内容类型
     * @param headerJson 请求头
     * @param queryData 请求数据
     * @param type 类型
     * @return java.lang.Object
     * @author yangxiao
     * @date 2020/11/27 18:01
     */
    @GetMapping("/callApi")
    public Object callApi(String path, String contentType, String headerJson, String queryData, String type) {
        return RequestUtil.callApi(path, contentType, headerJson, queryData, type);
    }
}

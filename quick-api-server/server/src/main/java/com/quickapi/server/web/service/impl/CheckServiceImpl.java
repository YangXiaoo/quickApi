package com.quickapi.server.web.service.impl;

import com.quickapi.server.common.constant.JSON_MODEL_CODE;
import com.quickapi.server.common.utils.JsonModel;
import com.quickapi.server.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 状态检查
 * @author yangxiao
 */
@RestController
@RequestMapping("/api/check")
public class CheckServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(CheckServiceImpl.class);
    /**
     * 检查服务器是否连接成功
     * @param map 无参数
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/3 21:59
     */
    @PostMapping("checkServerStatus")
    public JsonModel checkServerStatus(@RequestBody Map<String, Object> map) {
        logger.info("checkServerStatus");
        JsonModel jsonModel = new JsonModel();
        try {
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "服务器连接成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error("未知错误!");
        }

        return jsonModel;
    }
}

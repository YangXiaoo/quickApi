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

@RestController
@RequestMapping("/api/login")
public class LoginService {
    private static final Logger logger = LoggerFactory.getLogger(CheckServiceImpl.class);

    /**
     * 用户登录
     * @param map
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/3/30 21:52
     */
    @PostMapping("/loginCheck")
    public JsonModel loginCheck(@RequestBody Map<String, Object> map) {
        logger.info("loginCheck");
        JsonModel jsonModel = new JsonModel();
        try {
            String username = (String) map.get("username");
            String password = (String) map.get("password");
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, map);
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }
    @PostMapping("/logout")
    public JsonModel logout(@RequestBody Map<String, Object> map) {
        logger.info("logout");
        JsonModel jsonModel = new JsonModel();
        try {
            String username = (String) map.get("username");
            String password = (String) map.get("password");
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, true);
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

}
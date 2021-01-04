package com.quickapi.server.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.constant.JSON_MODEL_CODE;
import com.quickapi.server.common.utils.JsonModel;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.entity.MethodModel;
import com.quickapi.server.web.logic.MethodApiDataLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 接口文档
 * @author yangxiao
 */
@RestController
@RequestMapping("/api/methodApiData")
public class MethodApiDataServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(MethodDataServiceImpl.class);

    @Autowired
    private MethodApiDataLogic apiDataLogic;

    /**
     * 查找接口文档数据
     * @param map 查询条件
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/4 21:59
     */
    @PostMapping("getMethodApiData")
    public JsonModel getMethodApiData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            String url = (String) map.get("url");
            if (StringUtils.isBlank(projectName) || StringUtils.isBlank(url)) {
                throw new BusinessException("getMethodApiData()参数不完整");
            }
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, apiDataLogic.findMethodApiData(projectName, url));
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error("未知错误!");
        }

        return jsonModel;
    }

    /**
     * 保存接口文档数据
     * @param map 查询条件
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/4 21:59
     */
    @PostMapping("saveMethodApiData")
    public JsonModel saveMethodApiData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            String url = (String) map.get("url");
            if (StringUtils.isBlank(projectName) || StringUtils.isBlank(url)) {
                throw new BusinessException("getMethodApiData()参数不完整");
            }
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, apiDataLogic.findMethodApiData(projectName, url));
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error("未知错误!");
        }

        return jsonModel;
    }
}

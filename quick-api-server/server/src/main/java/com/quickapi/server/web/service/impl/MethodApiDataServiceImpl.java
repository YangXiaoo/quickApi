package com.quickapi.server.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.constant.JSON_MODEL_CODE;
import com.quickapi.server.common.utils.JsonModel;
import com.quickapi.server.exception.BusinessException;
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
@RequestMapping("/api")
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
    public JsonModel getProjectMethodApiData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            String url = (String) map.get("url");
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, apiDataLogic.findMethodApiData(projectName, url));
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 查找用户提交记录
     * @return java.util.List<com.quickapi.server.web.dao.entity.ApiDoc>
     * @author yangxiao
     * @date 2021/1/26 21:00
     */
    @PostMapping("getProjectMethodApiDataHistory")
    public JsonModel getProjectMethodApiDataHistory(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            String userName = (String) map.get("userName");
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, apiDataLogic.getProjectMethodApiDataHistory(projectName, userName));
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
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
    public JsonModel saveProjectMethodApiData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            String url = (String) map.get("url");
            String apiData = (String) map.get("apiData");
            String author = (String) map.get("author");
            if (StringUtils.isBlank(projectName) || StringUtils.isBlank(url)) {
                throw new BusinessException("getMethodApiData()参数不完整");
            }
            apiDataLogic.saveMethodApiData(projectName, url, apiData, author);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "保存成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 删除接口
     * @param map 主键ID
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/18 19:49
     */
    @PostMapping("deleteMethodApiData")
    public JsonModel deleteProjectMethodApiData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String apiDocId = (String) map.get("apiDocId");
            apiDataLogic.deleteMethodApiData(apiDocId);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "删除成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 根据URL查询用户接口文档数据
     * @param map 查询条件
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/14 20:59
     */
    @PostMapping("getUserMethodApiData")
    public JsonModel getUserMethodApiData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String userName = (String) map.get("userName");
            String url = (String) map.get("url");
            if (StringUtils.isBlank(userName) || StringUtils.isBlank(url)) {
                throw new BusinessException("getUserMethodApiData()参数不完整");
            }
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, apiDataLogic.findUserMethodApiData(userName, url));
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 保存用户接口文档数据
     * @param map 保存参数
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/14 21:00
     */
    @PostMapping("saveUserMethodApiData")
    public JsonModel saveUserMethodApiData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String userName = (String) map.get("userName");
            String url = (String) map.get("url");
            String apiData = (String) map.get("apiData");
            if (StringUtils.isBlank(userName) || StringUtils.isBlank(url)) {
                throw new BusinessException("saveUserMethodApiData()参数不完整");
            }
            apiDataLogic.saveUserMethodApiData(userName, url, apiData);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "保存成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 删除个人接口
     * @param map 用户名，url
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/20 21:19
     */
    @PostMapping("deleteUserMethodApiData")
    public JsonModel deleteUserMethodApiData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String userName = (String) map.get("userName");
            String url = (String) map.get("url");
            apiDataLogic.deleteUserMethodApiData(userName, url);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "删除成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }
}

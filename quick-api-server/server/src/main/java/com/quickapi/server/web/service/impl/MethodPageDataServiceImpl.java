package com.quickapi.server.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.constant.JSON_MODEL_CODE;
import com.quickapi.server.common.utils.JsonModel;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.logic.MethodPageDataLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MethodPageDataServiceImpl {
    @Autowired
    private MethodPageDataLogic methodPageDataLogic;

    /**
     * 保存用户测试项目页面数据
     * @param map 插入数据
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/17 17:49
     */
    @PostMapping("saveUserProjectMethodPageData")
    public JsonModel saveUserProjectMethodPageData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            String url = (String) map.get("url");
            String apiData = (String) map.get("pageData");
            String author = (String) map.get("author");
            if (StringUtils.isBlank(projectName) || StringUtils.isBlank(url)) {
                throw new BusinessException("saveUserProjectMethodPageData()参数不完整");
            }
            methodPageDataLogic.saveUserProjectMethodPageData(projectName, url, apiData, author);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "保存成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 获得用户页面pageData字段String类型数据，需要前端转为JSON
     * @param map 查询条件
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/17 17:50
     */
    @PostMapping("getUserProjectMethodPageData")
    public JsonModel getUserProjectMethodPageData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            String url = (String) map.get("url");
            String userName = (String) map.get("author");
            if (StringUtils.isBlank(projectName) || StringUtils.isBlank(url) || StringUtils.isBlank(userName)) {
                throw new BusinessException("getUserProjectMethodPageData()参数不完整");
            }
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, methodPageDataLogic.getUserProjectMethodPageData(projectName, url, userName));
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }
}

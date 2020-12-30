package com.quickapi.server.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.constant.JSON_MODEL_CODE;
import com.quickapi.server.common.utils.JsonModel;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.entity.ApiDataEntity;
import com.quickapi.server.web.logic.ApiDataLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiDataServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(ApiDataServiceImpl.class);

    @Autowired
    private ApiDataLogic apiDataLogic;
    /**
     * 根据项目名获取该项目的所有接口信息
     * @param map 项目名
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2020/12/29 20:46
     */
    @RequestMapping(value = "geApiDataByProjectName", method = RequestMethod.POST)
    public JsonModel geApiDataByProjectName(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            if (StringUtils.isBlank(projectName)) {
                throw new BusinessException("没有传入项目名");
            }

            List<ApiDataEntity> res = apiDataLogic.findApiDataByProjectName(projectName);
            if (res != null) {
                logger.info(res.size() + "");
            }

            jsonModel.success(JSON_MODEL_CODE.SUCCESS, res);
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            jsonModel.error("未知错误!");
        }

        return jsonModel;
    }

    /**
     * 保存个接口信息
     * @param map 接口信息
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2020/12/29 21:43
     */
    @RequestMapping(value = "saveApiData", method = RequestMethod.POST)
    public JsonModel saveApiData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            ApiDataEntity apiDataEntity = (ApiDataEntity) map.get("data");
            if (apiDataEntity == null) {
                throw new BusinessException("没有数据");
            }

            apiDataLogic.insertApiData(apiDataEntity);

            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "保存成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error("未知错误!");
        }

        return jsonModel;
    }
}

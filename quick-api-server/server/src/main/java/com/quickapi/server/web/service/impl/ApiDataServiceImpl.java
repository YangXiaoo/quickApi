package com.quickapi.server.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.constant.JSON_MODEL_CODE;
import com.quickapi.server.common.utils.JsonModel;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.entity.MethodModel;
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
    @RequestMapping(value = "getApiDataByProjectName", method = RequestMethod.POST)
    public JsonModel getApiDataByProjectName(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            if (StringUtils.isBlank(projectName)) {
                throw new BusinessException("没有传入项目名");
            }

            List<MethodModel> res = apiDataLogic.findApiDataByProjectName(projectName);
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
    @RequestMapping(value = "saveMethodData", method = RequestMethod.POST)
    public JsonModel saveMethodData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            MethodModel methodModel = (MethodModel) map.get("data");
            if (methodModel == null) {
                throw new BusinessException("没有数据");
            }

            apiDataLogic.insertApiData(methodModel);

            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "保存成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error("未知错误!");
        }

        return jsonModel;
    }

    /**
     * @param map 查询条件
     *            projectName 项目名
     *            data List<MethodModel> 接口数据
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/3 21:52
     */
    @PostMapping("deleteMethodInfoList")
    public JsonModel deleteMethodInfoList(@RequestBody Map<String, Object> map) {
        // todo
        JsonModel jsonModel = new JsonModel();
        try {

            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "删除数据成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error("未知错误!");
        }

        return jsonModel;
    }

    /**
     * 检查服务器是否连接成功
     * @param map 无参数
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/3 21:59
     */
    @PostMapping("checkServerStatus")
    public JsonModel checkServerStatus(@RequestBody Map<String, Object> map) {
        // todo
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

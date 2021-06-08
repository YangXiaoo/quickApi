package com.quickapi.server.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickapi.server.common.constant.JSON_MODEL_CODE;
import com.quickapi.server.common.utils.JsonModel;
import com.quickapi.server.common.utils.ModelUtil;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.entity.MethodModel;
import com.quickapi.server.web.logic.MethodDataLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 接口方法
 * @author yangxiao
 */
@RestController
@RequestMapping("/api")
public class MethodDataServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(MethodDataServiceImpl.class);

    @Autowired
    private MethodDataLogic methodDataLogic;
    /**
     * 根据项目名获取该项目的所有接口信息
     * @param map 项目名
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2020/12/29 20:46
     */
    @RequestMapping(value = "getMethodDataByProjectName", method = RequestMethod.POST)
    public JsonModel getMethodDataByProjectName(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            if (StringUtils.isBlank(projectName)) {
                throw new BusinessException("没有传入项目名");
            }

            List<MethodModel> res = methodDataLogic.findApiDataByProjectName(projectName);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, res);
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 保存接口信息
     * @param map 接口信息
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2020/12/29 21:43
     */
    @PostMapping("saveMethodData")
    public JsonModel saveMethodData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            Object object = map.get("methodModel");
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> methodModelMap = objectMapper.convertValue(object, Map.class);
            MethodModel methodModel = ModelUtil.mapToMethodModel(methodModelMap);
            if (methodModel == null) {
                throw new BusinessException("没有数据");
            }
            methodDataLogic.saveMethodData(methodModel);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "保存成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 删除一组接口方法数据
     * @param map 查询条件
     *            projectName 项目名
     *            data List<MethodModel> 接口数据
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/3 21:52
     */
    @PostMapping("deleteMethodDataList")
    public JsonModel deleteMethodDataList(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            Object object = map.get("data");
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> objList = objectMapper.convertValue(object, List.class);
            List<MethodModel> methodModelList = new ArrayList<>();
            for (Map<String, Object> obj : objList) {
                methodModelList.add(ModelUtil.mapToMethodModel(obj));
            }
            methodDataLogic.deleteMethodDataList(methodModelList);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "删除数据成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 更新方法信息
     * @param map 保存数据
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/14 21:09
     */
    @PostMapping("updateMethodData")
    public JsonModel updateMethodData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String url = (String) map.get("url");
            String projectName = (String) map.get("projectName");
            String name = (String) map.get("name");
            String methodGroup = (String) map.get("methodGroup");
            methodDataLogic.updateMethodData(url, projectName, name, methodGroup);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "更新成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 保存用户方法信息
     * @param map 保存条件
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/14 21:54
     */
    @PostMapping("saveUserMethodData")
    public JsonModel saveUserMethodData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String url = (String) map.get("url");
            String userName = (String) map.get("userName");
            String name = (String) map.get("methodName");
            String methodGroup = (String) map.get("methodGroup");
            methodDataLogic.saveUserMethodData(url, userName, name, methodGroup);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "保存成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }
    /**
     * 查询用户所有方法信息
     * @param map 查询条件
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/14 21:16
     */
    @PostMapping("getUserMethodDataList")
    public JsonModel getUserMethodDataList(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String userName = (String) map.get("userName");
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, methodDataLogic.getUserMethodDataList(userName));
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 更新用户方法信息
     * @param map 更新条件
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/14 21:28
     */
    @PostMapping("updateUserMethodData")
    public JsonModel updateUserMethodData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String url = (String) map.get("url");
            String userName = (String) map.get("userName");
            String name = (String) map.get("methodName");
            String methodGroup = (String) map.get("methodGroup");
            methodDataLogic.updateUserMethodData(url, userName, name, methodGroup);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "更新成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 删除用户接口方法
     * @param map 删除条件
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/14 21:51
     */
    @PostMapping("deleteUserMethodData")
    public JsonModel deleteUserMethodData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String url = (String) map.get("url");
            String userName = (String) map.get("userName");
            methodDataLogic.deleteUserMethodData(url, userName);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "删除成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 项目已完成接口文档数量
     * @param map
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/25 20:08
     */
    @PostMapping("getProjectFinishedMethodDataCount")
    public JsonModel getProjectFinishedMethodDataCount(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            jsonModel.success(JSON_MODEL_CODE.SUCCESS,
                    methodDataLogic.getProjectFinishedMethodDataCount(projectName));
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 项目已完成接口详细信息
     * @param map
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/25 20:08
     */
    @PostMapping("getProjectFinishedMethodDataMap")
    public JsonModel getProjectFinishedMethodDataMap(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            jsonModel.success(JSON_MODEL_CODE.SUCCESS,
                    methodDataLogic.getProjectFinishedMethodDataMap(projectName));
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }
}

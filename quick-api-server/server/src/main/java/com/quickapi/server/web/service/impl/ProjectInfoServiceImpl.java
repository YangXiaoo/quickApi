package com.quickapi.server.web.service.impl;

import com.quickapi.server.common.constant.JSON_MODEL_CODE;
import com.quickapi.server.common.utils.JsonModel;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.entity.ProjectInfo;
import com.quickapi.server.web.logic.ProjectInfoLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 项目信息
 * @author yangxiao
 */
@RestController
@RequestMapping("/api/project")
public class ProjectInfoServiceImpl {
    @Autowired
    private ProjectInfoLogic projectInfoLogic;

    /**
     * 保存项目信息
     * <p>
     *     存在更新，不存在插入
     * </p>
     * @param map 接口信息
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2020/12/29 21:43
     */
    @RequestMapping(value = "saveProjectInfo", method = RequestMethod.POST)
    public JsonModel saveProjectInfo(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            ProjectInfo projectInfo = (ProjectInfo) map.get("projectInfo");
            if (projectInfo == null) {
                throw new BusinessException("没有数据");
            }
            projectInfoLogic.saveProjectInfo(projectInfo);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "保存成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error("未知错误!");
        }

        return jsonModel;
    }
}

package com.quickapi.server.web.service.impl;

import com.quickapi.server.common.constant.JSON_MODEL_CODE;
import com.quickapi.server.common.utils.JsonModel;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.logic.UserHeaderPresetsLogic;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/presets")
public class UserHeaderPresetsImpl {
    private static final Logger log = LoggerFactory.getLogger(ProjectInfoServiceImpl.class);

    @Autowired
    private UserHeaderPresetsLogic userHeaderPresetsLogic;

    @PostMapping("addUserHeaderPresets")
    public JsonModel addUserHeaderPresets(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        JsonModel jsonModel = new JsonModel();
        try {
            String presetId = MapUtils.getString(map, "presetId");
            String userName = MapUtils.getString(map, "userName");
            String name = MapUtils.getString(map, "name");
            String value = MapUtils.getString(map, "value");

            userHeaderPresetsLogic.addUserHeaderPresets(presetId, userName, name, value);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "保存成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    @PostMapping("getUserHeaderPresets")
    public JsonModel getUserHeaderPresets(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        JsonModel jsonModel = new JsonModel();
        try {
            String userName = MapUtils.getString(map, "userName");

            jsonModel.success(JSON_MODEL_CODE.SUCCESS, userHeaderPresetsLogic.getUserHeaderPresets(userName));
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    @PostMapping("deleteUserHeaderPresets")
    public JsonModel deleteUserHeaderPresets(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        JsonModel jsonModel = new JsonModel();
        try {
            String presetId = MapUtils.getString(map, "presetId");

            userHeaderPresetsLogic.deleteUserHeaderPresets(presetId);
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "删除成功");
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

}

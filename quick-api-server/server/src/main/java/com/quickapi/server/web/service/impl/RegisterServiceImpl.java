package com.quickapi.server.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.constant.JSON_MODEL_CODE;
import com.quickapi.server.common.tools.DateTool;
import com.quickapi.server.common.utils.JsonModel;
import com.quickapi.server.exception.BusinessException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/register")
public class RegisterServiceImpl {
    private ConcurrentHashMap<String, List<String>> runningService = new ConcurrentHashMap<>(); // 记录运行中的服务器
    private  ConcurrentHashMap<String, Date> serviceTimeRecord = new ConcurrentHashMap<>();
    /**
     * 测试端每30s往服务器发送消息
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/27 20:53
     */
    @PostMapping(value = "reportStatus")
    public JsonModel reportStatus(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            String ip = (String) map.get("ip");
            String port = (String) map.get("port");
            if (StringUtils.isBlank(projectName) || StringUtils.isBlank(ip) || StringUtils.isBlank(port)) {
                throw new BusinessException("信息为空");
            }
            
            String address = ip + ":" + port;
            serviceTimeRecord.put(address, DateTool.getCurrentDate());
            List<String> runningServiceAddressList = runningService.get(projectName);
            if (runningServiceAddressList == null) {
                List<String> tmp = new ArrayList<>();
                tmp.add(address);
                runningService.put(projectName, tmp);
            } else {
                if (!runningServiceAddressList.contains(address)) {
                    runningServiceAddressList.add(address);
                }
            }
            
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "success");
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 获取正在运行的服务器地址
     * @return com.quickapi.server.common.utils.JsonModel
     * @author yangxiao
     * @date 2021/1/27 20:54
     */
    @PostMapping(value = "getRunningService")
    public JsonModel getRunningService(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            if (StringUtils.isBlank(projectName)) {
                throw new BusinessException("项目名为空");
            }
            List<String> result = new ArrayList<>();
            List<String> runningServiceAddressList = runningService.get(projectName);
            if (runningServiceAddressList == null) {
                throw new BusinessException("没有服务器可用");
            }
            // 一分钟内成功连接超过一次才算连接正常，否则将被移除
            for (String address : runningServiceAddressList) {
                Date lastDate = serviceTimeRecord.get(address);
                long diff = (DateTool.getCurrentDate().getTime() - lastDate.getTime()) / 1000;
                if (diff <= 60) {
                    result.add(address);
                } else {
                    // 移除
                    runningServiceAddressList.remove(address);
                }
            }

            jsonModel.success(JSON_MODEL_CODE.SUCCESS, result);
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }
}

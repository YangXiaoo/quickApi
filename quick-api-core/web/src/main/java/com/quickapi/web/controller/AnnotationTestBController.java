package com.quickapi.web.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import quickcore.annotations.component.QApi;
import quickcore.common.tools.JsonModel;

import java.util.Map;

@RestController
@RequestMapping("/annotationB")
@QApi(group = "菜单B")
public class AnnotationTestBController {
    @RequestMapping(value = "pathB-A", method = RequestMethod.POST)
    public JsonModel pathA(@RequestBody Map<String, String> map) {
        JsonModel jsonModel = new JsonModel();
        jsonModel.success("成功", map);
        return jsonModel;
    }

    @RequestMapping(value = "pathB-B", method = RequestMethod.POST)
    public JsonModel pathB(@RequestBody Map<String, String> map) {
        JsonModel jsonModel = new JsonModel();
        jsonModel.success("成功", map);
        return jsonModel;
    }
}

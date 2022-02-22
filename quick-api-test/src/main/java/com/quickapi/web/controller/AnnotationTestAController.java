package com.quickapi.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quickcore.annotations.component.QApi;
import quickcore.common.tools.JsonModel;

@RestController
@RequestMapping("/annotationA")
@QApi(group = "菜单A")
public class AnnotationTestAController {
    @RequestMapping(value = "pathA-A")
    public JsonModel pathA() {
        JsonModel jsonModel = new JsonModel();
        jsonModel.success("成功", "pathA-A");
        return jsonModel;
    }

    @RequestMapping(value = "pathA-B")
    public JsonModel pathB() {
        JsonModel jsonModel = new JsonModel();
        jsonModel.success("成功", "pathA-B");
        return jsonModel;
    }

    @RequestMapping(value = "pathA-C")
    public JsonModel pathC() {
        JsonModel jsonModel = new JsonModel();
        jsonModel.success("成功", "pathA-c");
        return jsonModel;
    }
}

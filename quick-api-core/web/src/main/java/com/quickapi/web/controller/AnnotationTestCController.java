package com.quickapi.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import quickcore.annotations.component.QApi;
import quickcore.common.JsonModel;

@RestController
@RequestMapping("/annotationC")
@QApi(group = "子菜单C")
public class AnnotationTestCController {
    @RequestMapping(value = "/annotationCMehtodA", method = RequestMethod.POST)
    public JsonModel annotationCMehtodA(String dataA, String dataB) {
        JsonModel jsonModel = new JsonModel();
        jsonModel.success("成功", dataA + dataB);
        return jsonModel;
    }
}
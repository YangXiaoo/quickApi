package com.quickapi.web.controller;

import org.springframework.web.bind.annotation.*;
import quickcore.annotations.component.QApi;
import quickcore.common.tools.JsonModel;

import java.util.Map;

@RestController
@RequestMapping("/annotationD")
@QApi(group = "菜单D")
public class AnnotationDTestController {
    @RequestMapping(value = "TestController/index", method = RequestMethod.POST)
    public JsonModel testCindex(@RequestBody Map<String, String> map) {
        System.out.println(map.toString());
        JsonModel jsonModel = new JsonModel();
        jsonModel.success("成功", map);
        return jsonModel;
    }
}

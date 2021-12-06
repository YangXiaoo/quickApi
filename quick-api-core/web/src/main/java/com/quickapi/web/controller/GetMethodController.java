package com.quickapi.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quickcore.common.tools.JsonModel;

@RestController
@RequestMapping("/getMethod")
public class GetMethodController {
    @GetMapping(value = "/methodA")
    public JsonModel methodA() {
        JsonModel jsonModel = new JsonModel();
        jsonModel.success("成功", "get methodA");
        return jsonModel;
    }
}

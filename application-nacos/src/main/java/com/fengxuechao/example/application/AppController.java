package com.fengxuechao.example.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengxuechao
 * @date 2020/5/11
 */
@RestController
public class AppController {

    @Autowired
    private AppInfoProperties appInfoProperties;

    @Autowired
    private OtherInfoProperties otherInfoProperties;

    @RequestMapping("/app/info")
    public AppInfoProperties getAppInfoProperties() {
        return appInfoProperties;
    }

    @RequestMapping("/other/info")
    public OtherInfoProperties getOtherInfoProperties() {
        return otherInfoProperties;
    }
}

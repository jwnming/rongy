package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.entity.Config;
import com.cd.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping("/configs")
    public RestResult getConfig() {
        List<Config> configList = configService.list();

        return ResultUtil.createSuccessResult(configList);
    }

}

package com.cd.controller;

import com.cd.common.RestResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/goodsCategory")
public class MarketGoodsCategoryController {


    @PostMapping("/xx")
    public RestResult xxx(HttpServletRequest request) {

        return null;
    }
}

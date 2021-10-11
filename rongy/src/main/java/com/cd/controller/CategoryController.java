package com.cd.controller;

import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.entity.Category;
import com.cd.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/user/{userNo}/category")
    public RestResult listByUserNo(@PathVariable("userNo") String userNo) {
        List<Category> categories = categoryService.listByUserNo(userNo);

        return ResultUtil.createSuccessResult(categories);
    }

    @GetMapping("/getDynamicTypes")
    public RestResult getDynamicTypes(){
        try {
            return ResultUtil.createSuccessResult(categoryService.getDynamicTypes());
        } catch (Exception e) {
            return ResultUtil.createFailedResult(e);
        }
    }



}

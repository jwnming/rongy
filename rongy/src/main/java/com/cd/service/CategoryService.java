package com.cd.service;

import com.cd.entity.Category;

import java.util.List;

public interface CategoryService {

    /**
     * 根据 userNo 获取可选的 类别
     * @param userNo
     * @return
     */
    List<Category> listByUserNo(String userNo);

    List<Category> getDynamicTypes();

}

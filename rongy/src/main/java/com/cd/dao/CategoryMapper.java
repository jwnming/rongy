package com.cd.dao;

import com.cd.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> listByRoleId(String roleId);

    Category selectByTypeNoAndRoleId(@Param("typeNo") String typeNo, @Param("roleId") String roleId);

    List<Category> getDynamicTypes();
}
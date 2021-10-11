package com.cd.service.impl;

import com.cd.dao.CategoryMapper;
import com.cd.dao.UserInfoMapper;
import com.cd.entity.Category;
import com.cd.entity.UserInfo;
import com.cd.enums.ErrorCodeEnum;
import com.cd.enums.RongyDictEnum;
import com.cd.exception.BusiException;
import com.cd.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> listByUserNo(String userNo) {
        // user -> roleId -> category
        UserInfo userInfo = userInfoMapper.selectByUserNo(userNo);
        if (userInfo == null) {
            throw new BusiException(ErrorCodeEnum.USER_NO_MATCH);
        }

        if (StringUtils.equals(userInfo.getStatus(), RongyDictEnum.USER_INFO_STATUS$INVALID.getCode())) {
            throw new BusiException(ErrorCodeEnum.UER_STATUS_INVALID);
        }

        return categoryMapper.listByRoleId(userInfo.getRoleId());
    }

    @Override
    public List<Category> getDynamicTypes() {
        return categoryMapper.getDynamicTypes();
    }
}

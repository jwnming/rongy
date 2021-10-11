package com.cd.dao;

import com.cd.entity.WorkerInfo;

public interface WorkerInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkerInfo record);

    int insertSelective(WorkerInfo record);

    WorkerInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkerInfo record);

    int updateByPrimaryKey(WorkerInfo record);
}
package com.cd.service;

import com.cd.entity.TeamActivityType;
import com.cd.entity.vo.TeamVO;

import java.util.List;

public interface TeamActivityTypeService {
    /**
     * 查询所有的组团大类
     * @return 组团大类类型列表
     */
    public List<TeamActivityType> queryAllTeamActType();

    public TeamVO queryTeamDetail(String typeNo, String userNo);
}

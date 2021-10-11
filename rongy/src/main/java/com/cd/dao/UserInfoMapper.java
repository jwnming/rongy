package com.cd.dao;

import com.cd.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    /**
     * 根据用户编号更新信息
     * @param userInfo
     * @return
     */
    int updateByUserNoSelective(UserInfo userInfo);

    /**
     * 根据用户编号查询
     * @param userNo
     * @return
     */
    UserInfo selectByUserNo(String userNo);

    /**
     * 根据用户编号查询
     * @param circleId
     * @return
     */
    List<UserInfo> selectByCircleId(Integer circleId);

    /**
     * 查询所有
     * @return
     */
    List<UserInfo> selectAll();

    /**
     * 根据姓名模糊查询
     * @return
     */
    List<UserInfo> selectByNameLike(String nameLike);

    UserInfo selectByOpenId(String openId);

    UserInfo selectByNameAndPhone(@Param("name") String name, @Param("phone") String phone);

    List<UserInfo> selectByActivityId(Integer id);

}
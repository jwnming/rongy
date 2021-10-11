package com.cd.service;

import com.cd.common.RestResult;
import com.cd.entity.Role;
import com.cd.entity.UserInfo;
import com.cd.entity.dto.UserLoginDTO;
import com.cd.entity.vo.RankingListVO;
import com.cd.entity.vo.RankingListVOs;
import com.cd.entity.vo.UserLoginVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserInfoService {

    /**
     * 根据用户编号查询用户信息
     * @param userNo
     * @return
     */
    RestResult getByUserNo(String userNo);

    /**
     * 根据他人编号查询信息
     * @param userNo
     * @return
     */
    RestResult getByOtherUserNo(String userNo);

    /**
     * 根据用户id修改用户信息
     * @param userInfo
     * @return
     */
    RestResult updateById(UserInfo userInfo);

    /**
     * 根据用户编号修改用户信息
     * @param userInfo
     * @return
     */
    RestResult updateByUserNo(UserInfo userInfo);

    /**
     * 根据圈子id修改用户信息，若为零，默认查询所有
     * @param circleId
     * @return
     */
    RestResult getByCircleId(Integer circleId);

    /**
     * 根据参数模糊匹配用户名字进行查询
     * @param userNo
     * @param nameLike
     * @return
     */
    RestResult getByNameLike(String userNo, String nameLike);

    UserLoginVO userLogin(UserLoginDTO userLogin);

    void readAll(String userNo);

    UserInfo getByNameAndPhone(String name, String phone);


    List<UserInfo> getAllUser(int pageNum, int pageSize);

    /**
     * 获取排行榜
     * @param userNo
     * @return
     */
    RankingListVOs getRankingList(String userNo);

    /**
     * Excel批量导入用户信息
     */
    void insertByExcel(MultipartFile multipartFile) throws Exception ;

    UserInfo getUserInfo(String userNo);

    void addSingle(UserInfo userInfo);

    // 角色查询
    Role roleValid(String userNo, String roleType);

    UserInfo userInfoValidate(String userNo);
}

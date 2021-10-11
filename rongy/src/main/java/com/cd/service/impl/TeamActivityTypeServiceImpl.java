package com.cd.service.impl;

import com.cd.dao.TeamActivityPromoteMapper;
import com.cd.dao.TeamActivityRelUserInfoMapper;
import com.cd.dao.TeamActivityTypeMapper;
import com.cd.dao.UserInfoMapper;
import com.cd.entity.TeamActivityPromote;
import com.cd.entity.TeamActivityType;
import com.cd.entity.vo.TeamVO;
import com.cd.enums.ErrorCodeEnum;
import com.cd.exception.BusiException;
import com.cd.service.TeamActivityTypeService;
import com.cd.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TeamActivityTypeServiceImpl implements TeamActivityTypeService {
    @Resource
    private TeamActivityTypeMapper teamActivityTypeMapper;
    @Resource
    private TeamActivityPromoteMapper teamActivityPromoteMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserInfoService userInfoService;
    @Resource
    private TeamActivityRelUserInfoMapper teamActivityRelUserInfoMapper;

    @Override
    public List<TeamActivityType> queryAllTeamActType() {
        List<TeamActivityType> teamActivityTypes = teamActivityTypeMapper.queryAll();
        for(TeamActivityType teamActivityType : teamActivityTypes){
            teamActivityType.setPublishNum(teamActivityPromoteMapper.count(String.valueOf(teamActivityType.getTypeNo())));
        }
        return teamActivityTypes;
    }

    @Override
    public TeamVO queryTeamDetail(String typeNo, String userNo) {
        TeamVO teamVO = teamActivityTypeMapper.selectByTypeNo(typeNo);
        if (teamVO == null) throw new BusiException(ErrorCodeEnum.TEAM_TYPE_NOT_SUPPORT);

        boolean isSendUserNo = false;
        if(StringUtils.isNotBlank(userNo)){
            userInfoService.userInfoValidate(userNo);
            isSendUserNo = true;
        }

        List<TeamActivityPromote> underway = teamActivityPromoteMapper.selectUnderwayByTypeNo(typeNo);
        for(TeamActivityPromote promote : underway){
            if(isSendUserNo){
                int count = teamActivityRelUserInfoMapper.isParticipateIn(userNo, promote.getId());
                if(count == 1){
                    promote.setParticipateIn(true);
                }
                if(StringUtils.equals(promote.getCreateBy(), userNo)){
                    promote.setCouldDelete(true);
                }else{
                    promote.setCouldDelete(false);
                }
            }
            promote.setCreaterInfo(userInfoMapper.selectByUserNo(promote.getCreateBy()));
        }
        teamVO.setUnderway(underway);
        List<TeamActivityPromote> finished = teamActivityPromoteMapper.selectFinishedByTypeNo(typeNo);
        for(TeamActivityPromote promote : finished){
            if(isSendUserNo){
                int count = teamActivityRelUserInfoMapper.isParticipateIn(userNo, promote.getId());
                if(count == 1){
                    promote.setParticipateIn(true);
                }
                if(StringUtils.equals(promote.getCreateBy(), userNo)){
                    promote.setCouldDelete(true);
                }else{
                    promote.setCouldDelete(false);
                }
            }
            promote.setCreaterInfo(userInfoMapper.selectByUserNo(promote.getCreateBy()));
        }
        teamVO.setFinished(finished);
        teamVO.setPublishNum(teamActivityPromoteMapper.count(typeNo));
        return teamVO;
    }
}

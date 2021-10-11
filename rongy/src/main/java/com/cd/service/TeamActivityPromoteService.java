package com.cd.service;

import com.cd.entity.TeamActivityPromote;

public interface TeamActivityPromoteService {

    void create(TeamActivityPromote teamActivityPromote);

    void signUpActivity(TeamActivityPromote teamActivityPromote);

    void cancelSignUpActivity(TeamActivityPromote teamActivityPromote);

    TeamActivityPromote qryActivityDetail(Integer id);

    void del(String userNo, Integer id);
}

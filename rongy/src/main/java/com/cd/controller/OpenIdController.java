package com.cd.controller;

import com.alibaba.fastjson.JSON;
import com.cd.common.MiniappConstants;
import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.entity.miniapp.LoginResp;
import com.cd.utils.HttpUtil;
import com.cd.utils.MiniappNotificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenIdController {

    @Autowired
    private MiniappNotificationUtil miniappNotificationUtil;

    @GetMapping("/openId")
    public RestResult getOpenId(@RequestParam("appid") String appId, @RequestParam("secret") String appSecret,
                                @RequestParam("js_code") String jsCode, @RequestParam("grant_type") String grantType) {
        LoginResp resp = HttpUtil.miniAppLogin(appId, appSecret, jsCode, grantType);

        if (resp != null && resp.getErrCode() == MiniappConstants.API_OK) {
            return ResultUtil.createSuccessResult(JSON.toJSONString(resp));
        }

        return ResultUtil.createFailedResult(resp);
    }

    @GetMapping("/tmpl_like")
    public void testTmpl() {
        miniappNotificationUtil.onLike("oEHh55AstjhMafWyDRd01kJEEfOc", "test_commenter", "test_article", 1);
    }

    @GetMapping("/tmpl_comment")
    public void testTmpl2() {
        miniappNotificationUtil.onComment("oEHh55AstjhMafWyDRd01kJEEfOc", "test_commenter", "test_title","test_content");
    }

}

package com.cd;

import com.cd.dao.UserInfoMapper;
import com.cd.entity.UserInfo;
import com.cd.utils.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest
class SecurityTests {
    /*@Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private PlatformTransactionManager txManager;*/

    @Test
    void contextLoads1() {
        String phoneNum = "66666666";
        System.out.println("手机号：" + phoneNum);
        String s1 = SecurityUtil.phoneNumEncrypt(phoneNum);
        System.out.println("手机号加密：" + s1);
        String s2 = SecurityUtil.phoneNumDecrypt(phoneNum);
        System.out.println("手机号解密：" + s2);
    }

    // 将所有手机手机号使用加密函数进行加密
    /*@Test
    public void updateUserTelephone() {
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        try {
            List<UserInfo> userInfos = userInfoMapper.selectAll();
            for (int i = 0; i < userInfos.size(); i++) {
                UserInfo userInfo = userInfos.get(i);
                String s = SecurityUtil.phoneNumEncrypt(userInfo.getTelephone());
                userInfo.setTelephone(s);
                int i1 = userInfoMapper.updateByPrimaryKey(userInfo);
                if (i1 != 1) {
                    throw new RuntimeException();
                }
            }
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
        }

    }*/

}

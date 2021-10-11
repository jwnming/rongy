package com.cd.aspect;

import com.cd.enums.ErrorCodeEnum;
import com.cd.exception.BusiException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mayn
 */
@Aspect
@Component
public class Aspect4Post {

    private static final String SIGNATURE_VALUE = "rongyu";
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postController(){
    }

    @Before("postController()")
    public void doAround(JoinPoint jp) {
        HttpServletRequest request = (HttpServletRequest)jp.getArgs()[0];
        String signature = request.getHeader("Signature");
        if (!StringUtils.equals(SIGNATURE_VALUE, signature)){
            throw new BusiException(ErrorCodeEnum.SIGNATURE_FAILED);
        }
    }

}

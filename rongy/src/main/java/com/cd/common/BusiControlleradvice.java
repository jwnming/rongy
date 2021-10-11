package com.cd.common;

import com.cd.exception.BusiException;
import com.cd.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wanggq
 * @date ${date}${time}
 * @description
 */
@ControllerAdvice
public class BusiControlleradvice {

    private static final Logger log = LoggerFactory.getLogger(BusiControlleradvice.class);
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public RestResult exceptionHandler(Exception e){
        log.error("出现未知异常", e);
        return ResultUtil.createFailedResult(e);
    }

    @ResponseBody
    @ExceptionHandler(value = SystemException.class)
    public RestResult exceptionHandler(SystemException e){
        log.error("出现系统异常", e);
        return ResultUtil.createFailedResult(e);
    }

    @ResponseBody
    @ExceptionHandler(value = BusiException.class)
    public RestResult exceptionHandler(BusiException e){
        log.error("出现业务异常", e);
        return ResultUtil.createFailedResult(e);
    }

}

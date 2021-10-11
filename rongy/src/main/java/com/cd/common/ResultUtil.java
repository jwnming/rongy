package com.cd.common;

import com.cd.enums.ErrorCodeEnum;
import com.cd.exception.BusiException;
import com.cd.exception.SystemException;


/**  
* <p>Title: ResultUtil</p>  
* <p>Description: 统一rest返回结果生成工具类</p>  
* @author wanggq  
* @date 2019年6月26日  
*/  
public class ResultUtil {

    public static RestResult createSuccessResult(){

        Header header = new Header();
        header.setCode(ErrorCodeEnum.SUCCESS.getCode());
        header.setMsg(ErrorCodeEnum.SUCCESS.getMsg());
        return new RestResult(header, null);
    }


    public static RestResult createSuccessResult(Object data){

        Header header = new Header();
        header.setCode(ErrorCodeEnum.SUCCESS.getCode());
        header.setMsg(ErrorCodeEnum.SUCCESS.getMsg());
        return new RestResult(header, data);
    }

    public static RestResult createFailedResult(){
        Header header = new Header();
        header.setCode(ErrorCodeEnum.FAILED.getCode());
        header.setMsg(ErrorCodeEnum.FAILED.getMsg());
        return new RestResult(header);
    }

    public static RestResult createFailedResult(Object data){
        Header header = new Header();
        header.setCode(ErrorCodeEnum.FAILED.getCode());
        header.setMsg(ErrorCodeEnum.FAILED.getMsg());
        return new RestResult(header, data);
    }


    public static RestResult createFailedResult(Exception e){
        Header header = new Header();
        header.setCode(ErrorCodeEnum.FAILED.getCode());
        if (e instanceof SystemException){
            header.setCode(((SystemException) e).getErrcCode());

        }
        header.setMsg("系统出现异常");

        if (e instanceof BusiException){
            header.setCode(((BusiException) e).getErrcCode());
            header.setMsg(e.getMessage());
        }


        return new RestResult(header);
    }

    /**
     * 根据 ErrorCodeEnum 的描述构造失败返回
     * @param err
     * @return
     */
    public static RestResult createFailedResult(ErrorCodeEnum err) {
        Header header = new Header();
        header.setCode(ErrorCodeEnum.FAILED.getCode());
        header.setMsg(err.getMsg());
        return new RestResult(header);
    }


}
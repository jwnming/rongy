package com.cd.exception;

import com.cd.enums.ErrorCodeEnum;
import com.cd.enums.SystemEnum;

/**
 * @author wanggq
 * @date ${date}${time}
 * @description
 */
public class BusiException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1559470579366550826L;
	private String errcCode;
    private String errMsg;

    public BusiException(SystemEnum systemEnum) {
		this(systemEnum.getCode(), systemEnum.getMsg());
	}
    
    public BusiException(String errcCode, String errMsg){
        super(errMsg);
        this.setErrcCode(errcCode);
        this.setErrMsg(errMsg);
    }

    public BusiException(String errcCode, String errMsg, Throwable cause){
        super(errcCode + "[" + errMsg + "]", cause);
        this.setErrcCode(errcCode);
        this.setErrMsg(errMsg);
    }

	public BusiException(ErrorCodeEnum errorCodeEnum) {
		this(errorCodeEnum.getCode(), errorCodeEnum.getMsg());
	}

	public String getErrcCode() {
		return errcCode;
	}

	public void setErrcCode(String errcCode) {
		this.errcCode = errcCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
    
}

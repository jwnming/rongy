package com.cd.exception;

import com.cd.enums.SystemEnum;

/**
 * @author wanggq
 * @date ${date}${time}
 * @description
 */
public class SystemException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6926007365712006269L;
	private String errcCode;
    private String errMsg;

    public SystemException(String errcCode, String errMsg){
        super(errcCode + "[" + errMsg + "]");
        this.setErrcCode(errcCode);
        this.setErrMsg(errMsg);
    }

    public SystemException(String errcCode, String errMsg, Throwable cause){
        super(errcCode + "[" + errMsg + "]", cause);
        this.setErrcCode(errcCode);
        this.setErrMsg(errMsg);
    }

	public SystemException(SystemEnum systemEnum) {
		this(systemEnum.code, systemEnum.msg);
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

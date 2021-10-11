package com.cd.enums;

public enum SystemEnum {
	OPERATE_DATABASE_ERROR001("product001", "数据库异常，操作失败"),
	ACCOUNT_NOT_EXIST("product002", "基金公司账号不存在");
	
	private SystemEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public String code;
	public String msg;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}

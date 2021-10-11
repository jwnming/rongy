package com.cd.common;


/**  
* <p>Title: RestResult</p>  
* <p>Description: 统一rest返回结果</p>  
* @author wanggq  
* @date 2019年6月26日  
*/  
public class RestResult {
    private Header header;
    private Object data;

    public RestResult(Header header, Object data){
        this.header = header;
        this.data = data;
    }
    public RestResult(Header header){
        this.header = header;
    }
    public RestResult(){
    }
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
    
}

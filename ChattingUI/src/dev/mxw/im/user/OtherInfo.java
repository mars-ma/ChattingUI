package dev.mxw.im.user;

import java.io.Serializable;

public class OtherInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 对方姓名，头像地址，聊天Id
	 */
	private String otherName, otherHeadURL;
	private int otherChatIdNo;
	
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
	public String getOtherHeadURL() {
		return otherHeadURL;
	}
	public void setOtherHeadURL(String otherHeadURL) {
		this.otherHeadURL = otherHeadURL;
	}
	public int getOtherChatIdNo() {
		return otherChatIdNo;
	}
	public void setOtherChatIdNo(int otherChatIdNo) {
		this.otherChatIdNo = otherChatIdNo;
	}

}

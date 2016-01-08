package dev.mxw.im.user;

import java.io.Serializable;

public class MyInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 对方姓名，头像地址，聊天Id
	 */
	private String myName, myHeadURL;
	private int myChatIdNo;
	public String getMyName() {
		return myName;
	}
	public void setMyName(String myName) {
		this.myName = myName;
	}
	public String getMyHeadURL() {
		return myHeadURL;
	}
	public void setMyHeadURL(String myHeadURL) {
		this.myHeadURL = myHeadURL;
	}
	public int getMyChatIdNo() {
		return myChatIdNo;
	}
	public void setMyChatIdNo(int myChatIdNo) {
		this.myChatIdNo = myChatIdNo;
	}
	

}

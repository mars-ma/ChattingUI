package dev.mxw.im.event;

public class NewMSG {
	
	public int dwFromUserid,dwToUserid;
	public String message;
	
	public NewMSG(){
		
	}

	public NewMSG(int dwFromUserid, int dwToUserid, String message) {
		this.dwFromUserid = dwFromUserid;
		this.dwToUserid = dwToUserid;
		this.message = message;
	}

}

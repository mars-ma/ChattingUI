package dev.mxw.im.event;

public class UpdateProgressImageView {
	
	public int progress;
	public int msgId;
	public int status;
	
	public UpdateProgressImageView(int t,int p,int status) {
		msgId = t;
		progress = p;
		this.status = status;
	}
}

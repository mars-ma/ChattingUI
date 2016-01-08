package dev.mxw.im.event;

public class SendWebURL {
	public String title,content,imgURL,targetURL;
	public SendWebURL( String title,String content,String imgURL,String targetURL) {
		this.content = content;
		this.targetURL = targetURL;
		this.title = title;
		this.imgURL = imgURL;
	}
}

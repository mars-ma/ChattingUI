package dev.mxw.im.event;

public class OnAnyChatUserAtRoomMessage {
	public int dwUserId;
	public boolean bEnter;
	
	public OnAnyChatUserAtRoomMessage(int id,boolean enter) {
		dwUserId = id;
		bEnter = enter;
	}
}

package dev.mxw.im.communication.listener;

public interface OnReceiveTextListener {
	void onReceiveText(int dwFromUserid, int dwToUserid, String message);
}

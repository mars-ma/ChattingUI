package dev.mxw.im.communication.listener;

public interface OnReceiveFileListener {
	void onReceiveFile(int dwFromUserid, int dwToUserid, String fileName,String filePath,int fileType);
}

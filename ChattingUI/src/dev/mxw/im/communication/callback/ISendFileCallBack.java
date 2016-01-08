package dev.mxw.im.communication.callback;

public interface ISendFileCallBack extends ISendCallBack{
	void onUpdateProgress(int progress);
}

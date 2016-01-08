package dev.mxw.im.communication;

import android.content.Context;
import dev.mxw.im.communication.listener.OnReceiveFileListener;
import dev.mxw.im.communication.listener.OnReceiveTextListener;

public class CommunicaionReceiver {
	
	private static CommunicaionReceiver mCommunicaionReceiver;
	
	public static CommunicaionReceiver getDefault(){
		if(mCommunicaionReceiver==null){
			mCommunicaionReceiver = new CommunicaionReceiver();
		}
		return mCommunicaionReceiver;
	}
	
	CommunicaionReceiver(){
	}
	
	private OnReceiveTextListener mOnReceiveTextListener;
	private OnReceiveFileListener mOnReceiveFileListener;
	
	public void setOnReceiveFileListener(OnReceiveFileListener onReceiveFileListener){
		mOnReceiveFileListener = onReceiveFileListener;
	}
	
	public void setOnReceiveTextListener(OnReceiveTextListener onReceiveTextListener){
		mOnReceiveTextListener = onReceiveTextListener;
	}
	
	public void onReceiveText(int from,int to,String message){
		if(mOnReceiveTextListener!=null){
			mOnReceiveTextListener.onReceiveText(from, to, message);
		}
	}
	
	public void onReceiveFile(int dwFromUserid, int dwToUserid, String fileName,String filePath,int fileType){
		if(mOnReceiveFileListener!=null){
			mOnReceiveFileListener.onReceiveFile(dwFromUserid, dwToUserid, fileName, filePath, fileType);
		}
	}
}

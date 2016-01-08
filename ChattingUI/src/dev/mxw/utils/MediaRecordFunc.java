package dev.mxw.utils;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

public class MediaRecordFunc {  
    private boolean isRecord = false;
    private String fileName;
    private MediaRecorder mMediaRecorder;
    private MediaRecordFunc(){
    }
     
    private static MediaRecordFunc mInstance;
    public synchronized static MediaRecordFunc getInstance(){
        if(mInstance == null)
            mInstance = new MediaRecordFunc();
        return mInstance;
    }
     
    public int startRecordAndFile(){
        //判断是否有外部存储设备sdcard
        if(isExistExternalStore())
        {
            if(isRecord)
            {
                return ErrorCode.E_STATE_RECODING;
            }
            else
            {
                if(mMediaRecorder == null)
                    createMediaRecord();
                try{
                    mMediaRecorder.prepare();
                    mMediaRecorder.start();
                    // 让录制状态为true  
                    isRecord = true;
                    return ErrorCode.SUCCESS;
                }catch(IOException ex){
                    ex.printStackTrace();
                    return ErrorCode.E_UNKOWN;
                }
            }
             
        }       
        else
        {
            return ErrorCode.E_NOSDCARD;            
        }       
    }
     
     
    public void stopRecordAndFile(){
         close();
    }
     
    public long getRecordFileSize(){
        return AudioFileFunc.getFileSize(AudioFileFunc.getAMRFilePath(fileName));
    }
     
    public String getFilePath(){
    	return AudioFileFunc.getAMRFilePath(fileName);
    }
     
    private void createMediaRecord(){
         /* ①Initial：实例化MediaRecorder对象 */
        mMediaRecorder = new MediaRecorder();
         
        /* setAudioSource/setVedioSource*/
        mMediaRecorder.setAudioSource(AudioFileFunc.AUDIO_INPUT);//设置麦克风
         
        /* 设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default
         * THREE_GPP(3gp格式，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
         */
         mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
          
         /* 设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default */
         mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
          
         fileName = MD5.encodeByMD5(String.valueOf(System.currentTimeMillis()))+".amr";
         /* 设置输出文件的路径 */
         File file = new File(AudioFileFunc.getAMRFilePath(fileName));
         if (file.exists()) {  
             file.delete();  
         }else if(!file.getParentFile().exists()){
        	 file.getParentFile().mkdirs();
         }
         mMediaRecorder.setOutputFile(file.getAbsolutePath());
    }
     
     
    private void close(){
        if (mMediaRecorder != null) {
        	try{
            isRecord = false;
            mMediaRecorder.stop();  
            mMediaRecorder.release();  
            mMediaRecorder = null;
        	}catch(IllegalStateException ex){
        		ex.printStackTrace();
        	}
        }  
    }
    
    /**
	 * 是否有外存卡
	 * 
	 * @return
	 */
	public static boolean isExistExternalStore() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
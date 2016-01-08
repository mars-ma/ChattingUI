package dev.mxw.im.data.db;

public class IMDBField {
	private static final String IM = "im";
	public static final String DB_NAME = IM+".db";
	
	private static final String MESSAGE = "MESSAGE";
	public static final String IM_MESSAGE_TABLE = "IM_MESSAGE_TABLE";
	public static final String MESSAGE_OWNER = "MESSAGE_OWNER";
	public static final String MESSAGE_ID = "MESSAGE_ID";
	public static final String MESSAGE_STATUS = "MESSAGE_STATUS"; //0:失败 1:成功
	public static final String MESSAGE_TYPE = "MESSAGE_TYPE"; //0:文本 1:语音 2:图片
	public static final String MESSAGE_TIME = "MESSAGE_TIME";
	public static final String MESSAGE_FROM = "MESSAGE_FROM";
	public static final String MESSAGE_TO = "MESSAGE_TO";
	
	public static final String FILE_PATH = "FILE_PATH";
	public static final String OTHER = "OTHER";
	public static final String CUSTOM = "CUSTOM";
	public static final String UN_READ = "UN_READ";
	
	public static final String CONTENT = "CONTENT";
	public static final String OWNER = "OWNER";
	
}

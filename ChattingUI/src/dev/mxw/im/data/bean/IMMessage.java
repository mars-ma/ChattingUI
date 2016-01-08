package dev.mxw.im.data.bean;

public class IMMessage {
	private int id, status, type, unread;

	public int getUnread() {
		return unread;
	}

	/**
	 * 
	 * @param unread 1:未读 
	 */
	public void setUnread(int unread) {
		this.unread = unread;
	}

	private long time;
	private int from, to, owner, other;
	private String content, filePath, custom;

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 *            //0:失败 1:成功 2:进行时
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 
	 * @return 0:文本 1:图片 2:语音
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            0:文本 1:图片 2:语音
	 */
	public void setType(int type) {
		this.type = type;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public int getOther() {
		return other;
	}

	public void setOther(int other) {
		this.other = other;
	}
}

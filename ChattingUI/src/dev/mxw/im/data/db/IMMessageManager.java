package dev.mxw.im.data.db;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dev.mxw.im.data.bean.IMMessage;

public class IMMessageManager {
	private SQLiteDatabase db;
	private Context mContext;

	private static IMMessageManager instance;

	public static IMMessageManager getInstance(Context context) {
		if (instance == null) {
			instance = new IMMessageManager(context);
		}
		return instance;
	}

	public IMMessageManager(Context context) {
		this.mContext = context;
		IMDBHelper mDBHelper = new IMDBHelper(mContext);
		db = mDBHelper.getWritableDatabase();
	}

	private Cursor getCursor(String sql) {
		return db.rawQuery(sql, null);
	}

	public Cursor getCursor(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}

	public void closeDB() {
		db.close();
		db = null;
	}

	public void beginTransaction() {
		db.beginTransaction();
	}

	public void endTransaction() {
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	public int queryUnReadCount(int owner,int other){
		Cursor c = getCursor("select COUNT(*) AS TOTALCOUNT from IM_MESSAGE_TABLE where MESSAGE_OWNER = '"+owner+"' and OTHER = '"+other+"' and UN_READ = 1");
		if (c.moveToNext()) {
			int out = Integer.parseInt(c.getString(c.getColumnIndex("TOTALCOUNT")));
			c.close();
			return out;
		} else {
			c.close();
			return -1;
		}
	}

	public ArrayList<IMMessage> queryLastMSGSet(int owner) {
		ArrayList<IMMessage> msgs = new ArrayList<>();
		String sql = "select * from "+IMDBField.IM_MESSAGE_TABLE+" where "+IMDBField.MESSAGE_OWNER+" = '"+owner+"' group by "+IMDBField.OTHER+" order by "+IMDBField.MESSAGE_TIME+" desc";
		Cursor c = getCursor(sql);
		while (c.moveToNext()) {
			IMMessage msg = new IMMessage();
			msg.setId(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_ID)));
			msg.setContent(c.getString(c.getColumnIndex(IMDBField.CONTENT)));
			msg.setFilePath(c.getString(c.getColumnIndex(IMDBField.FILE_PATH)));
			msg.setFrom(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_FROM)));
			msg.setTo(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_TO)));
			msg.setOther(c.getInt(c.getColumnIndex(IMDBField.OTHER)));
			msg.setOwner(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_OWNER)));
			msg.setStatus(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_STATUS)));
			msg.setType(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_TYPE)));
			msg.setTime(c.getLong(c.getColumnIndex(IMDBField.MESSAGE_TIME)));
			msg.setCustom(c.getString(c.getColumnIndex(IMDBField.CUSTOM)));
			msgs.add(msg);
		}
		c.close();
		return msgs;
	}

	public int insert(IMMessage msg) {
		ContentValues cv = new ContentValues();
		cv.put(IMDBField.MESSAGE_TYPE, msg.getType());
		cv.put(IMDBField.MESSAGE_STATUS, msg.getStatus());
		cv.put(IMDBField.MESSAGE_TIME, msg.getTime());
		cv.put(IMDBField.MESSAGE_FROM, msg.getFrom());
		cv.put(IMDBField.MESSAGE_TO, msg.getTo());
		cv.put(IMDBField.MESSAGE_OWNER, msg.getOwner());
		cv.put(IMDBField.CONTENT, msg.getContent());
		cv.put(IMDBField.FILE_PATH, msg.getFilePath());
		cv.put(IMDBField.OTHER, msg.getOther());
		cv.put(IMDBField.CUSTOM, msg.getCustom());
		cv.put(IMDBField.UN_READ, msg.getUnread());
		int returnID = -1;

		try {
			db.insert(IMDBField.IM_MESSAGE_TABLE, null, cv);
			Cursor c = getCursor("SELECT " + IMDBField.MESSAGE_ID + " FROM " + IMDBField.IM_MESSAGE_TABLE + " WHERE "
					+ IMDBField.MESSAGE_TIME + " = " + msg.getTime());
			if (c.moveToNext()) {
				returnID = c.getInt(c.getColumnIndex(IMDBField.MESSAGE_ID));
			} else {
				returnID = -1;
			}
			c.close();
		} catch (Exception ex) {
			returnID = -1;
		}
		return returnID;
	}

	public void update(IMMessage msg) {
		ContentValues cv = new ContentValues();
		cv.put(IMDBField.MESSAGE_TYPE, msg.getType());
		cv.put(IMDBField.MESSAGE_STATUS, msg.getStatus());
		cv.put(IMDBField.MESSAGE_TIME, msg.getTime());
		cv.put(IMDBField.MESSAGE_FROM, msg.getFrom());
		cv.put(IMDBField.MESSAGE_TO, msg.getTo());
		cv.put(IMDBField.MESSAGE_OWNER, msg.getOwner());
		cv.put(IMDBField.CONTENT, msg.getContent());
		cv.put(IMDBField.FILE_PATH, msg.getFilePath());
		cv.put(IMDBField.OTHER, msg.getOther());
		cv.put(IMDBField.CUSTOM, msg.getCustom());
		cv.put(IMDBField.UN_READ, msg.getUnread());
		String whereClause = IMDBField.MESSAGE_ID + "=?";
		String[] whereArgs = new String[] { String.valueOf(msg.getId()) };
		db.update(IMDBField.IM_MESSAGE_TABLE, cv, whereClause, whereArgs);
	}

	public void delete(IMMessage msg) {
		String whereClause = IMDBField.MESSAGE_ID + "=?";
		String[] whereArgs = new String[] { String.valueOf(msg.getId()) };
		db.delete(IMDBField.IM_MESSAGE_TABLE, whereClause, whereArgs);
	}

	public int getTotalCount(int owner, int other) {
		Cursor c = getCursor("SELECT COUNT(*) AS TOTALCOUNT FROM " + IMDBField.IM_MESSAGE_TABLE + " WHERE "
				+ IMDBField.MESSAGE_OWNER + " = '" + owner + "' AND " + IMDBField.OTHER + " = '" + other + "'");
		if (c.moveToNext()) {
			int out = Integer.parseInt(c.getString(c.getColumnIndex("TOTALCOUNT")));
			c.close();
			return out;
		} else {
			c.close();
			return -1;
		}
	}

	public IMMessage query(int id) {
		Cursor c = getCursor(
				"SELECT * FROM " + IMDBField.IM_MESSAGE_TABLE + " where " + IMDBField.MESSAGE_ID + " = " + id);
		IMMessage msg = null;
		if (c.moveToNext()) {
			msg = new IMMessage();
			msg.setId(id);
			msg.setContent(c.getString(c.getColumnIndex(IMDBField.CONTENT)));
			msg.setFilePath(c.getString(c.getColumnIndex(IMDBField.FILE_PATH)));
			msg.setFrom(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_FROM)));
			msg.setTo(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_TO)));
			msg.setOther(c.getInt(c.getColumnIndex(IMDBField.OTHER)));
			msg.setOwner(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_OWNER)));
			msg.setStatus(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_STATUS)));
			msg.setType(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_TYPE)));
			msg.setTime(c.getLong(c.getColumnIndex(IMDBField.MESSAGE_TIME)));
			msg.setCustom(c.getString(c.getColumnIndex(IMDBField.CUSTOM)));
		}
		c.close();
		return msg;
	}
	
	public IMMessage queryLast(int owner,int other) {
		Cursor c = getCursor(
				"SELECT * FROM " + IMDBField.IM_MESSAGE_TABLE + " where " + IMDBField.MESSAGE_OWNER + " = " + "'"+owner+"' and "+IMDBField.OTHER+" = "+"'"+other+"' order by "+IMDBField.MESSAGE_TIME+" desc");
		IMMessage msg = null;
		if (c.moveToNext()) {
			msg = new IMMessage();
			msg.setId(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_ID)));
			msg.setContent(c.getString(c.getColumnIndex(IMDBField.CONTENT)));
			msg.setFilePath(c.getString(c.getColumnIndex(IMDBField.FILE_PATH)));
			msg.setFrom(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_FROM)));
			msg.setTo(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_TO)));
			msg.setOther(c.getInt(c.getColumnIndex(IMDBField.OTHER)));
			msg.setOwner(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_OWNER)));
			msg.setStatus(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_STATUS)));
			msg.setType(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_TYPE)));
			msg.setTime(c.getLong(c.getColumnIndex(IMDBField.MESSAGE_TIME)));
			msg.setCustom(c.getString(c.getColumnIndex(IMDBField.CUSTOM)));
		}
		c.close();
		return msg;
	}

	public ArrayList<IMMessage> query(int owner, int other, int pageNo, int pageCount) {
		ArrayList<IMMessage> msgs = new ArrayList<>();
		String sql = "SELECT * FROM " + IMDBField.IM_MESSAGE_TABLE + " WHERE " + IMDBField.MESSAGE_OWNER + " = '"
				+ owner + "' AND " + IMDBField.OTHER + " = '" + other + "' order by " + IMDBField.MESSAGE_TIME
				+ " desc limit " + pageCount * pageNo;
		Cursor c = getCursor(sql);
		while (c.moveToNext()) {
			IMMessage msg = new IMMessage();
			msg.setId(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_ID)));
			msg.setContent(c.getString(c.getColumnIndex(IMDBField.CONTENT)));
			msg.setFilePath(c.getString(c.getColumnIndex(IMDBField.FILE_PATH)));
			msg.setFrom(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_FROM)));
			msg.setTo(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_TO)));
			msg.setOther(c.getInt(c.getColumnIndex(IMDBField.OTHER)));
			msg.setOwner(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_OWNER)));
			msg.setStatus(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_STATUS)));
			msg.setType(c.getInt(c.getColumnIndex(IMDBField.MESSAGE_TYPE)));
			msg.setTime(c.getLong(c.getColumnIndex(IMDBField.MESSAGE_TIME)));
			msg.setCustom(c.getString(c.getColumnIndex(IMDBField.CUSTOM)));
			msgs.add(msg);
		}
		c.close();
		return msgs;
	}

	public void deleteTable(String tableName) {
		db.delete(tableName, null, null);
	}

	public void close() {
		closeDB();
		instance = null;
	}

	public void updateUnRead(int owner, int other) {
		String sql = "update IM_MESSAGE_TABLE SET UN_READ = 0 where MESSAGE_OWNER = '"+owner+"' and OTHER = '"+other+"' and UN_READ = 1";
		db.execSQL(sql);
	}

	public void clear() {
		String sql = "delete from "+IMDBField.IM_MESSAGE_TABLE;
		db.execSQL(sql);
	}
}
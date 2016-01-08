package dev.mxw.im.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class IMDBHelper extends SQLiteOpenHelper {
	private Context mContext;
	private static final int DB_VERSION = 1;

	public IMDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public IMDBHelper(Context context) {
		this(context, IMDBField.DB_NAME, null, DB_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS IM_MESSAGE_TABLE " + "(MESSAGE_ID INTEGER PRIMARY KEY, "
				+ IMDBField.MESSAGE_STATUS + " NUMERIC, " + IMDBField.MESSAGE_TYPE + " NUMERIC, "
				+ IMDBField.MESSAGE_TIME + " NUMERIC, " + IMDBField.MESSAGE_OWNER + " NUMERIC, " + IMDBField.OTHER
				+ " NUMERIC, " + IMDBField.MESSAGE_FROM + " NUMERIC, " + IMDBField.MESSAGE_TO + " NUMERIC, " + IMDBField.CONTENT
				+ " TEXT, " + IMDBField.CUSTOM + " TEXT, " + IMDBField.UN_READ + " NUMERIC, " + IMDBField.FILE_PATH
				+ " TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}

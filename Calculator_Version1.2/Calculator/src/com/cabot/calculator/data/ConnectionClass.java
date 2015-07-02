package com.cabot.calculator.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConnectionClass extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_NAME="Exp_HistoryDB";
	public static final String TABLE_EXPS="Exp_History";
	public  static final String EXP_TAB_COL_ID="EXP_id";
	public static final String EXP_TAB_COL_VALUE="EXP_String";
	public static final String EXP_TAB_COL_DATE="Date";
	
	
	public ConnectionClass(Context context)
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_EXP_HISTORY_TABLE="CREATE TABLE "+TABLE_EXPS + "("
				+ EXP_TAB_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + EXP_TAB_COL_VALUE + " TEXT," + EXP_TAB_COL_DATE + " INTEGER );"; 
		db.execSQL(CREATE_EXP_HISTORY_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPS);
	}
	

}

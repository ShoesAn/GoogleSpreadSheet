package terry.example.spread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class Create_db extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Mytable.db";
	static final String TABLE_name = "mytable";
	private static final String[] COLUMNS = {Table_name.COLUMN_NAME_NUMBER,Table_name.COLUMN_NAME_DATE_ADDED,Table_name.COLUMN_NAME_REMARK,Table_name.COLUMN_NAME_SPREADSHEET_FEED_URL,Table_name.COLUMN_NAME_STATE};
	public Create_db(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO 
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 
		db.execSQL(Table_name.SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 
		db.execSQL(Table_name.SQL_DELETE_TABLE);
      onCreate(db);
		
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
	
	public void addColumn(Columns column){
        Log.d("addBook", column.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(Table_name.COLUMN_NAME_NUMBER, column.getNumber()); 
        values.put(Table_name.COLUMN_NAME_DATE_ADDED, column.getDATE()); 
        values.put(Table_name.COLUMN_NAME_REMARK, column.getRemark());
        values.put(Table_name.COLUMN_NAME_SPREADSHEET_FEED_URL, column.getUrl());
        values.put(Table_name.COLUMN_NAME_STATE, column.getState());
        values.put(Table_name.COLUMN_NAME_TYPE, column.getType());
        // 3. insert
        db.insert(TABLE_name, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close(); 
    }
	
	public Columns getColumn(int id){
		 
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
 
        // 2. build query
        Cursor cursor = 
                db.query(TABLE_name, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections 
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
 
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
 
        // 4. build column object
        Columns column = new Columns();
        column.setId(Integer.parseInt(cursor.getString(0)));
        column.setNumber(Double.parseDouble(cursor.getString(1)));
        column.setDATE(Long.parseLong(cursor.getString(2)));
        column.setRemark(cursor.getString(3));
        column.setUrl(cursor.getString(4));
        column.setState(cursor.getString(5));
        column.setType(cursor.getString(6));
        Log.d("getColumn("+id+")", column.toString());
 
        // 5. return book
        return column;
    }
	
	public ArrayList<Columns> getAllColumns() {
        ArrayList<Columns> info = new ArrayList<Columns>();
 
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_name;
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        Columns column = null;
        if (cursor.moveToFirst()) {
            do {
            	column = new Columns();
            	column.setId(Integer.parseInt(cursor.getString(0)));
            	column.setNumber(Double.parseDouble(cursor.getString(1)));
               column.setDATE(Long.parseLong(cursor.getString(2)));
               column.setRemark(cursor.getString(3));
               column.setUrl(cursor.getString(4));
               column.setState(cursor.getString(5));
               column.setType(cursor.getString(6));
               
                // Add column to books
               info.add(column);
            } while (cursor.moveToNext());
        }
 
        //Log.d("getAllBooks()", info.toString());
 
        // return columns
        return info;
    }

	public int updateColumn(Columns column) {
		 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(Table_name.COLUMN_NAME_NUMBER, column.getNumber()); // get title 
        values.put(Table_name.COLUMN_NAME_DATE_ADDED, column.getDATE()); // get author
        values.put(Table_name.COLUMN_NAME_REMARK, column.getRemark());
        values.put(Table_name.COLUMN_NAME_SPREADSHEET_FEED_URL, column.getUrl());
        values.put(Table_name.COLUMN_NAME_STATE, column.getState());
        values.put(Table_name.COLUMN_NAME_TYPE, column.getType());
        // 3. updating row
        int i = db.update(TABLE_name, //table
                values, // column/value
                "id" +" = ?", // selections
                new String[] { String.valueOf(column.getId()) }); //selection args
 
        // 4. close
        db.close();
        return i;
    }
	
	public void deleteColumn(Columns column) {
		 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. delete
        db.delete(TABLE_name,
        		    "id"+" = ?",
                new String[] { String.valueOf(column.getId()) });
        // 3. close
        db.close();
        Log.d("deleteBook", column.toString());
 
    }
	
	public final class Table_name implements BaseColumns{
		private Table_name() {};
		
		private static final String TEXT_TYPE = " TEXT";
		private static final String REAL_TYPE = " REAL";
		private static final String INTEGER_TYPE = " INTEGER";
		private static final String COMMA_SEP = ",";
    	public static final String COLUMN_NAME_NUMBER = "number";
    	public static final String COLUMN_NAME_REMARK = "remark";
    	public static final String COLUMN_NAME_DATE_ADDED = "date_added";
    	public static final String COLUMN_NAME_SPREADSHEET_FEED_URL = "spreadsheet_feed_url";
    	public static final String COLUMN_NAME_STATE = "state";
    	public static final String COLUMN_NAME_TYPE = "type";
    	public static final String SQL_CREATE_TABLE = 
    			"CREATE TABLE " + TABLE_name + " (" +
    				    " id INTEGER PRIMARY KEY," +
    				    COLUMN_NAME_NUMBER + REAL_TYPE + COMMA_SEP +
    				    COLUMN_NAME_DATE_ADDED + INTEGER_TYPE + COMMA_SEP +
    				    COLUMN_NAME_REMARK + TEXT_TYPE + COMMA_SEP +
    				    COLUMN_NAME_SPREADSHEET_FEED_URL + TEXT_TYPE + COMMA_SEP +
    				    COLUMN_NAME_STATE + TEXT_TYPE + COMMA_SEP +
    				    COLUMN_NAME_TYPE + TEXT_TYPE +
    				     ");";
    	static final String SQL_DELETE_TABLE =
    			"DROP TABLE IF EXISTS " + TABLE_name;
	}
	
}

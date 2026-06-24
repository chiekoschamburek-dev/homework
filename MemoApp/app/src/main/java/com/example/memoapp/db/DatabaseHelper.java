package com.example.memoapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.memoapp.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库帮助类 — 管理 SQLite 数据库的创建与增删改查
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "memo.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_RECORDS = "records";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_CONTENT = "content";
    private static final String COL_DATE = "date";
    private static final String COL_TIME = "time";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_RECORDS + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TITLE + " TEXT NOT NULL, " +
                    COL_CONTENT + " TEXT, " +
                    COL_DATE + " TEXT, " +
                    COL_TIME + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        onCreate(db);
    }

    /**
     * 插入一条新记录
     */
    public long insertRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, record.getTitle());
        values.put(COL_CONTENT, record.getContent());
        values.put(COL_DATE, record.getDate());
        values.put(COL_TIME, record.getTime());
        long id = db.insert(TABLE_RECORDS, null, values);
        db.close();
        return id;
    }

    /**
     * 更新一条记录
     */
    public int updateRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, record.getTitle());
        values.put(COL_CONTENT, record.getContent());
        values.put(COL_DATE, record.getDate());
        values.put(COL_TIME, record.getTime());
        int rows = db.update(TABLE_RECORDS, values, COL_ID + " = ?",
                new String[]{String.valueOf(record.getId())});
        db.close();
        return rows;
    }

    /**
     * 删除一条记录
     */
    public void deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORDS, COL_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * 查询所有记录，按 ID 倒序排列
     */
    public List<Record> getAllRecords() {
        List<Record> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECORDS, null, null, null, null, null,
                COL_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Record record = new Record(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME))
                );
                records.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return records;
    }

    /**
     * 根据 ID 查询一条记录
     */
    public Record getRecordById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECORDS, null, COL_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Record record = null;
        if (cursor.moveToFirst()) {
            record = new Record(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME))
            );
        }
        cursor.close();
        db.close();
        return record;
    }
}

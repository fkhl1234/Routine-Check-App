package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

// SQLiteOpenHelper를 상속받는 MyDatabaseHelper 클래스 생성
public class DatabaseHelper extends SQLiteOpenHelper {

    // db 설정
    private static final String dbName = "Routine.db";  // db 이름
    private static final String tableName = "routines"; // table 이름
    private static final int dbVersion = 1;  // db 버전

    // db 생성 쿼리
    private static final String TABLE_CREATE =
        "CREATE TABLE routines (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "date TEXT, " +
        "routine TEXT);";

    // 생성자
    public DatabaseHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    // 처음 db 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성 쿼리 실행
        db.execSQL(TABLE_CREATE);
    }

    // db 업데이트
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블을 삭제하고 새로 생성
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}

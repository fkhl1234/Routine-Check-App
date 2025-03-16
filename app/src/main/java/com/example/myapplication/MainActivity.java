package com.example.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbhelper; // db 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 안드로이드 액티비티 기본 설정
        EdgeToEdge.enable(this); // edge to edge 모드

        setContentView(R.layout.activity_main); // view 설정

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // 시스템 바 영역의 크기 가져오기
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // 시스템 바만큼 패딩을 설정
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        }); // inset, 여백 설정

        // db 설정

        dbhelper = new DatabaseHelper(this); // db 인스턴스 생성
        SQLiteDatabase db = dbhelper.getWritableDatabase(); // db 테이블 가져오기
        if (db == null) {
            Toast.makeText(this, "Database not opened", Toast.LENGTH_SHORT).show();
        } // db 체크

        ContentValues routine = new ContentValues();

        db.insert("routines", null, routine);

        List<String> array = new ArrayList<String>();
        array.add("routine");
        listChange(array);

        long rowId = db.insert("routines", null, routine); // 테스트 데이터 삽입

        // 캘린더 설정

        CalendarView cal = findViewById(R.id.calendarView); // 캘린더 객체

        cal.setOnDateChangeListener((view, year, month, day) -> {
            String selectedDate = String.valueOf(year).substring(2) + ((month + 1 > 9) ? (month + 1) : "0" + (month + 1)) + String.valueOf(day); // 선택한 날짜
            Toast.makeText(this, selectedDate, Toast.LENGTH_SHORT).show(); // 날짜 띄우기

            // db 조회
            String[] columns = {"id", "routine", "date"}; // 조회할 컬럼
            String[] args = {selectedDate}; // 조회할 데이터
            Cursor cursor = db.query("routines", columns, "date = ?", args, null, null, null); // 조회

            // 조회된 데이터가 있는지 확인
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // 각 컬럼의 값을 가져오기
                    String routineData = cursor.getString(cursor.getColumnIndex("routine"));
                    String dateData = cursor.getString(cursor.getColumnIndex("date"));

                    array.add(routineData);

                } while (cursor.moveToNext()); // 다음 행으로 이동

                cursor.close(); // 커서 닫기
            }

            listChange(array); // List View 반영

        }); // 날짜 선택 리스너
    }

    private void listChange(List<String> array) {
        // ListView 설정

        ListView listView = findViewById(R.id.routineList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        // 어댑터 설정

        listView.setAdapter(adapter); // 어댑터 연결
    }
}
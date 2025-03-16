package com.example.myapplication;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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

        Toast.makeText(this, "DB 연결", Toast.LENGTH_SHORT).show();

        // 캘린더 설정

        CalendarView cal = findViewById(R.id.calendarView); // 캘린더 객체

        cal.setOnDateChangeListener((view, year, month, day) -> {
            String selectedDate = year + "." + ((month>9)?month:"0"+month) + "." + day; // 선택된 날짜 문자열 변환
            Toast.makeText(this, selectedDate, Toast.LENGTH_SHORT).show(); // 날짜 띄우기

            TextView routineText = findViewById(R.id.routinetext);
            routineText.setText(selectedDate);
        }); // 날짜 선택 리스너
    }


}
package com.example.myapplication;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import com.example.myapplication.Item;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbhelper; // db 객체
    String selectedDate;

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


        // 캘린더 설정

        CalendarView cal = findViewById(R.id.calendarView); // 캘린더 객체

        cal.setOnDateChangeListener((view, year, month, day) -> {
            selectedDate = String.valueOf(year).substring(2) + ((month + 1 > 9) ? (month + 1) : "0" + (month + 1)) + String.valueOf(day); // 선택한 날짜
            Toast.makeText(this, selectedDate, Toast.LENGTH_SHORT).show(); // 날짜 띄우기

            listRefresh(db);
        }); // 날짜 선택 리스너


        // Add 버튼 설정

        Button addButton = findViewById(R.id.addButton); // 버튼 연결

        addButton.setOnClickListener(v -> showInputDialog(new InputListener() {
            @Override
            public void onInputReceived(String userInput) {
                listRefresh(db, userInput, selectedDate);
            }
        })); // 버튼 클릭시 리프레쉬까지


        // Delete 버튼 설정

        Button delButton = findViewById(R.id.deleteButton);

        delButton.setOnClickListener(view -> {
            ListView listView = findViewById(R.id.routineList);

            CustomAdapter adapter = (CustomAdapter) listView.getAdapter();

            // db 조회
            String[] columns = {"id", "routine", "date"};
            String[] args = {selectedDate};
            Cursor cursor = db.query("routines", columns, "date = ?", args, null, null, null);

            // 조회된 데이터가 있는지 확인
            if (cursor != null && cursor.moveToFirst()) {
                ArrayList<Item> array = new ArrayList<>(); // 데이터를 담을 리스트

                do {
                    String routineData = cursor.getString(cursor.getColumnIndex("routine"));
                    Item routineItem = new Item(routineData);
                    array.add(routineItem);
                } while (cursor.moveToNext()); // 다음 행으로 이동

                cursor.close(); // 커서 닫기

                ArrayList<String> delete = new ArrayList<>();
                for(Item temp: array) {
                    if(temp.getChecked()) {
                        delete.add(temp.getText());
                    }
                }
            }
        });
    }

    private void listChange(List<Item> array) {
        // ListView 설정

        ListView listView = findViewById(R.id.routineList);

        // 어댑터 설정
        CustomAdapter adapter = new CustomAdapter(this,array);
        listView.setAdapter(adapter); // 어댑터 연결
    }

    public interface InputListener {
        void onInputReceived(String userInput);
    } // 콜백함수 : Alert Dialog에서 문자열 받아옴

    private void showInputDialog(final InputListener listener) {
        // 다이얼로그에서 사용할 EditText 생성
        final EditText input = new EditText(this);

        // AlertDialog 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("입력창")
                .setMessage("문자열을 입력하세요.")
                .setView(input)  // EditText 추가
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = input.getText().toString();
                        // 입력값 반환
                        listener.onInputReceived(userInput);
                    }
                })
                .setNegativeButton("취소", null);

        // 다이얼로그 표시
        builder.show();
    }

    public void listRefresh(SQLiteDatabase db, String routine, String date){
        ContentValues value = new ContentValues();
        value.put("routine", routine);
        value.put("date", date); // 삽입할 데이터

        long result = db.insert("routines", null, value);
        // db에 데이터 삽입

        if (result != -1) {
            // db 조회
            String[] columns = {"id", "routine", "date"};
            String[] args = {selectedDate};
            Cursor cursor = db.query("routines", columns, "date = ?", args, null, null, null);

            // 조회된 데이터가 있는지 확인
            if (cursor != null && cursor.moveToFirst()) {
                ArrayList<Item> array = new ArrayList<>(); // 데이터를 담을 리스트

                do {
                    String routineData = cursor.getString(cursor.getColumnIndex("routine"));
                    Item routineItem = new Item(routineData);
                    array.add(routineItem);
                } while (cursor.moveToNext()); // 다음 행으로 이동

                cursor.close(); // 커서 닫기

                listChange(array); // ListView 갱신
            }
            else {
                ArrayList<Item> array = new ArrayList<>(); // 데이터를 담을 리스트
                Item routineItem = new Item("No Routine");
                array.add(routineItem);
                array.add(routineItem);

                listChange(array);
            }
        }
    }

    public void listRefresh(SQLiteDatabase db){
        // db 조회
        String[] columns = {"id", "routine", "date"};
        String[] args = {selectedDate};
        Cursor cursor = db.query("routines", columns, "date = ?", args, null, null, null);

        // 조회된 데이터가 있는지 확인
        if (cursor != null && cursor.moveToFirst()) {
            ArrayList<Item> array = new ArrayList<>(); // 데이터를 담을 리스트

            do {
                String routineData = cursor.getString(cursor.getColumnIndex("routine"));
                Item routineItem = new Item(routineData);
                array.add(routineItem);
            } while (cursor.moveToNext()); // 다음 행으로 이동

            cursor.close(); // 커서 닫기

            listChange(array); // ListView 갱신
        }
        else {
            ArrayList<Item> array = new ArrayList<>(); // 데이터를 담을 리스트
            Item routineItem = new Item("No routine");
            array.add(routineItem);

            listChange(array);
        }
    }
}


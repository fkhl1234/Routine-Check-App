package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Item> {
    private Context context;
    private List<Item> items;

    public CustomAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 재사용 가능한 뷰를 얻거나 새로 만든다
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        // 현재 항목 가져오기
        Item item = items.get(position);

        // 텍스트와 체크박스 설정
        TextView itemText = convertView.findViewById(R.id.routineText);
        itemText.setText(item.getText());

        CheckBox itemCheck = convertView.findViewById(R.id.routineCheck);
        itemCheck.setChecked(item.getChecked());

        // 체크박스 상태 변경 시, 해당 상태를 데이터 모델에 반영
        itemCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);  // 체크 상태 업데이트
        });

        return convertView;
    }
}

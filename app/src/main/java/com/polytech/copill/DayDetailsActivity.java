package com.polytech.copill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DayDetailsActivity extends AppCompatActivity {

    private int day,id;
    private TextView textView_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_details);
        Intent intent = getIntent();
        day = intent.getIntExtra("day",0);
        id = intent.getIntExtra("id",0);
        textView_day = (TextView)findViewById(R.id.textView_day);
        textView_day.setText(String.valueOf(day));
    }
}

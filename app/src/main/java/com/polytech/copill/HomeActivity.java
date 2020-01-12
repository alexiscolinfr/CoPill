package com.polytech.copill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private int id;
    private String firstName,lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");

        TextView textViewFirstName = (TextView) findViewById(R.id.textView_firstName);
        TextView textViewLastName = (TextView) findViewById(R.id.textView_lastName);
        TextView textViewId = (TextView) findViewById(R.id.textView_id);
        textViewId.setText("Id : " + id);
        textViewFirstName.setText("Prénom : " + firstName);
        textViewLastName.setText("Nom : " + lastName);
    }
}

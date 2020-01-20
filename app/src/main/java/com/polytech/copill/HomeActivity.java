package com.polytech.copill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private int id;
    private String firstName,lastName,dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        final Date date = new Date();
        dateString = new SimpleDateFormat("dd/MM/yyyy").format(date);

        TextView textViewWelcomeMessage = (TextView) findViewById(R.id.textView_welcomeMessage);
        TextView textViewTime = (TextView) findViewById(R.id.textView_time);
        textViewWelcomeMessage.setText("Bienvenue " + firstName + " " + lastName + " !");
        textViewTime.setText(dateString);
    }

    public void logout(View view){
        Intent intent1 = new Intent(this, ConnexionActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
    }

    public void addNewPill(View view){
        Intent intent2 = new Intent(this, AddPillActivity.class);
        intent2.putExtra("id", id);
        startActivity(intent2);
    }
}

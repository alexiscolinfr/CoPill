package com.polytech.copill;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private int id,sumPillsMonday,sumPillsTuesday,sumPillsWednesday,sumPillsThursday,sumPillsFriday,sumPillsSaturday,sumPillsSunday;
    private String firstName,lastName,dateString;
    TextView textViewWelcomeMsg,textViewTime,textViewPCMonday,textViewPCTuesday,textViewPCWednesday,textViewPCThursday,textViewPCFriday,textViewPCSaturday,textViewPCSunday;

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

        textViewWelcomeMsg = (TextView)findViewById(R.id.textView_welcomeMessage);
        textViewTime = (TextView)findViewById(R.id.textView_time);
        textViewWelcomeMsg.setText("Bienvenue " + firstName + " " + lastName + " !");
        textViewTime.setText(dateString);

        textViewPCMonday = (TextView)findViewById(R.id.textView_pillCountMonday);
        textViewPCTuesday = (TextView)findViewById(R.id.textView_pillCountTuesday);
        textViewPCWednesday = (TextView)findViewById(R.id.textView_pillCountWednesday);
        textViewPCThursday = (TextView)findViewById(R.id.textView_pillCountThursday);
        textViewPCFriday = (TextView)findViewById(R.id.textView_pillCountFriday);
        textViewPCSaturday = (TextView)findViewById(R.id.textView_pillCountSaturday);
        textViewPCSunday = (TextView)findViewById(R.id.textView_pillCountSunday);

        new DataBaseConnection().execute();
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

    private class DataBaseConnection extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                ConnectionManager connectionManager = new ConnectionManager();
                Connection con = connectionManager.createConnection();
                Statement stmt1 = con.createStatement();
                String query1 = "SELECT quantity FROM treatment WHERE id_user='"+id+"' AND days LIKE '%monday%'";
                final ResultSet rs1 = stmt1.executeQuery(query1);

                while(rs1.next()){
                    sumPillsMonday += rs1.getInt("quantity");
                }

                Statement stmt2 = con.createStatement();
                String query2 = "SELECT quantity FROM treatment WHERE id_user='"+id+"' AND days LIKE '%tuesday%'";
                final ResultSet rs2 = stmt2.executeQuery(query2);

                while(rs2.next()){
                    sumPillsTuesday += rs2.getInt("quantity");
                }

                Statement stmt3 = con.createStatement();
                String query3 = "SELECT quantity FROM treatment WHERE id_user='"+id+"' AND days LIKE '%wednesday%'";
                final ResultSet rs3 = stmt3.executeQuery(query3);

                while(rs3.next()){
                    sumPillsWednesday += rs3.getInt("quantity");
                }

                Statement stmt4 = con.createStatement();
                String query4 = "SELECT quantity FROM treatment WHERE id_user='"+id+"' AND days LIKE '%thursday%'";
                final ResultSet rs4 = stmt4.executeQuery(query4);

                while(rs4.next()){
                    sumPillsThursday += rs4.getInt("quantity");
                }

                Statement stmt5 = con.createStatement();
                String query5 = "SELECT quantity FROM treatment WHERE id_user='"+id+"' AND days LIKE '%friday%'";
                final ResultSet rs5 = stmt5.executeQuery(query5);

                while(rs5.next()){
                    sumPillsFriday += rs5.getInt("quantity");
                }

                Statement stmt6 = con.createStatement();
                String query6 = "SELECT quantity FROM treatment WHERE id_user='"+id+"' AND days LIKE '%saturday%'";
                final ResultSet rs6 = stmt6.executeQuery(query6);

                while(rs6.next()){
                    sumPillsSaturday += rs6.getInt("quantity");
                }

                Statement stmt7 = con.createStatement();
                String query7 = "SELECT quantity FROM treatment WHERE id_user='"+id+"' AND days LIKE '%sunday%'";
                final ResultSet rs7 = stmt7.executeQuery(query7);

                while(rs7.next()){
                    sumPillsSunday += rs7.getInt("quantity");
                }

                connectionManager.closeConnection();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            textViewPCMonday.setText(String.valueOf(sumPillsMonday));
            textViewPCTuesday.setText(String.valueOf(sumPillsTuesday));
            textViewPCWednesday.setText(String.valueOf(sumPillsWednesday));
            textViewPCThursday.setText(String.valueOf(sumPillsThursday));
            textViewPCFriday.setText(String.valueOf(sumPillsFriday));
            textViewPCSaturday.setText(String.valueOf(sumPillsSaturday));
            textViewPCSunday.setText(String.valueOf(sumPillsSunday));
        }

    }
}

package com.polytech.copill;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DayDetailsActivity extends AppCompatActivity {

    private int id;
    private String day;
    private ListView listView;
    private List<Detail> details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_details);
        Intent intent = getIntent();
        day = intent.getStringExtra("day");
        id = intent.getIntExtra("id",0);

        listView = (ListView)findViewById(R.id.listView);

        new DataBaseConnection().execute();
    }

    private class DataBaseConnection extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                ConnectionManager connectionManager = new ConnectionManager();
                Connection con = connectionManager.createConnection();
                Statement stmt1 = con.createStatement();
                String query1 = "SELECT name, quantity, hour FROM treatment,pill WHERE pill.id = treatment.id_pill AND treatment.id_user="+id+" AND treatment.days LIKE '%"+day+"%'";
                final ResultSet rs1 = stmt1.executeQuery(query1);

                details = new ArrayList<Detail>();

                while(rs1.next()){
                    details.add(new Detail(rs1.getString("name"),rs1.getInt("quantity"),rs1.getString("hour")));
                }

                connectionManager.closeConnection();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            DetailAdapter adapter = new DetailAdapter(DayDetailsActivity.this, details);
            listView.setAdapter(adapter);

        }

    }
}

package com.polytech.copill;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class AddPillActivity extends AppCompatActivity {

    Button buttonHour;
    TextView textViewHour,textViewQuantity;
    EditText editTextPillName;
    CheckBox checkBoxMonday,checkBoxTuesday,checkBoxWednesday,checkBoxThursday,checkBoxFriday,checkBoxSaturday,checkBoxSunday;
    ToggleButton toggleButtonNotification,toggleButtonAlarm,toggleButtonLight;
    private int id,quantity,notificationState,alarmState,lightState;
    private String pillName,hour,days;
    private Boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pill);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);

        buttonHour=(Button)findViewById(R.id.button_hour);
        textViewHour=(TextView)findViewById(R.id.textView_hour);
        textViewQuantity=(TextView)findViewById(R.id.textView_quantity);
        editTextPillName=(EditText)findViewById(R.id.editText_pillName);
        checkBoxMonday=(CheckBox)findViewById(R.id.checkBox_monday);
        checkBoxTuesday=(CheckBox)findViewById(R.id.checkBox_tuesday);
        checkBoxWednesday=(CheckBox)findViewById(R.id.checkBox_wednesday);
        checkBoxThursday=(CheckBox)findViewById(R.id.checkBox_thursday);
        checkBoxFriday=(CheckBox)findViewById(R.id.checkBox_friday);
        checkBoxSaturday=(CheckBox)findViewById(R.id.checkBox_saturday);
        checkBoxSunday=(CheckBox)findViewById(R.id.checkBox_sunday);
        toggleButtonNotification = (ToggleButton) findViewById(R.id.toggleButton_notification);
        toggleButtonAlarm = (ToggleButton) findViewById(R.id.toggleButton_alarm);
        toggleButtonLight = (ToggleButton) findViewById(R.id.toggleButton_light);
    }

    public void save (View view){
        int error = 0;
        days = "";
        pillName = editTextPillName.getText().toString();
        quantity = Integer.parseInt((String)textViewQuantity.getText());
        hour = textViewHour.getText().toString();
        hour += ":00";

        if(toggleButtonNotification.isChecked()){ notificationState = 1; }else{ notificationState = 0; }
        if(toggleButtonAlarm.isChecked()){ alarmState = 1; }else{ alarmState = 0; }
        if(toggleButtonLight.isChecked()){ lightState = 1; }else{ lightState = 0; }

        if(checkBoxMonday.isChecked()) {days = "monday";}
        if(checkBoxTuesday.isChecked()) {days += ",tuesday";}
        if(checkBoxWednesday.isChecked()) {days += ",wednesday";}
        if(checkBoxThursday.isChecked()) {days += ",thursday";}
        if(checkBoxFriday.isChecked()) {days += ",friday";}
        if(checkBoxSaturday.isChecked()) {days += ",saturday";}
        if(checkBoxSunday.isChecked()) {days += ",sunday";}

        if(days == null || days.isEmpty()){
            error=1;
        }
        else if(days.substring(0, 1).equals(",")){
            days = days.substring(1);
        }

        if(pillName == null || pillName.isEmpty()){
            error = 2;
        }

        if(error==0){
            new DataBaseConnection().execute(view);
        }
        else{
            if(error == 1){
                Toast.makeText(view.getContext(), "Vous devez sélectionner au moins un jour de la semaine !", Toast.LENGTH_SHORT).show();
            }
            if (error == 2){
                Toast.makeText(view.getContext(), "Vous devez indiquer le nom du médicament", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void cancel (View view){
        finish();
    }

    public void setHour (View view){
        // Get Current Time
        int mHour,mMinute;
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        textViewHour.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    public void setMorePills(View view){
        int q;
        q = Integer.parseInt((String)textViewQuantity.getText());

        if(q < 25){
            q+=1;
            textViewQuantity.setText(String.valueOf(q));
        }else {
            Toast.makeText(view.getContext(), "La quantité maximum est atteinte !", Toast.LENGTH_SHORT).show();
        }
    }

    public void setLessPills(View view){
        int q;
        q = Integer.parseInt((String)textViewQuantity.getText());

        if(q > 1){
            q-=1;
            textViewQuantity.setText(String.valueOf(q));
        }else {
            Toast.makeText(view.getContext(), "La quantité minimum est atteinte !", Toast.LENGTH_SHORT).show();
        }

    }

    private class DataBaseConnection extends AsyncTask<View,Void,View> {

        @Override
        protected View doInBackground(View... views) {
            try{
                ConnectionManager connectionManager = new ConnectionManager();
                Connection con = connectionManager.createConnection();
                Statement stmt1 = con.createStatement();
                String query1 = "SELECT id FROM pill WHERE name='"+pillName+"'";
                final ResultSet rs1 = stmt1.executeQuery(query1);
                int pillId;

                if(!rs1.next()){
                    Statement stmt2 = con.createStatement();
                    String query2 = "INSERT INTO pill VALUES (null, '"+pillName+"', null, null)";
                    stmt2.executeUpdate(query2);

                    Statement stmt3 = con.createStatement();
                    String query3 = "SELECT id FROM pill WHERE name='"+pillName+"'";
                    final ResultSet rs3 = stmt3.executeQuery(query3);
                    rs3.next();
                    pillId=rs3.getInt("id");

                    Statement stmt4 = con.createStatement();
                    String query4 = "INSERT INTO treatment VALUES (NULL, '"+id+"', '"+pillId+"', '"+quantity+"', '"+days+"', '"+hour+"', '"+notificationState+"', '"+alarmState+"', '"+lightState+"')";
                    stmt4.executeUpdate(query4);

                    isAdded=true;
                }
                else{
                    pillId=rs1.getInt("id");

                    Statement stmt5 = con.createStatement();
                    String query5 = "SELECT id FROM treatment WHERE id_pill='"+pillId+"' AND quantity='"+quantity+"' AND days='"+days+"' AND hour='"+hour+"'";
                    final ResultSet rs5 = stmt5.executeQuery(query5);

                    if(!rs5.next()){
                        Statement stmt6 = con.createStatement();
                        String query6 = "INSERT INTO treatment VALUES (NULL, '"+id+"', '"+pillId+"', '"+quantity+"', '"+days+"', '"+hour+"', '"+notificationState+"', '"+alarmState+"', '"+lightState+"')";
                        stmt6.executeUpdate(query6);

                        isAdded=true;
                    }
                }

                connectionManager.closeConnection();

            }catch (Exception e){
                e.printStackTrace();
            }
            return views[0];
        }

        @Override
        protected void onPostExecute(View view) {
            if(isAdded){
                finish();
            }
            else {
                Toast.makeText(view.getContext(),"Un ajout identique a déjà été effectué", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

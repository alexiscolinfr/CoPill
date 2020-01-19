package com.polytech.copill;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddPillActivity extends AppCompatActivity {

    Button buttonHour;
    TextView textViewHour,textViewQuantity;
    private int mHour,mMinute,quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pill);

        buttonHour=(Button)findViewById(R.id.button_hour);
        textViewHour=(TextView)findViewById(R.id.textView_hour);
        textViewQuantity=(TextView)findViewById(R.id.textView_quantity);
    }

    public void save (View view){
        finish();
    }

    public void cancel (View view){
        finish();
    }

    public void setHour (View view){
        // Get Current Time
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
        quantity = Integer.parseInt((String)textViewQuantity.getText());

        if(quantity < 25){
            quantity+=1;
            textViewQuantity.setText(String.valueOf(quantity));
        }else {
            Toast.makeText(view.getContext(), "La quantité maximum est atteinte !", Toast.LENGTH_SHORT).show();
        }
    }

    public void setLessPills(View view){
        quantity = Integer.parseInt((String)textViewQuantity.getText());

        if(quantity > 1){
            quantity-=1;
            textViewQuantity.setText(String.valueOf(quantity));
        }else {
            Toast.makeText(view.getContext(), "La quantité minimum est atteinte !", Toast.LENGTH_SHORT).show();
        }

    }
}

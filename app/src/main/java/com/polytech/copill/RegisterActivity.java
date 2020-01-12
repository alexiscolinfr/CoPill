package com.polytech.copill;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class RegisterActivity extends AppCompatActivity {

    private String firstName,lastName,email,password;
    private Boolean isRegistered = false;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void signUp(View view){
        EditText editTextFirstName = (EditText) findViewById(R.id.editText_firstName);
        EditText editTextLastName = (EditText) findViewById(R.id.editText_lastName);
        EditText editTextEmail = (EditText) findViewById(R.id.editText_email);
        EditText editTextPassword = (EditText) findViewById(R.id.editText_password);

        firstName = editTextFirstName.getText().toString();
        lastName = editTextLastName.getText().toString();
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Veuillez renseigner tous les champs du formulaire", Toast.LENGTH_SHORT).show();
        }
        else{
            new DataBaseConnection().execute(view);
        }
    }

    private class DataBaseConnection extends AsyncTask<View,Void,View> {

        @Override
        protected View doInBackground(View... views) {
            try{
                ConnectionManager connectionManager = new ConnectionManager();
                Connection con = connectionManager.createConnection();
                Statement stmt1 = con.createStatement();
                String query1 = "SELECT id FROM user WHERE email='"+email+"'";
                final ResultSet rs1 = stmt1.executeQuery(query1);

                if(!rs1.next()){
                    Statement stmt2 = con.createStatement();
                    String query2 = "INSERT INTO user VALUES (null, '"+firstName+"', '"+lastName+"', '"+email+"', '"+password+"')";
                    stmt2.executeUpdate(query2);
                    isRegistered=true;
                    Statement stmt3 = con.createStatement();
                    String query3 = "SELECT id FROM user WHERE email='"+email+"'";
                    final ResultSet rs3 = stmt3.executeQuery(query3);
                    rs3.next();
                    id = rs3.getInt("id");
                }

                connectionManager.closeConnection();

            }catch (Exception e){
                e.printStackTrace();
            }
            return views[0];
        }

        @Override
        protected void onPostExecute(View view) {
            if(isRegistered){
                isRegistered=false;
                Intent intent1 = new Intent(view.getContext(), HomeActivity.class);
                intent1.putExtra("id", id);
                intent1.putExtra("firstName", firstName);
                intent1.putExtra("lastName", lastName);
                startActivity(intent1);
            }
            else {
                Toast.makeText(view.getContext(), "Un compte est déjà associé à cette adresse email", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

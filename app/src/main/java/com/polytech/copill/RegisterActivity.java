package com.polytech.copill;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private String firstName,lastName,email,password,pillCode,errorMessage;
    private Boolean isRegistered = false;
    private int id;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_signUp);
    }

    public void signUp(View view){
        EditText editTextFirstName = (EditText) findViewById(R.id.editText_firstName);
        EditText editTextLastName = (EditText) findViewById(R.id.editText_lastName);
        EditText editTextPillCode = (EditText) findViewById(R.id.editText_pillCode);
        EditText editTextEmail = (EditText) findViewById(R.id.editText_email);
        EditText editTextPassword = (EditText) findViewById(R.id.editText_password);

        firstName = editTextFirstName.getText().toString();
        lastName = editTextLastName.getText().toString();
        pillCode = editTextPillCode.getText().toString();
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(firstName.isEmpty() || lastName.isEmpty() || pillCode.isEmpty() || email.isEmpty() || password.isEmpty()){
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
                publishProgress();
                Connection con = connectionManager.createConnection();
                publishProgress();
                Statement stmt1 = con.createStatement();
                String query1 = "SELECT id FROM user WHERE email='"+email+"'";
                final ResultSet rs1 = stmt1.executeQuery(query1);
                publishProgress();

                if(!rs1.next()){
                    publishProgress();
                    Statement stmt2 = con.createStatement();
                    String query2 = "SELECT code FROM product WHERE code='"+pillCode+"'";
                    final ResultSet rs2 = stmt2.executeQuery(query2);

                    if(rs2.next()){
                        publishProgress();
                        Statement stmt3 = con.createStatement();
                        String query3 = "SELECT product_code FROM register WHERE product_code='"+pillCode+"'";
                        final ResultSet rs3 = stmt3.executeQuery(query3);

                        if(!rs3.next()){
                            publishProgress();
                            Statement stmt4 = con.createStatement();
                            String query4 = "INSERT INTO user VALUES (null, '"+firstName+"', '"+lastName+"', '"+email+"', '"+password+"')";
                            stmt4.executeUpdate(query4);
                            isRegistered=true;

                            publishProgress();
                            Statement stmt5 = con.createStatement();
                            String query5 = "SELECT id FROM user WHERE email='"+email+"'";
                            final ResultSet rs5 = stmt5.executeQuery(query5);
                            rs5.next();
                            id = rs5.getInt("id");

                            final Date date = new Date();
                            String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);

                            publishProgress();
                            Statement stmt6 = con.createStatement();
                            String query6 = "INSERT INTO register VALUES ('"+id+"', '"+pillCode+"', '"+dateString+"')";
                            stmt6.executeUpdate(query6);

                        }else {
                            errorMessage="Ce pilulier est déjà associé à un compte";
                        }

                    }else {
                        errorMessage="Numéro de pilulier invalide";
                    }

                }else {
                    errorMessage="Un compte est déjà associé à cette adresse email";
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
                progressBar.setProgress(0);
                Intent intent1 = new Intent(view.getContext(), HomeActivity.class);
                intent1.putExtra("id", id);
                intent1.putExtra("firstName", firstName);
                intent1.putExtra("lastName", lastName);
                startActivity(intent1);
            }
            else {
                Toast.makeText(view.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                progressBar.setProgress(0);
            }
        }

        @Override
        protected void onProgressUpdate(Void... voids) {
            progressBar.incrementProgressBy(1);

        }
    }
}

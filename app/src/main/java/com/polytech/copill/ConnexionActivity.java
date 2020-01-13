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

public class ConnexionActivity extends AppCompatActivity {

    private String login,password,firstName,lastName;
    private int id;
    private Boolean isLogged = false;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_signIn);
    }

    public void signIn(View view){
        EditText editTextLogin = (EditText) findViewById(R.id.editText_login);
        EditText editTextPassword = (EditText) findViewById(R.id.editText_password);

        login = editTextLogin.getText().toString();
        password = editTextPassword.getText().toString();

        new DataBaseConnection().execute(view);
    }

    public void signUp(View view){
        Intent intent2 = new Intent(this, RegisterActivity.class);
        startActivity(intent2);
    }

    private class DataBaseConnection extends AsyncTask<View,Void,View> {

        @Override
        protected View doInBackground(View... views) {
            try{
                ConnectionManager connectionManager = new ConnectionManager();
                publishProgress();
                Connection con = connectionManager.createConnection();
                publishProgress();
                Statement stmt = con.createStatement();
                publishProgress();
                String query = "SELECT * FROM user WHERE email='"+login+"' AND password='"+password+"'";
                final ResultSet rs = stmt.executeQuery(query);
                publishProgress();

                if(rs.next()){
                    publishProgress();
                    isLogged=true;
                    id = rs.getInt("id");
                    firstName = rs.getString("first_name");
                    lastName = rs.getString("last_name");
                }

                connectionManager.closeConnection();

            }catch (Exception e){
                e.printStackTrace();
            }
            return views[0];
        }

        @Override
        protected void onPostExecute(View view) {
            if(isLogged){
                isLogged=false;
                progressBar.setProgress(0);
                Intent intent1 = new Intent(view.getContext(), HomeActivity.class);
                intent1.putExtra("id", id);
                intent1.putExtra("firstName", firstName);
                intent1.putExtra("lastName", lastName);
                startActivity(intent1);
            }
            else {
                Toast.makeText(view.getContext(), "Login et/ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                progressBar.setProgress(0);
            }
        }

        @Override
        protected void onProgressUpdate(Void... voids) {
            progressBar.incrementProgressBy(1);

        }
    }
}

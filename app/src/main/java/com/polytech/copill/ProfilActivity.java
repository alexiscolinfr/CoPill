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

public class ProfilActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword, editTextPassword2;
    private String firstName,lastName,email,password;
    private String newFirstName,newLastName,newEmail,newPassword,newPassword2;
    private int id;
    private boolean changeInformations=false,changePassword=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);

        editTextFirstName = (EditText)findViewById(R.id.editText_firstName);
        editTextLastName = (EditText)findViewById(R.id.editText_lastName);
        editTextEmail = (EditText)findViewById(R.id.editText_email);
        editTextPassword = (EditText)findViewById(R.id.editText_password);
        editTextPassword2 = (EditText)findViewById(R.id.editText_password2);

        new DataBaseConnection().execute(this.findViewById(android.R.id.content));
    }

    public void save(View view){
        boolean error=false;
        newFirstName = editTextFirstName.getText().toString();
        newLastName = editTextLastName.getText().toString();
        newEmail = editTextEmail.getText().toString();
        newPassword =editTextPassword.getText().toString();
        newPassword2 =editTextPassword2.getText().toString();

        if(!newFirstName.equals(firstName) || !newLastName.equals(lastName) || !newEmail.equals(email)){
            if(!newFirstName.isEmpty() && !newLastName.isEmpty() && !newEmail.isEmpty()){
                changeInformations=true;
            }else {
                error=true;
                Toast.makeText(view.getContext(), "Un champ est vide", Toast.LENGTH_SHORT).show();
            }
        }

        if((!newPassword.isEmpty() || !newPassword2.isEmpty()) && !error){
            if(!newPassword.isEmpty() && !newPassword2.isEmpty()){
                if(newPassword.equals(newPassword2)){
                    if(!newPassword.equals(password)){
                        changePassword=true;
                    }else {
                        error=true;
                        Toast.makeText(view.getContext(), "Mot de passe invalide", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    error=true;
                    Toast.makeText(view.getContext(), "Les mots de passe ne sont pas identiques", Toast.LENGTH_SHORT).show();
                }
            }else {
                error=true;
                Toast.makeText(view.getContext(), "Un champ est vide", Toast.LENGTH_SHORT).show();
            }
        }

        if ((changeInformations || changePassword) && !error){
            new DataBaseConnection().execute(view);
        }else {
            changeInformations=false;
            changePassword=false;
        }


    }

    private class DataBaseConnection extends AsyncTask<View,Void,View> {

        @Override
        protected View doInBackground(View... views) {
            try{
                ConnectionManager connectionManager = new ConnectionManager();
                Connection con = connectionManager.createConnection();

                if(changeInformations) {
                    Statement stmt1 = con.createStatement();
                    String query1 = "UPDATE user SET first_name='"+newFirstName+"', last_name='"+newLastName+"', email='"+newEmail+"' WHERE user.id = '"+id+"';";
                    stmt1.executeUpdate(query1);
                }
                if(changePassword) {
                    Statement stmt2 = con.createStatement();
                    String query2 = "UPDATE user SET password = '" + newPassword + "' WHERE id = '" + id + "';";
                    stmt2.executeUpdate(query2);
                }

                Statement stmt3 = con.createStatement();
                String query3 = "SELECT first_name, last_name, email, password FROM user WHERE id='"+id+"'";
                final ResultSet rs3 = stmt3.executeQuery(query3);

                if(rs3.next()) {
                    firstName = rs3.getString("first_name");
                    lastName = rs3.getString("last_name");
                    email = rs3.getString("email");
                    password = rs3.getString("password");
                }

                connectionManager.closeConnection();

            }catch (Exception e){
                e.printStackTrace();
            }
            return views[0];
        }

        @Override
        protected void onPostExecute(View view) {
            editTextFirstName.setText(firstName);
            editTextLastName.setText(lastName);
            editTextEmail.setText(email);
            editTextPassword.setText("");
            editTextPassword2.setText("");
            if(changeInformations && changePassword){
                Toast.makeText(view.getContext(), "Vos informations et votre mot de passe ont été mises à jour", Toast.LENGTH_SHORT).show();
            }else if (changeInformations){
                Toast.makeText(view.getContext(), "Vos informations ont été mises à jour", Toast.LENGTH_SHORT).show();
            }else if (changePassword){
                Toast.makeText(view.getContext(), "Votre mot de passe a été mis à jour", Toast.LENGTH_SHORT).show();
            }
            changeInformations=false;
            changePassword=false;
        }
    }
}

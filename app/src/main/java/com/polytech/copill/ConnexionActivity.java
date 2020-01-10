package com.polytech.copill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ConnexionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
    }

    public void signIn(View view){
        EditText editTextLogin = (EditText) findViewById(R.id.editText_login);
        EditText editTextPassword = (EditText) findViewById(R.id.editText_password);

        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();

        if(login.equals("admin@admin.com") && password.equals("admin")) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Login et/ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    public void signUp(View view){
        Toast.makeText(this, "Fonctionnalité indisponible", Toast.LENGTH_SHORT).show();
    }
}

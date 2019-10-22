package com.example.smarttechmardan.bilal20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin,buttonSignup;

    private static final String REGISTER_URL = "http://gkbtechnologies.com/Android/login.php";

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //////////////////////////////////////////
        sp = getSharedPreferences("login",MODE_PRIVATE);
        //if SharedPreferences contains usernname and password then directly redirect to Home Activity
        if (sp.contains("username")&&sp.contains("password")){
            startActivity(new Intent(getApplicationContext(),FregmentationActivity.class));
            finish();//finish current activity
        }


        //////////////////////////////////////////

        editTextUsername = (EditText) findViewById(R.id.editTextemail);
        editTextPassword = (EditText) findViewById(R.id.editTextpasword);
        buttonLogin = (Button) findViewById(R.id.buttonsignin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        buttonSignup=(Button)findViewById(R.id.buttonSU);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(j);
            }
        });

        //////////////////////////////////////////
       /* TextView a = (TextView) findViewById(R.id.textviewemail);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main2Activity.this, "email", Toast.LENGTH_SHORT).show();
            }
        });

        Button b = (Button) findViewById(R.id.buttonsignin);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextemail = (EditText) findViewById(R.id.editTextemail);
                EditText editTextPasword = (EditText) findViewById(R.id.editTextpasword);
                String etn = editTextemail.getText().toString();
                String etn1 = editTextPasword.getText().toString();
                String emailPatteren = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+";
                if (etn.isEmpty() || !etn.matches(emailPatteren)) {
                    Toast.makeText(Main2Activity.this, "Email is invalid", Toast.LENGTH_SHORT).show();
                } else if (etn1.isEmpty()) {
                    Toast.makeText(Main2Activity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                    startActivity(i);
                }


            }
        });*/
        //////////////////////////////////////////////

    }

    private void registerUser() {

        String username = editTextUsername.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim().toLowerCase();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Kindly enter the user name and password", Toast.LENGTH_SHORT).show();
        } else {
            register(username, password);
        }
    }

    private void register(final String username, final String password) {
        String urlSuffix = "?username=" + username + "&password=" + password;
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Main2Activity.this, "Please Wiat", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if (s.equalsIgnoreCase("Login Successfully")) {
                    SharedPreferences.Editor e=sp.edit();
                    e.putString("username",username);
                    e.putString("password",password);
                    e.commit();
                    Intent i = new Intent(getApplicationContext(), FregmentationActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL + s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                } catch (Exception e) {
                    return null;

                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }

}

package com.example.smarttechmardan.bilal20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;

    private Button buttonRegister;
    private Button buttonLogin;

    private static final String REGISTER_URL = "http://gkbtechnologies.com/Android/Add.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /////////////////////////////////////////

        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent h = new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(h);
            }
        });

        editTextName = (EditText)findViewById(R.id.editTextname);
        editTextUsername = (EditText)findViewById(R.id.editTextUN);
        editTextPassword = (EditText)findViewById(R.id.editTextpswrd);
        editTextEmail = (EditText)findViewById(R.id.editTextemail);

        buttonRegister = (Button)findViewById(R.id.buttonSignUp);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();

                //////////////////////////////
                EditText editTextname=(EditText)findViewById(R.id.editTextname);
                EditText editTextusername=(EditText)findViewById(R.id.editTextUN);
                EditText editTextemail=(EditText)findViewById(R.id.editTextemail);
                EditText editTextpaswrd=(EditText)findViewById(R.id.editTextpswrd);
                String name=editTextname.getText().toString();
                String username=editTextusername.getText().toString();
                String email1=editTextemail.getText().toString();
                String pswrd1=editTextpaswrd.getText().toString();
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(username) && TextUtils.isEmpty(email1)&& TextUtils.isEmpty(pswrd1))
                {
                    editTextname.setError("Name field is empty");
                    editTextusername.setError("UserName field is empty");
                    editTextemail.setError("Email field is empty");
                    editTextpaswrd  .setError("Password field is empty");
                    return;
                }

                if (TextUtils.isEmpty(username))
                {
                    editTextusername.setError("UserName field is empty");
                    return;
                }
                if (TextUtils.isEmpty(email1))
                {
                    editTextemail.setError("Email field is empty");
                    return;
                }
                if (TextUtils.isEmpty(pswrd1))
                {
                    editTextpaswrd  .setError("Password field is empty");
                    return;
                }
                else
                {
                    Intent l=new Intent(getApplicationContext(),Main2Activity.class);
                    startActivity(l);
                }
                //////////////////////////////////
            }
        });


    }

    private void registerUser(){
        String name = editTextName.getText().toString().trim().toLowerCase();
        name = name.replaceAll("[\\s()]+","-");
        String username = editTextUsername.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim().toLowerCase();
        String email = editTextEmail.getText().toString().trim().toLowerCase();
        if (name.isEmpty()||username.isEmpty()||password.isEmpty()||email.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        if ((username.contains(" "))|| (email.contains(" "))){
            Toast.makeText(getApplicationContext(), "No space is allowed in username or email", Toast.LENGTH_SHORT).show();
        }
        else{
            register(name,username,password,email);}
        }

        private void register(String name,String username,String password,String email) {
            String urlSuffix = "?name=" + name + "&username=" + username + "&password=" + password + "&email=" + email;
            class RegisterUser extends AsyncTask<String, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                    if (s.equalsIgnoreCase("User Added Successfully")) {
                        Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                        startActivity(i);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

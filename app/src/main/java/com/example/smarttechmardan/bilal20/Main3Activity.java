package com.example.smarttechmardan.bilal20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main3Activity extends AppCompatActivity {

    private EditText editTextId;
    private Button buttonGet;
    private TextView textViewResult;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //////////////////////////////////////////////////
        Button w = (Button)findViewById(R.id.buttonFreg);
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l = new Intent(getApplicationContext(),FregmentationActivity.class);
                startActivity(l);
            }
        });

        /////////////////////////////////////////////////////////////////////
        editTextId = (EditText)findViewById(R.id.editTextID);
        textViewResult = (TextView)findViewById(R.id.textViewResult);

        buttonGet = (Button)findViewById(R.id.buttonGet);
        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }
    private void getData(){
        String id = editTextId.getText().toString().trim();
        if (id.equals("")){
            Toast.makeText(this,"Please enter an id",Toast.LENGTH_LONG).show();
            return;
        }
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = Config.DATA_URL+editTextId.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void showJSON(String response){
        String name="";
        String username="";
        String password="";
        String email="";
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString(Config.KEY_NAME);
            username = collegeData.getString(Config.KEY_USERNAME);
            password = collegeData.getString(Config.KEY_PASSWORD);
            email = collegeData.getString(Config.KEY_EMAIL);
        }catch (JSONException e ){
            e.printStackTrace();
        }
        String result="<font color='red'><b>Name</b></font>\t\t\t\t\t\t\t"+name+
                "<br/><br/><font color='green'><i>Username</i></font>\t\t\t"+username+
                "<br/><br/><font color='blue'><u>Password</u></font>\t\t\t"+password+
                "<br/><br/><font color='magenta'>Email</font>\t\t\t\t\t\t\t"+email;
        textViewResult.setText(Html.fromHtml(result),TextView.BufferType.SPANNABLE);
        //textViewResult.setText("\nName:\t\t"+name+"\n\nUsername:\t\t"+username+"\n\nPassword:\t\t"+password+"\n\nEmail:\t\t"+email);
    }
}

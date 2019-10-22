package com.example.smarttechmardan.bilal20;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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

public class SearchActivity extends AppCompatActivity {

    private EditText editTextId;
    private Button buttonGet;
    private TextView textViewResult;
    private ProgressDialog loading;

    String urlAddress="http://gkbtechnologies.com/Android/Search.php";
    SearchView sv;
    ListView lv;
    ImageView noDataImg,noNetworkImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //
        editTextId = (EditText) findViewById(R.id.editTextId);
        buttonGet = (Button) findViewById(R.id.buttonGet);
        textViewResult= (TextView) findViewById(R.id.textViewResult);
        lv= (ListView) findViewById(R.id.lv);

        sv= (SearchView) findViewById(R.id.sv);
        noDataImg = (ImageView) findViewById(R.id.nodataImg);
        noNetworkImg = (ImageView) findViewById(R.id.noserver);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                lv.setVisibility(View.VISIBLE);
                SenderReceiver sr=new SenderReceiver(SearchActivity.this,urlAddress,query,lv,noDataImg,noNetworkImg);
                sr.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                lv.setVisibility(View.VISIBLE);
                textViewResult.setVisibility(View.INVISIBLE);
                SenderReceiver sr=new SenderReceiver(SearchActivity.this,urlAddress,query,lv,noDataImg,noNetworkImg);
                sr.execute();
                return false;
            }

        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position,
                                     long id) {

                String name = (String) parent.getItemAtPosition(position);
                //.........Custom Toast..............................//
                Toast toast = Toast.makeText(getApplicationContext(), name+" selected." +
                        "\nClick Search to see teh details", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.LEFT | Gravity.TOP, 0, 0);
                TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
                toastMessage.setTextColor(Color.CYAN);
                toast.show();
                ////////////////////////////////////////////////////////////////

                editTextId.setText(name);

            }
        });

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv.setVisibility(View.INVISIBLE);
                textViewResult.setVisibility(View.VISIBLE);
                getData();
            }
        });
    }/////////////////////////////on create ands//////////

    /////////////////....................................get data.................................//
    private void getData(){
        String id = editTextId.getText().toString().trim();
        if (id.equals("")) {
            Toast.makeText(this, "Plaese select any name first", Toast.LENGTH_SHORT).show();
            return;
        }
        loading = ProgressDialog.show(this,"Please wait...","Fetching ...",false,false);

        String url = Config.DATA_URLNAME+editTextId.getText().toString().trim();

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